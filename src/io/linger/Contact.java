package io.linger;

import java.util.HashMap;

import android.util.Log;

import com.google.gson.Gson;

public class Contact
{	
	public static final String KEY_UPLOAD_CONTACT = "post_contact"; 


	// KEYS FOR DATABASE USAGE TODO
	public static final String CONTACT_ID = "contact_id";
	public static final String CONTACT_USER = "contact_user";
	public static final String CONTACT_NAME = "contact_name";
	public static final String CONTACT_PHONE1 = "contact_phone1";
	public static final String CONTACT_PHONE2 = "contact_phone2";
	public static final String CONTACT_EMAIL1 = "contact_email1";
	public static final String CONTACT_EMAIL2 = "contact_email2";

	public static final String INMESSAGE_ID = "inmessage_id";
	public static final String INMESSAGE_USER = "inmessage_user";
	public static final String INMESSAGE_CONTACT = "inmessage_contact";
	public static final String INMESSAGE_CONTENT = "inmessage_content";
	public static final String INMESSAGE_WHEN_RECEIVED = "inmessage_when_received";

	public static final String OUTMESSAGE_ID = "inmessage_id";
	public static final String OUTMESSAGE_USER = "inmessage_user";
	public static final String OUTMESSAGE_CONTACT = "inmessage_contact";
	public static final String OUTMESSAGE_CONTENT = "inmessage_content";
	public static final String OUTMESSAGE_WHEN_RECEIVED = "inmessage_when_received";

	
	private String contactId;
	private String name;
	private String phoneNumber;
	private String emailAddress;
	
	public Contact(String contactId, String name, String phoneNumber, String emailAddress)
	{
		this.contactId = contactId;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
	}
	
	//http://google-gson.googlecode.com/svn/tags/1.2.3/docs/javadocs/com/google/gson/Gson.html
	//TODO
	public void postToDatabase()
	{
		Log.v("Testing", "Post contact to database");
		Gson gson = new Gson();
		Log.v("Testing", gson.toJson(this));
	}
	
	/**
	 * Returns a HashMap of all the values
	 * @return
	 */
	public HashMap<String, String> getMap()
	{
		HashMap<String, String> userInfo = new HashMap<String, String>();
        userInfo.put(SQLiteDatabaseHandler.USER_ID, contactId);
        userInfo.put(SQLiteDatabaseHandler.USER_NAME, name);
        userInfo.put(SQLiteDatabaseHandler.USER_PHONE, phoneNumber);
        userInfo.put(SQLiteDatabaseHandler.USER_EMAIL, emailAddress);
        return userInfo;
	}
	
	/**
	 * Returns a String with the most important information about this 
	 * contact.
	 * Used StringBuilder because apparently it consumes less memory:
	 * http://stackoverflow.com/questions/4645020/when-to-use-stringbuilder-in-java
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("ContactId: " + contactId + ", ");
		sb.append("Name: " + name + ", ");
		sb.append("Phone number: " + phoneNumber + ", ");
		sb.append("Email address: " + emailAddress);
		return sb.toString();
	}
}
