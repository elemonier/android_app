package io.linger;

import java.util.ArrayList;

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
	private static final int PICK_CONTACT = 0;
	
	private String contactId;
	
	private String name;
	private String phoneNumber;
	private String emailAddress;
	private String poBox;
	private String street;
	private String city;
	private String state;
	private String postalCode;
	private String country;
	private String type; 
	
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
	  contactList.postToDatabase();
	  messageList.postToDatabase();
	  Log.v("Testing", "finished onCreate method");
	}//onCreate

	protected void getContactInfo(Intent intent)
	{
	   Cursor cursor =  managedQuery(intent.getData(), null, null, null, null);      
	   while (cursor.moveToNext()) 
	   {           
	       contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
	       name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)); 

	       String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

	       if ( hasPhone.equalsIgnoreCase("1"))
	           hasPhone = "true";
	       else
	           hasPhone = "false" ;

	       if (Boolean.parseBoolean(hasPhone)) 
	       {
	        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
	        while (phones.moveToNext()) 
	        {
	          phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	        }
	        phones.close();
	       }

	       // Find Email Addresses
	       Cursor emails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,null, null);
	       while (emails.moveToNext()) 
	       {
	        emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
	       }
	       emails.close();

	    Cursor address = getContentResolver().query(
	                ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
	                null,
	                ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = " + contactId,
	                null, null);
	    while (address.moveToNext()) 
	    { 
	      // These are all private class variables, don't forget to create them.
	      poBox      = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
	      street     = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
	      city       = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
	      state      = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
	      postalCode = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
	      country    = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
	      type       = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));
	    }  //address.moveToNext()   
	  
	   Contact newContact = new Contact(contactId, name, phoneNumber, emailAddress);
	   contactList.add(newContact);
//	   newContact.postToDatabase();
	   }  // while (cursor.moveToNext())        
	  cursor.close();
	} // getContactInfo
	
	/**
	 * Helper function that retrieve inbox msg
	 */
	private void retrieveInbox()
	{	
		Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
		cursor.moveToFirst();
		   
		for (int out_idx = 0; out_idx < 2; out_idx++) // inbox
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
				   Log.w("sms", "index is " + currentMessage + "  " + columnName + " ::: " + value);
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
			
			Log.w("INBOX", "XXXXXXXXXXXXXXXXXXXXXXX");
			cursor.moveToNext();
		}
	}
	
	/**
	 * Helper function that retrieve outbox msg
	 */
	private void retrieveOutbox()
	{
		Cursor cursor = getContentResolver().query(Uri.parse("content://sms/sent"), null, null, null, null);
		cursor.moveToFirst();

		   for (int out_idx = 0; out_idx < 2; out_idx++) // outbox
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
				   Log.w("sms", "index is " + currentMessage + "  " + columnName + " ::: " + value);
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
			   
			   Log.w("OUTBOX", "XXXXXXXXXXXXXXXXXXXXXXX");
			   cursor.moveToNext();
		   }
	}
	
	
	/**
	 * Retrieve phone text conversation
	 */
	private void retrievePhoneMessage() {
		retrieveInbox();
		retrieveOutbox();
	}
}