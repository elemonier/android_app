package io.linger;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class Contact {
	
	public static final String KEY_UPLOAD_CONTACT = "post_contact"; 
	// KEYS FOR DATABASE USAGE TODO
	public static final String USER_ID = "user_id";
	public static final String USER_NAME = "user_name";
	public static final String USER_PHONE = "user_phone";
	public static final String USER_ENCRYPTED_PASS = "user_encrypted_pass";
	public static final String USER_SALT = "user_salt";
	public static final String USER_EMAIL = "user_email";

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
	private String poBox;
	private String street;
	private String city;
	private String state;
	private String postalCode;
	private String country;
	private String type; 
	
	public Contact(String contactId, String name, String phoneNumber,
			String emailAddress, String poBox, String street, 
			String city, String state, String postalCode, String country,
			String type)
	{
		this.contactId = contactId;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
		this.poBox = poBox;
		this.street = street;
		this.city = city;
		this.state = state;
		this.postalCode = postalCode;
		this.country = country;
		this.type = type;
	}
	
	public Contact(String contactId, String name, String phoneNumber, String emailAddress)
	{
		this.contactId = contactId;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
	}
	
	// TODO
	public JSONObject postToDatabase()
	{
		List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(JSONParser.KEY_TAG, KEY_UPLOAD_CONTACT));
        params.add(new BasicNameValuePair(USER_ID, contactId));
        params.add(new BasicNameValuePair(USER_NAME, name));
        params.add(new BasicNameValuePair(USER_PHONE, phoneNumber));
        params.add(new BasicNameValuePair(USER_EMAIL, emailAddress));
        
        JSONParser jsonParser = new JSONParser();
        JSONObject json = jsonParser.getJSONFromUrl(JSONParser.API_URL, params);
        return json;
	}
	
	public String toString()
	{
		String complete = "\n";
		complete += "ContactId: \t" + contactId + "\n";
		complete += "Name: \t" + name + "\n";
		complete += "Phone number: \t" + phoneNumber + "\n";
		complete += "Email address: \t" + emailAddress + "\n";
		return complete;
	}
}
