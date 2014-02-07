package io.linger;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.google.gson.Gson;

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

/**
 * The second fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class LoginFragment extends Fragment
{	
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	public static final String TAG_LOGIN = "user_login";
	
	private String userPhoneNumber;
	private String userPassword;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		final View rootView = inflater.inflate(R.layout.fragment_login, container, false);
		
		// set title text font
		TextView myTextView = (TextView) rootView.findViewById(R.id.section_label);
		Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/grandhotel_regular.ttf");
		myTextView.setTypeface(typeFace);
		myTextView.setText("Login");
		
		// set listener for button
		Button submitButton = (Button) rootView.findViewById(R.id.button_submit_login);
		submitButton.setOnClickListener(new OnClickListener() 
		{
			   @Override
			   public void onClick(View view)
			   {
				   Log.v("Testing", "Clicked submit button");
				   userPhoneNumber = ((EditText) 
						   rootView.findViewById(R.id.phoneTextLogin)).getText().toString();
				   userPassword = ((EditText) 
						   rootView.findViewById(R.id.passEditTextLogin)).getText().toString();
					
				   new LoginTask().execute(userPhoneNumber, userPassword);
			   }
		});
		
		return rootView;
	}
	
	/** 
	 * AsyncTask to connect to database in order to check login information and
	 * scheck user values in SQLite Database.
	 */
	private class LoginTask extends AsyncTask<String, Void, String>
	{  
		 protected String doInBackground(String... inputs)
		 {
			 List<NameValuePair> params = new ArrayList<NameValuePair>(); 
			 params.add(new BasicNameValuePair(JSONParser.KEY_TAG, TAG_LOGIN));
			 params.add(new BasicNameValuePair(SQLiteDatabaseHandler.USER_PHONE, inputs[0]));
		     params.add(new BasicNameValuePair(SQLiteDatabaseHandler.USER_PASS, inputs[1]));
		     JSONParser.getJSONFromUrl(params);
		     Gson gson = new Gson();
		     Log.v("Testing", gson.toJson(params));
		     return gson.toJson(params);
		 }
		
//		protected String doInBackground(String ... params)
//		{
//            SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(getActivity());
//            db.addUser("1", userPhoneNumber, userPassword, "2/6/2014 18:08");
////			db.addUser(jsonUser.getString(DatabaseHandler.KEY_NAME), 
////            		jsonUser.getString(DatabaseHandler.KEY_EMAIL), 
////            		jsonUser.getString(DatabaseHandler.KEY_NETWORK),
////	                jsonUser.getString(DatabaseHandler.KEY_UNIQUE_ID), 
////	                jsonUser.getString(DatabaseHandler.KEY_CREATED_AT));  
//            return null;
//		}
	}
}