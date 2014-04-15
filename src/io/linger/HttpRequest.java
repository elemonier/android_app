package io.linger;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpRequest
{
	public static final String URL = "http://www.linger.io";
	public static final String URL_REGISTRATION = URL + "/app/register";
	public static final String URL_LOGIN = URL + "/app/login";
	public static final String SYNC_CONTACTS = URL + "/app/contacts/";
	public static final String SYNC_INBOX = URL + "/app/inmessages/";
	public static final String SYNC_OUTBOX = URL + "/app/outmessages/";

	private static String response;
	
//	/**
//	 * 
//	 * @param json
//	 * @param url
//	 * @param postHeader Application/json
//	 */
//	public HttpRequest(String json, String url, String postHeader)
//	{
//		execute(json, url, postHeader);
//		Log.v("Testing", json);
//	}
//
//	public HttpRequest(String json, String postHeader)
//	{
//		this(json, URL, postHeader);
//	}
//
//	@Override
//	protected Void doInBackground(String... params)
//	{
//		Log.v("Testing", "calling doInBackground");
//		postData(params[0], params[1], params[2]);
//		return null;
//	}

	// http://cdrussell.blogspot.com.es/2011/12/android-get-body-of-http-response-as.html
	public static String postData(String jsonStr, String urlString, String postHeader)
	{
		DefaultHttpClient http = new DefaultHttpClient();
		
		HttpPost httppost = new HttpPost(urlString);
		Log.v("Testing", "Posting data to " + urlString);
		
		try
		{
			httppost.setEntity(new StringEntity(jsonStr));
			httppost.setHeader("Accept", "application/json");
			httppost.setHeader("Content-type", "application/json");
			
			HttpResponse httpResponse = http.execute(httppost);
			
			int responseCode = httpResponse.getStatusLine().getStatusCode();
			switch(responseCode)
			{
			    case (200):
			        HttpEntity entity = httpResponse.getEntity();
			    	if(entity != null)
			    	{
			    		response = EntityUtils.toString(entity);
			    		return response;
			    	}
			        break;
			    default:
			    	return "error";
			} 
		}

		catch (ClientProtocolException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return "error";	
	}
	
	/** Returns whatever response this HttpRequest got. */
	public String getResponse()
	{
		if (response != null)
		{
			Log.v("Testing", "Response: " + response);
			return response;
		}
		else
		{
			Log.v("Testing", "No response registered");
			return "";
		}
	}
}

