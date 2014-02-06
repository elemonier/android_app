package io.linger;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * The second fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class LoginFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	private String userPhoneNumber;
	private String userPassword;
	
	public LoginFragment() 
	{
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_login,
				container, false);
		
		TextView myTextView = (TextView) rootView.findViewById(R.id.section_label);
		Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(),"fonts/grandhotel_regular.ttf");
		myTextView.setTypeface(typeFace);
		
		myTextView.setText("Login");
		return rootView;
	}
	
	public void submitFields(View view)
	{
		userPhoneNumber = ((EditText) view.findViewById(R.id.phoneTextLogin)).getText().toString();
		userPassword = ((EditText) view.findViewById(R.id.passEditTextLogin)).getText().toString();
	}
	
	/** 
	 * AsyncTask to connect to database in order to check login information and
	 * set user and network values in SQLite Database.
	 */
	private class LoginTask extends AsyncTask<String, Void, String>
	{  
		protected String doInBackground(String ... params)
		{
            SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(getActivity());
//            db.addUser()
//			db.addUser(jsonUser.getString(DatabaseHandler.KEY_NAME), 
//            		jsonUser.getString(DatabaseHandler.KEY_EMAIL), 
//            		jsonUser.getString(DatabaseHandler.KEY_NETWORK),
//	                jsonUser.getString(DatabaseHandler.KEY_UNIQUE_ID), 
//	                jsonUser.getString(DatabaseHandler.KEY_CREATED_AT));  
            return null;
		}
	}
}