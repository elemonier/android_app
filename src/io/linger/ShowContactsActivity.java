package io.linger;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

/**
 * Source: http://stackoverflow.com/questions/866769/how-to-call-android-contacts-list
 * @author Emily Pakulski
 *
 */

public class ShowContactsActivity extends Activity
{
	private static final int NUMBER_OF_MESSAGES = 20;

	private ContactList contactList;
	private MessageList	messageList;

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);       
		contactList = new ContactList();
		messageList = new MessageList();

		Intent intentContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI); 
		getContactInfo(intentContact);
		retrievePhoneMessage();
		// TODO why isn't this working!!??
		DataAggregator aggregator = new DataAggregator(contactList, messageList);
		aggregator.postToDatabase();
		Log.v("Testing", "finished onCreate method");
	}//onCreate

	//TODO Right now only grabs last contact email/phone, needs to grab list of them and pass them somehow
	protected void getContactInfo(Intent intent) 
	{

		String[] projection    = new String[] { ContactsContract.Contacts._ID,
				ContactsContract.Contacts.LOOKUP_KEY,
				ContactsContract.Contacts.DISPLAY_NAME,
				ContactsContract.Contacts.HAS_PHONE_NUMBER
		};

		// return a cursor that points to the first row of a table 

		Cursor contact_cursor = getContentResolver().query(intent.getData(),   
				projection, // tells query which columns to return
				null, // tells query which rows to return 
				null, // give check_phn its parameter 
				null);	 // sort [optional] ~ not used

		// iterates through the returned table
		// table structure - [ contactid - col1, lookup_key ,contactname - col3, contact_phn - col4 ]

		while (contact_cursor.moveToNext()) {
			String con_id = contact_cursor.getString(0);
			String con_look_up_key = contact_cursor.getString(1);
			String con_name = contact_cursor.getString(2);
			String con_has_phone = contact_cursor.getString(3);
			// debugging
					 Log.v("con_id", con_id);
					 Log.v("con_key", con_look_up_key);
					 Log.v("con_name", con_name);

			// FIX!!! 
			Cursor phones_cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
					null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ con_id,null, null);

			// iterate through the whole phone table to grab the person with 'contactid'

			// FIX!!!
			String phone_num = "";
			while(phones_cursor.moveToNext()){
				phone_num = phones_cursor.getString(phones_cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				 Log.v("phone num: ", phone_num);
			}
			phones_cursor.close();
			// Find Email Addresses 

			// FIX!!! (another computational sucky call)
			Cursor emails_cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, 
					ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + con_id, null,  null);

			// retrieve email
			String emailAddress = "";
			while (emails_cursor.moveToNext()){
				emailAddress = emails_cursor.getString(emails_cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)); 
			 Log.v("email : ", emailAddress);
			}
			emails_cursor.close();
			
			Contact newContact = new Contact(con_look_up_key, con_name, phone_num, emailAddress);
			contactList.add(newContact);
			
		} // while
		contact_cursor.close();
	} // end method

	/**
	 * Retrieve phone text conversation
	 */
	private void retrievePhoneMessage()
	{
		retrieveInbox();
		retrieveOutbox();
	}

	/**
	 * Helper function that retrieve inbox msg
	 */
	private void retrieveInbox()
	{	
		Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
		cursor.moveToFirst();

		for (int out_idx = 0; out_idx < NUMBER_OF_MESSAGES; out_idx++) // inbox
		{
			Message message;

			String threadId = "none";
			String phoneNumberAddress = "none";
			String content = "none";
			String senderName = "none";
			String dateSent = "none";

			for(int currentMessage = 0; currentMessage < cursor.getColumnCount(); currentMessage++)
			{	
				String columnName = cursor.getColumnName(currentMessage);
				String value = cursor.getString(currentMessage);
				//				   Log.w("sms", "index is " + currentMessage + "  " + columnName + " ::: " + value);
				if (columnName.equals(Message.THREAD_ID))
					threadId = value;
				else if (columnName.equals(Message.PHONE_NUMBER_ADDRESS))
					phoneNumberAddress = value;
				else if (columnName.equals(Message.CONTENT))
					content = value;
				else if (columnName.equals(Message.NAME))
					senderName = value;
				else if (columnName.equals(Message.DATE_SENT))
					dateSent = value;
			}
			message = new Message(threadId, phoneNumberAddress, content, senderName, dateSent);
			messageList.add(message);

			//			Log.w("INBOX", "XXXXXXXXXXXXXXXXXXXXXXX");
			cursor.moveToNext();
		}
	}

	/**
	 * Helper function that retrieve outbox msg
	 */
	private void retrieveOutbox()
	{
		Cursor cursor = getContentResolver().query(Uri.parse("content://sms/sent"), 
				null, null, null, null);
		cursor.moveToFirst();

		for (int out_idx = 0; out_idx < NUMBER_OF_MESSAGES; out_idx++) // outbox
		{
			Message message;

			String threadId = "none";
			String phoneNumberAddress = "none";
			String content = "none";
			String senderName = "none";
			String dateSent = "none";

			for(int currentMessage = 0; currentMessage<cursor.getColumnCount(); currentMessage++) // for each message
			{
				String columnName = cursor.getColumnName(currentMessage);
				String value = cursor.getString(currentMessage);
				//				   Log.w("sms", "index is " + currentMessage + "  " + columnName + " ::: " + value);
				if (columnName.equals(Message.THREAD_ID))
					threadId = value;
				else if (columnName.equals(Message.PHONE_NUMBER_ADDRESS))
					phoneNumberAddress = value;
				else if (columnName.equals(Message.CONTENT))
					content = value;
				else if (columnName.equals(Message.NAME))
					senderName = value;
				else if (columnName.equals(Message.DATE_SENT))
					dateSent = value;
			}
			message = new Message(threadId, phoneNumberAddress, content, senderName, dateSent);
			messageList.add(message);

			//			   Log.w("OUTBOX", "XXXXXXXXXXXXXXXXXXXXXXX");
			cursor.moveToNext();
		}
	}
}