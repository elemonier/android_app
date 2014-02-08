package io.linger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class HttpRequest extends AsyncTask<String, Integer, Double>{
	
	private ProgressBar pb;

	public HttpRequest(String Json, String url, String postHeader){
		execute(Json, url, postHeader);
	}
	
	@Override
	protected Double doInBackground(String... params) {
		pb.setVisibility(View.VISIBLE);
		postData(params[0], params[1], params[2]);
		return null;
	}

	protected void onPostExecute(Double result){
		pb.setVisibility(View.GONE);
	}
	
	protected void onProgressUpdate(Integer... progress){
		pb.setProgress(progress[0]);
	}

	public void postData(String Json, String url, String postHeader) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url); // "http://somewebsite.com/receiver.php"

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair(postHeader, Json));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			httpclient.execute(httppost);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}

}

