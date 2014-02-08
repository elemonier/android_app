package io.linger;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;


public class HttpRequest extends AsyncTask<String, Integer, Double>{
	
	/**
	 * 
	 * @param Json
	 * @param url
	 * @param postHeader Application/json
	 */
	public HttpRequest(String Json, String url, String postHeader){
		execute(Json, url, postHeader);
	}

	@Override
	protected Double doInBackground(String... params) {
		postData(params[0], params[1], params[2]);
		return null;
	}

//	protected void onPostExecute(Double result){
//		pb.setVisibility(View.GONE);
//	}



	public void postData(String Json, String url, String postHeader) {

		HttpPost httppost = new HttpPost(url);
		try {

			httppost.setEntity(new StringEntity(Json));
			httppost.setHeader("Accept", "application/json");
			httppost.setHeader("Content-type", "application/json");
			new DefaultHttpClient().execute(httppost);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}

