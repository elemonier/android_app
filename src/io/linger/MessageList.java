package io.linger;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

import com.google.gson.Gson;

/**
 * Important to aggregate messages to be able to make a single http request.
 * @author Emily Pakulski
 * http://stackoverflow.com/questions/3138371/very-large-http-request-vs-many-small-requests
 */

public class MessageList implements Aggregatable
{
	private ArrayList<HashMap<String, String>> messages;
	
	/**
	 * Default constructor.
	 */
	public MessageList()
	{
		this.messages = new ArrayList<HashMap<String, String>>();
	}

	/**
	 * Casts the object passed as a message and adds it to the list of 
	 * messages.
	 */
	@Override
	public void add(Aggregatable aggregatable) {
		this.add((Message) aggregatable);
	}	
	
	/**
	 * Adds the newMessage to the list.
	 */
	public void add(Message newMessage)
	{
		messages.add(newMessage.getMap());
	}
	
	/**
	 * Iterates through the ArrayList's HashMaps to create a String version.
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		for (HashMap<String, String> each : messages)
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
	public void postToDatabase()
	{
		Gson gson = new Gson();
		Log.v("Testing", gson.toJson(messages));
	}
}
