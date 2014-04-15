package io.linger.unrestricted_content;

/**
 * Activity that opens when you turn on the app.
 */

//// monitoring battery life
//import java.util.Locale;
//import android.app.FragmentTransaction;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;

import io.linger.HttpRequest;
import io.linger.R;
import io.linger.SQLiteDatabaseHandler;
import io.linger.Sha256Crypt;
import io.linger.UserFunctions;
import io.linger.R.id;
import io.linger.R.layout;
import io.linger.R.menu;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class LandingActivity extends ParentActivity_LoginRegister 
{
	public static final int PHONE_NUM_LEN = 10;
	public static final int MIN_PASS_LEN = 5;
	public static final int MAX_PASS_LEN = 20;
	
	private String phoneNumber;
	private String unencryptedPass;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_landing);

		setRootView(android.R.id.content);
		
		// set title text font to our custom imported font
		setFont(R.id.title_text);
		
		// set up checking for battery changes for when to upload
//	    registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_LOW));
	}

	
	/** Called when user clicks the send button in the action bar. */
	public void submitFields()
	{
		phoneNumber = ((EditText) 
				rootView.findViewById(R.id.phoneTextLogin)).getText().toString();
		 unencryptedPass = ((EditText) 
				rootView.findViewById(R.id.passEditTextLogin)).getText().toString();

		// check if fields are filled in
		if (phoneNumber.length() != PHONE_NUM_LEN)
			showToast("Enter your 10-digit phone number.");
		else if (unencryptedPass.length() < MIN_PASS_LEN || unencryptedPass.length() > MAX_PASS_LEN)
			showToast("Passwords must be " + MIN_PASS_LEN + "-" + MAX_PASS_LEN + " characters.");
		else // if they are filled in, try to log in
			new GetSaltTask().execute();
	}
	
	/** Called on clicking the registration text. */
	public void toRegistration(View view) 
	{
	    Intent intent = new Intent(this, RegistrationActivity.class);
	    startActivity(intent);
	}
	
	/**
	 * AsyncTask to connect to database in order to check login information and
	 * check user values in SQLite Database.
	 */
	public class GetSaltTask extends AsyncTask<String, Void, String>
	{  		
		private HttpRequest saltRequest;
		
		@Override
		protected String doInBackground(String... inputs)
		{
			HashMap<String, String> params = new HashMap<String, String>();
			params.put(SQLiteDatabaseHandler.USER_PHONE, phoneNumber);
		    
			// convert params to Json using Gson
		    Gson gson = new Gson(); 

		    // send HTTP post request
		    String saltReturn = HttpRequest.postData(gson.toJson(gson.toJson(params)), 
		    		HttpRequest.URL_LOGIN, "application/json");
		    Log.v("Testing", "GetSaltTask doInBackground");
		    Log.v("Testing", "Salt? " + saltReturn);
		    return saltReturn;
		}
		
		/**
		 * Get values back from server to put into SQLiteDatabase and log in.
		 */
		@Override
		protected void onPostExecute(String param)
		{
			Log.v("Testing", "2234 Check salt: " + param);
			new GetAccessToken().execute(param);
		}
	}
	
	/**
	 * POST the phoneNumber (de facto username) and encrypted pass in exchange
	 * for the access token for this session.
	 * @author Emily Pakulski
	 *
	 */
	public class GetAccessToken extends AsyncTask<String, Void, String>
	{
		private String encryptedPass;
		private String accessTokenRequest;
		
		protected String doInBackground(String... inputs)
		{
			Log.v("Testing", "Getting access token, hopefully.");
			encryptedPass = 
					Sha256Crypt.Sha256_crypt(unencryptedPass, inputs[0]);
			
			HashMap<String, String> params = new HashMap<String, String>();
			params.put(SQLiteDatabaseHandler.USER_PHONE, phoneNumber);
		    params.put(SQLiteDatabaseHandler.USER_HASH, encryptedPass);
		    
		    // convert params to Json
		    Gson gson = new Gson();
		    Log.v("Testing", gson.toJson(params));
		    
		    // send HTTP post request
		    String accessTokenRequest = HttpRequest.postData(gson.toJson(gson.toJson(params)), 
		    		HttpRequest.URL_LOGIN, "application/json");
		    // return the access token
		    return accessTokenRequest;
		}
		
		/**
		 * Get value, the access token, back from the server to login by
		 * putting info into the SQLiteDatabase, creating persistent login.
		 */
		@Override
		protected void onPostExecute(String param)
		{
			Log.v("Testing", "access token: " + param);
			UserFunctions.loginUser(getApplicationContext(), phoneNumber, param);
			
			Log.v("Testing", 
					"Is user logged in? " + UserFunctions.isUserLoggedIn(getApplicationContext()));
			
			Toast.makeText(rootView.getContext(), 
					"Login complete. Welcome!", Toast.LENGTH_SHORT).show();
		}
	}
}
