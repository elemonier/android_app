package io.linger;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

import com.google.gson.Gson;

/**
 * Important to aggregate contacts to be able to make a single http request.
 * @author Emily Pakulski
 * http://stackoverflow.com/questions/3138371/very-large-http-request-vs-many-small-requests
 */

public class ContactList {

	private ArrayList<HashMap<String, String>> contacts;
	
	/**
	 * Default constructor.
	 */
	public ContactList()
	{
		this.contacts = new ArrayList<HashMap<String, String>>();
	}

	/**
	 * 
	 */
	public void add(Contact newContact)
	{
		contacts.add(newContact.getMap());
	}
	
	/**
	 * 
	 */
//	public String toString()
//	{
//		String all = "";
//		for (Contact each : contacts)
//			all += each.toString();
//		return all;
//	}
	
	//http://google-gson.googlecode.com/svn/tags/1.2.3/docs/javadocs/com/google/gson/Gson.html
	//TODO
	public void postToDatabase()
	{
		Gson gson = new Gson();
		Log.v("Testing", gson.toJson(contacts));
	}
}
