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

public class ContactList implements Aggregatable
{
	private ArrayList<HashMap<String, String>> contacts;
	
	/**
	 * Default constructor.
	 */
	public ContactList()
	{
		this.contacts = new ArrayList<HashMap<String, String>>();
	}


	@Override
	public void add(Aggregatable aggregatable) {
		this.add((Aggregatable) aggregatable);
		
	}
	
	/**
	 * Adds a contact to the list of contacts.
	 */
	public void add(Contact newContact)
	{
		contacts.add(newContact.getMap());
	}
	
	/**
	 * Iterates through the ArrayList's HashMaps to create a String version.
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		for (HashMap<String, String> each : contacts)
		{
			for (HashMap.Entry<String, String> entry : each.entrySet())
			{
				String key = entry.getKey();
				Object value = entry.getValue();
				sb.append(key + ": " + value + ", ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	//http://google-gson.googlecode.com/svn/tags/1.2.3/docs/javadocs/com/google/gson/Gson.html
	//TODO
	public void postToDatabase()
	{
		Gson gson = new Gson();
		Log.v("Testing", gson.toJson(contacts));
	}
}
