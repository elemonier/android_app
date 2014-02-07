package io.linger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.util.Log;

/**
 * Deals with getting and storing data on the database.
 * 
 * @author Ravi Tamada, from androidhive.info
 */

public class JSONParser 
{ 
	// location of the API for the whole application
	public static final String API_URL = "http://160.39.142.43/linger_api/";
//	public static final String API_URL = "http://127.0.0.1/linger_api/";
//	public static final String API_URL = "/app";
	
	public static final String KEY_TAG = "tag";
	
	static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
 
    public static JSONObject getJSONFromUrl(List<NameValuePair> params)
    {
    	return getJSONFromUrl(API_URL, params);
    }
    
    public static JSONObject getJSONFromUrl(String url, List<NameValuePair> params)
    {
    	// Making HTTP request
        try {
        	Log.v("Testing", "Line 49 JSONParser");
        	DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse httpResponse = httpClient.execute(httpPost);
        	Log.v("Testing", "Line 53 JSONParser");
            HttpEntity httpEntity = httpResponse.getEntity();
        	Log.v("Testing", "Line 55 JSONParser");
            is = httpEntity.getContent();
            
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        try 
        {
        	Log.v("Testing", "Line 69 JSONParser");
        	BufferedReader reader = new BufferedReader(new 
            		InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
        	Log.v("Testing", "Line 74 JSONParser");
            while ((line = reader.readLine()) != null) 
            {
                sb.append(line + "n");
            }
            is.close();
            json = sb.toString();
//            Log.v("Testing", json); // TODO
            Log.e("JSON", json);
        }
        catch (Exception e)
        {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        // try parse the string to a JSON object
        try
        {
            jObj = new JSONObject(json);           
        }
        catch (JSONException e)
        {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return jObj; // return JSON String
    }
}