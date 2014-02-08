package io.linger;

import java.sql.Date;
import java.util.ArrayList;

import com.google.gson.Gson;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.util.Log;

public class SyncDataActivity extends Activity
{
	//TODO NUMBER_OF_MESSAGES HAS to be by date, 20 in != 20 out in conversation
	private static final int NUMBER_OF_MESSAGES = 25; 
	private ArrayList<Contact> contactList;
	private ArrayList<Message>	inbox;
	private ArrayList<Message>	outbox;	

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);       
		contactList = new ArrayList<Contact>();
		inbox = new ArrayList<Message>();
		outbox = new ArrayList<Message>();		
		
		Intent intentContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
		getContactInfo(intentContact);
		retrieveMessages("inbox", inbox);
		retrieveMessages("sent", outbox);
		Gson gson = new Gson(); 
		
		//BuildJson with the 3 lists
		//Call HTTP Client class
		new HttpRequest(gson.toJson(contactList).toString(), "http://160.39.14.214:5000/app/contacts/6313349665", "Application/json");
		new HttpRequest(gson.toJson(inbox).toString(), "http://160.39.14.214:5000/app/inmessages/6313349665", "inbox");
		new HttpRequest(gson.toJson(outbox).toString(), "http://160.39.14.214:5000/app/outmessages/6313349665", "outbox");
//		Log.v("TESTING POST", gson.toJson(contactList).toString());
		
	}
	
	
	//TODO Right now only grabs last contact email/phone
	// 		(overrides with every new number)
	//		needs to grab list of them and pass them somehow
	protected void getContactInfo(Intent intent) 
	{
		String[] projection = new String[] { 
				ContactsContract.Contacts._ID,
				ContactsContract.Contacts.DISPLAY_NAME };

		// return a cursor that points to the first row of a table 
		Cursor contact_cursor = getContentResolver().query(intent.getData(),
				projection, // tells query which columns to return
				null, // tells query which rows to return 
				null, // give check_phn its parameter 
				null);	 // sort [optional] ~ not used

		// table structure - [ contactid - col1, lookup_key ,contactname - col3, contact_phn - col4 ]
		while (contact_cursor.moveToNext()) {
			String con_id = contact_cursor.getString(0);
			String con_name = contact_cursor.getString(1);
//			Log.v("con_id", con_id + " con_name "+ con_name);

			//TODO FIX!!! (natural-join later) 
			Cursor phones_cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ con_id, // The Query
					null, null);
			String phone_num = "none";
			if(phones_cursor.moveToNext()){
				phone_num = phones_cursor.getString(phones_cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			}
			phones_cursor.close();

			//TODO FIX!!! (natural-join later or something) 
			Cursor emails_cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, 
					ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + con_id, // The Query
					null,  null);
			String emailAddress = "none";
			if(emails_cursor.moveToNext()){
				emailAddress = emails_cursor.getString(emails_cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)); 
			}
			emails_cursor.close();
//			Log.v("con_id", con_id + " con_name "+ con_name+" phone: "+ phone_num+" email: "+emailAddress);
			Contact newContact = new Contact(con_name, phone_num, emailAddress);
			contactList.add(newContact);
		} // while		
		contact_cursor.close();
	} // end method

	private void retrieveMessages(String whichBox, ArrayList<Message> messageList)
	{	
		
		long last_day = new Date(System.currentTimeMillis() - 1L * 24 * 3600 * 1000).getTime();
		
		Cursor cursor = getContentResolver().query(Uri.parse("content://sms/"+ whichBox), null,
				"date" + ">?",new String[]{""+last_day},"date DESC");
		cursor.moveToFirst();
		for (int out_idx = 0; out_idx < NUMBER_OF_MESSAGES; out_idx++) // inbox stream
		{
			Message message;
			String threadId = "none";
			String phoneNumberAddress = "none";
			String content = "none";
			String date = "none";

			for(int currentMessage = 0; currentMessage < cursor.getColumnCount(); currentMessage++)
			{	
				String columnName = cursor.getColumnName(currentMessage);
				String value = cursor.getString(currentMessage);
//				Log.v("columnName", whichBox + ", with columnName = " + columnName + ", value: " + value);
				if (columnName.equals("address")){
					phoneNumberAddress = value;					
				}
				else if (columnName.equals("date")){
					date = value;					
				}
				else if (columnName.equals("body")){
					content = value;
				}
				else if (columnName.equals("thread_id")){
					threadId = value;
				}
			}
//			Log.v("con_id", " which "+ whichBox +" phone: "+ phoneNumberAddress);// +" content: "+content + " date: " + date);
			message = new Message(threadId, phoneNumberAddress, content, date);
			messageList.add(message);
			cursor.moveToNext();
		}
	}
}