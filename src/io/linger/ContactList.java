package io.linger;

import java.util.ArrayList;

/**
 * Important to aggregate contacts to be able to make a single http request.
 * @author Emily Pakulski
 * http://stackoverflow.com/questions/3138371/very-large-http-request-vs-many-small-requests
 */

public class ContactList {

	private ArrayList<Contact> contacts;
	
	public ContactList()
	{
		this.contacts = new ArrayList<Contact>();
	}
	
	public void add(Contact newContact)
	{
		contacts.add(newContact);
	}
	
	public String toString()
	{
		String all = "";
		for (Contact each : contacts)
			all += each.toString();
		return all;
	}
	
	//TODO
	public void postToDatabase()
	{
		
	}
}
