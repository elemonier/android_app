package io.linger;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class HttpRequest extends AsyncTask<String, Integer, Void>
{
	public static final String URL = "http://www.linger.io";
	public static final String URL_REGISTRATION = URL + "/app/register";
	public static final String URL_LOGIN = URL + "/app/login";
	public static final String SYNC_CONTACTS = URL + "/app/contacts/";
	public static final String SYNC_INBOX = URL + "/app/inmessages/";
	public static final String SYNC_OUTBOX = URL + "/app/outmessages/";

	/**
	 * 
	 * @param json
	 * @param url
	 * @param postHeader Application/json
	 */
	public HttpRequest(String json, String url, String postHeader)
	{
		execute(json, url, postHeader);
		Log.v("Testing", json);
	}

	public HttpRequest(String json, String postHeader)
	{
		this(json, URL, postHeader);
	}

	@Override
	protected Void doInBackground(String... params)
	{
		postData(params[0], params[1], params[2]);
		return null;
	}

	public void postData(String jsonStr, String urlString, String postHeader)
	{
		HttpPost httppost = new HttpPost(urlString);
		Log.v("Testing", urlString);
		try
		{
			httppost.setEntity(new StringEntity(jsonStr));
			httppost.setHeader("Accept", "application/json");
			httppost.setHeader("Content-type", "application/json");
			new DefaultHttpClient().execute(httppost);
		}
		catch (ClientProtocolException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}	
	}
}

