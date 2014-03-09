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

//	protected void onPostExecute(Double result)
//	{
//		pb.setVisibility(View.GONE);
//	}

	public void postData(String jsonStr, String urlString, String postHeader)
	{
//		// my attempt: http://stackoverflow.com/questions/13911993/sending-a-json-http-post-request-from-android
//		URLConnection urlConn;
//		DataOutputStream printout;
////		DataInputStream input;
//		try {
//			urlConn = new URL(urlString).openConnection();
//			urlConn.setDoInput(true);
//			urlConn.setDoOutput(true);
//			urlConn.setUseCaches(false);
//			urlConn.setRequestProperty("Content-Type","application/json");   
//			urlConn.setRequestProperty("Host", "linger.io");
//				urlConn.connect();
//			
//			printout = new DataOutputStream(urlConn.getOutputStream());
//			Log.v("Testing", "Being posted: " + URLEncoder.encode(jsonStr,"UTF-8"));
//			printout.writeBytes(jsonStr);
//			printout.writeUTF(URLEncoder.encode(jsonStr,"UTF-8"));
//			printout.flush();
//			printout.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		HttpPost httppost = new HttpPost(URL);
		
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

