package io.linger;

import io.linger.LandingActivity.GetSaltTask;

import java.util.HashMap;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RegistrationActivity extends Activity
{
	private String userSalt;
	
	public static final String TAG_REGISTER = "user_register";

	private String phoneNumber;
	private String unencryptedPass;
	private String userName;
	private String userEmail;
	
	private View rootView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		
		rootView = findViewById(android.R.id.content);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registration, menu);
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
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/** Show the user the message string parameter passed. */
	public void showToast(String message)
	{
		Toast toast = Toast.makeText(rootView.getContext(), message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
	}
	
	/** Called when user clicks the send button in the action bar. */
	public void submitFields()
	{
		userName = ((EditText)
				rootView.findViewById(R.id.nameTextRegistration)).getText().toString();
		userEmail = ((EditText)
				rootView.findViewById(R.id.emailTextRegistration)).getText().toString();
		phoneNumber = ((EditText) 
				rootView.findViewById(R.id.phoneTextLogin)).getText().toString();
		 unencryptedPass = ((EditText) 
				rootView.findViewById(R.id.passEditTextLogin)).getText().toString();

		// check if fields are filled in
		if(userName.length() < 2 || userName.length() > 15)
			showToast("Enter your 2-15 letter name.");
	
		if (phoneNumber.length() != LandingActivity.PHONE_NUM_LEN)
			showToast("Enter your 10-digit phone number.");
		else if (unencryptedPass.length() < LandingActivity.MIN_PASS_LEN || 
				unencryptedPass.length() > LandingActivity.MAX_PASS_LEN)
			showToast("Passwords must be " + LandingActivity.MIN_PASS_LEN + "-" 
				+ LandingActivity.MAX_PASS_LEN + " characters.");
		else // if they are filled in, try to log in
			new RegistrationTask().execute();
	}
	
	/** Called on clicking the login text. Simply closes this activity to 
	 * return to the previous one. */
	public void toLogin(View view)
	{
		finish();
	}


	/** 
	 * AsyncTask to POST name, email, phone, and hash to get an access token back.
	 */
	private class RegistrationTask extends AsyncTask<String, Void, String>
	{
		private String encryptedPass;
		
		/**
		 *  Send a request in Json form to the server with the user's name, 
		 *  email, phone number, and password.
		 */
		protected String doInBackground(String... inputs)
	    {
			encryptedPass = 
					Sha256Crypt.Sha256_crypt(unencryptedPass, Sha256Crypt.generateSalt());
			
			HashMap<String, String> params = new HashMap<String, String>();
			params.put(SQLiteDatabaseHandler.USER_PHONE, phoneNumber);
			params.put(SQLiteDatabaseHandler.USER_EMAIL, userEmail);
			params.put(SQLiteDatabaseHandler.USER_NAME, userName);
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
//			UserFunctions.loginUser(getApplicationContext(), phoneNumber, params[0]);
//			
			Log.v("Testing", 
					"Registered. Is user logged in? " +
							UserFunctions.isUserLoggedIn(getApplicationContext()));
//			
//			Toast.makeText(rootView.getContext(), 
//					"Login complete. Welcome!", Toast.LENGTH_SHORT).show();
		}
	}
}
