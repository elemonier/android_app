package io.linger;

import java.util.HashMap;

import android.util.Log;

import com.google.gson.Gson;

public class Contact
{	
	
	private String name;
	private String phoneNumber;
	private String emailAddress;
	
	public Contact( String name, String phoneNumber, String emailAddress)
	{
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
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
		sb.append("name : " + name + ", ");
		sb.append("phone : " + phoneNumber + ", ");
		sb.append("email : " + emailAddress);
		return sb.toString();
	}
	
	public String getName() { return name; };
	public String getEmail() { return emailAddress; }
	public String getPhone() { return phoneNumber; }
}
