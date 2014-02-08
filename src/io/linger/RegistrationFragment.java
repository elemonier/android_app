package io.linger;

//import io.linger.LoginFragment.LoginTask;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * The second fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class RegistrationFragment extends Fragment
{
	private String userName;
	private String userEmail;
	private String userPhoneNumber;
	private String userPassword;
	
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
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
				   userPassword = ((EditText) 
						   rootView.findViewById(R.id.passEditTextLogin)).getText().toString();
					
				   new RegistrationTask().execute(userName, userEmail, userPhoneNumber, userPassword);
			   }
		});
		return rootView;
	}
	
	/** 
	 * AsyncTask to connect to database in order to add a new row to the users table.
	 * It then automatically logs the user in.
	 */
	private class RegistrationTask extends AsyncTask<String, Void, String>
	{  
		 protected String doInBackground(String... inputs)
		 {
			 List<NameValuePair> params = new ArrayList<NameValuePair>(); 
			 params.add(new BasicNameValuePair(JSONParser.KEY_TAG, TAG_REGISTER));
			 params.add(new BasicNameValuePair(SQLiteDatabaseHandler.USER_NAME, inputs[0]));
			 params.add(new BasicNameValuePair(SQLiteDatabaseHandler.USER_EMAIL, inputs[1]));
			 params.add(new BasicNameValuePair(SQLiteDatabaseHandler.USER_PHONE, inputs[2]));
		     params.add(new BasicNameValuePair(SQLiteDatabaseHandler.USER_PASS, inputs[3]));
		     JSONParser.getJSONFromUrl(params);
		     Gson gson = new Gson();
		     Log.v("Testing", gson.toJson(params));
		     return gson.toJson(params);
		 }
		
		protected void onPostExecute(String json)
		{
            SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(getActivity());
            // FILLER
            db.addUser("1", userPhoneNumber, userPassword, "2/6/2014 18:08");
//			db.addUser(jsonUser.getString(DatabaseHandler.KEY_NAME), 
//            		jsonUser.getString(DatabaseHandler.KEY_EMAIL), 
//            		jsonUser.getString(DatabaseHandler.KEY_NETWORK),
//	                jsonUser.getString(DatabaseHandler.KEY_UNIQUE_ID), 
//	                jsonUser.getString(DatabaseHandler.KEY_CREATED_AT)); 
		}
	}
}