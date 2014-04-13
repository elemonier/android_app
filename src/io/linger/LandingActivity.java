package io.linger;

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

public class LandingActivity extends Activity 
{
	public static final int PHONE_NUM_LEN = 10;
	public static final int MIN_PASS_LEN = 5;
	public static final int MAX_PASS_LEN = 20;
	
	private View rootView;
	private String phoneNumber;
	private String unencryptedPass;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_landing);

		rootView = findViewById(android.R.id.content);
		
		// set title text font to our custom imported font
		TextView titleText = (TextView) rootView.findViewById(R.id.title_text);
		Typeface typeFace = Typeface.createFromAsset(this.getAssets(), 
				"fonts/century_gothic_bold.ttf");
		titleText.setTypeface(typeFace);
		
		// set up checking for battery changes for when to upload
//	    registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_LOW));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.landing, menu);
		return true;
	}
	
	/** 
	 * Calls certain methods depending on what action bar button is pressed.
	 * developer.android.com/training/basics/actionbar/adding-buttons.html
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
	    // Handle presses on the action bar items
	    switch (item.getItemId())
	    {
	        case (R.id.action_submit):
	            submitFields();
	            return true;
	        case (R.id.action_settings):
	            // openSettings();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
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
	
	/** Show the user the message string parameter passed. */
	public void showToast(String message)
	{
		Toast toast = Toast.makeText(rootView.getContext(), message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
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
		protected String doInBackground(String... inputs)
		{
			HashMap<String, String> params = new HashMap<String, String>();
			params.put(SQLiteDatabaseHandler.USER_PHONE, phoneNumber);
		    // convert params to Json
		    Gson gson = new Gson(); 
		    Log.v("Testing", gson.toJson(params));
		    // send HTTP post request
		    HttpRequest saltRequest = new HttpRequest(gson.toJson(gson.toJson(params)), 
		    		HttpRequest.URL_LOGIN, "application/json");
		    return saltRequest.getResponse();
		}
		
		/**
		 * Get values back from server to put into SQLiteDatabase and log in.
		 */
		protected void onPostExecute(String ... params)
		{
			new GetAccessToken().execute(params[0]);
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
		
		protected String doInBackground(String... inputs)
		{
			encryptedPass = 
					Sha256Crypt.Sha256_crypt(unencryptedPass, inputs[0]);
			
			HashMap<String, String> params = new HashMap<String, String>();
			params.put(SQLiteDatabaseHandler.USER_PHONE, phoneNumber);
		    params.put(SQLiteDatabaseHandler.USER_HASH, encryptedPass);
		    
		    // convert params to Json
		    Gson gson = new Gson();
		    Log.v("Testing", gson.toJson(params));
		    
		    // send HTTP post request
		    HttpRequest accessTokenRequest = new HttpRequest(gson.toJson(gson.toJson(params)), 
		    		HttpRequest.URL_LOGIN, "application/json");
		    // return the access token
		    return accessTokenRequest.getResponse();
		}
		
		/**
		 * Get value, the access token, back from the server to login by
		 * putting info into the SQLiteDatabase, creating persistent login.
		 */
		protected void onPostExecute(String ... params)
		{
			UserFunctions.loginUser(getApplicationContext(), phoneNumber, params[0]);
			
			Log.v("Testing", 
					"Is user logged in? " + UserFunctions.isUserLoggedIn(getApplicationContext()));
			
			Toast.makeText(rootView.getContext(), 
					"Login complete. Welcome!", Toast.LENGTH_SHORT).show();
		}
	}
}
