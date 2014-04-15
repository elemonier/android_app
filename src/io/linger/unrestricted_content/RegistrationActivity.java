package io.linger.unrestricted_content;

import io.linger.HttpRequest;
import io.linger.R;
import io.linger.SQLiteDatabaseHandler;
import io.linger.Sha256Crypt;
import io.linger.UserFunctions;

import java.util.HashMap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

public class RegistrationActivity extends ParentActivity_LoginRegister
{
	private String userSalt;
	
	public static final String TAG_REGISTER = "user_register";

	private String phoneNumber;
	private String unencryptedPass;
	private String userName;
	private String userEmail;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		
		setRootView(android.R.id.content);
	}

	/** Called when user clicks the send button in the action bar. */
	public void submitFields()
	{
		/* get values from text fields */
		userName = ((EditText)
				rootView.findViewById(R.id.nameTextRegistration)).getText().toString();
		userEmail = ((EditText)
				rootView.findViewById(R.id.emailTextRegistration)).getText().toString();
		phoneNumber = ((EditText) 
				rootView.findViewById(R.id.phoneTextLogin)).getText().toString();
		 unencryptedPass = ((EditText) 
				rootView.findViewById(R.id.passEditTextLogin)).getText().toString();

		/* check if fields are filled in. Notify user if there is an error. */
		// check username
		if(userName.length() < 2 || userName.length() > 15)
			showToast("Enter your 2-15 letter name.");
		// check email address
		else if(!isValidEmailAddress(userEmail))
			showToast("Enter a valid email address.");
		// check phone number
		else if (phoneNumber.length() != LandingActivity.PHONE_NUM_LEN)
			showToast("Enter your 10-digit phone number.");
		// check password
		else if (unencryptedPass.length() < LandingActivity.MIN_PASS_LEN || 
				unencryptedPass.length() > LandingActivity.MAX_PASS_LEN)
			showToast("Passwords must be " + LandingActivity.MIN_PASS_LEN + "-" 
				+ LandingActivity.MAX_PASS_LEN + " characters.");
		else // if they are filled in, try to log in
			new RegistrationTask().execute();
	}
	
	/**
	 * Returns true if valid email, false if not.
	 * @param email
	 */
	public boolean isValidEmailAddress(String email) 
	{
	    // email addresses are a max of 256 chars long
		if (email.length() > 256) 
	    	return false;
		if (!email.contains("@"))
			return false;
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(".+@.+\\.[a-z]+");
	       java.util.regex.Matcher m = p.matcher(email);
	       return m.matches();
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
//		    HttpRequest accessTokenRequest = new HttpRequest(gson.toJson(gson.toJson(params)), 
//		    		HttpRequest.URL_LOGIN, "application/json");
//		    // return the access token
//		    return accessTokenRequest.getResponse();
		    return ""; // TEMP FIX WHILE WORKING ON HTTPREQUEST CLASS
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
