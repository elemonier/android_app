package io.linger;

import java.util.HashMap;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RegistrationFragment extends Fragment
{
	private String userName;
	private String userEmail;
	private String userPhoneNumber;
	private String userEncryptedPassword;
	private String userSalt;
	
	public static final String TAG_REGISTER = "user_register";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		final View rootView = inflater.inflate(R.layout.fragment_registration, container, false);
		
		// set title text font
		TextView titleText = (TextView) rootView.findViewById(R.id.section_label);
		Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/grandhotel_regular.ttf");
		titleText.setTypeface(typeFace);
		
		// set listener for button
		Button submitButton = (Button) rootView.findViewById(R.id.button_submit_login);
		submitButton.setOnClickListener(new OnClickListener() 
		{
			   @Override
			   public void onClick(View view)
			   {			   
				   Log.v("Testing", "Clicked registration submit button");
				   userName = ((EditText) 
						   rootView.findViewById(R.id.nameTextLogin)).getText().toString();
				   userEmail = ((EditText) 
						   rootView.findViewById(R.id.emailTextLogin)).getText().toString();
				   userPhoneNumber = ((EditText) 
						   rootView.findViewById(R.id.phoneTextLogin)).getText().toString();
				   String userUnencryptedPass = ((EditText) 
						   rootView.findViewById(R.id.passEditTextLogin)).getText().toString();
				   
				   if (userName == "" || userEmail == "" || userPhoneNumber ==
						   "" || userUnencryptedPass == "")
				   {
					   // TODO
					   // show error message
				   }
				   else
				   {
					   // encryption
						userSalt = Sha256Crypt.generateSalt();
						userEncryptedPassword = Sha256Crypt.Sha256_crypt(userUnencryptedPass, userSalt);
				   
					   new RegistrationTask().execute(userName, userEmail, userPhoneNumber, 
							   userEncryptedPassword);
				   }
			   }
		});
		return rootView;
	}


	/** 
	 * AsyncTask to connect to database in order to add a new row to the users table.
	 * It then automatically logs the user in.
	 */
	private class RegistrationTask extends AsyncTask<String, Void, HttpRequest>
	{  
		/**
		 *  Send a request in Json form to the server with the user's name, 
		 *  email, phone number, and password.
		 */
		protected HttpRequest doInBackground(String... inputs)
	    {
			// using Gson Builder http://www.mkyong.com/java/how-to-enable-pretty-print-json-output-gson/
			// turn the params into Json
		    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		    HashMap<String, String> params = new HashMap<String, String>();
			params.put(SQLiteDatabaseHandler.USER_NAME, inputs[0]);
			params.put(SQLiteDatabaseHandler.USER_EMAIL, inputs[1]);
			params.put(SQLiteDatabaseHandler.USER_PHONE, inputs[2]);
			params.put(SQLiteDatabaseHandler.USER_PASS, inputs[3]);
		    Log.v("Testing", gson.toJson(params));
		    return new HttpRequest(HttpRequest.URL_REGISTRATION, 
		    		gson.toJson(params), "POST");
		}
		
//		/**
//		 * After sending the request, try to log in.
//		 * @param json
//		 */
//		protected void onPostExecute(String json)
//		{
//			LoginFragment.LoginTask().execute(userPhoneNumber, userPassword);
//			// TODO may have to separate LoginTask into its own class for this to work
//		}
	}
}
