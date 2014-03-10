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
import android.widget.Toast;

import com.google.gson.Gson;

/**
 * The second fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class LoginFragment extends Fragment
{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		final View rootView = inflater.inflate(R.layout.fragment_login, container, false);
		
		// set title text font to our custom imported font
		TextView titleText = (TextView) rootView.findViewById(R.id.section_label);
		Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/century_gothic.ttf");
		titleText.setTypeface(typeFace);
		
		// set listener for button
		Button submitButton = (Button) rootView.findViewById(R.id.button_submit_login);
		submitButton.setOnClickListener(new OnClickListener() 
		{
			   @Override
			   public void onClick(View view)
			   {
				   Log.v("Testing", "Clicked submit button");
				   String userPhoneNumber = ((EditText) 
						   rootView.findViewById(R.id.phoneTextLogin)).getText().toString();
				   String userUnencryptedPass = ((EditText) 
						   rootView.findViewById(R.id.passEditTextLogin)).getText().toString();

				   // encryption
//				   String userSalt = Sha256Crypt.generateSalt();
//				   String userEncryptedPass = Sha256Crypt.Sha256_crypt(userUnencryptedPassword, userSalt);
				   
				   new LoginTask().execute(userPhoneNumber, userUnencryptedPass);
				   
//				   SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(getActivity());
				   
				   // TODO if logged in
				   Toast.makeText(getView().getContext(), 
							"Login complete. Welcome!", Toast.LENGTH_LONG).show();
			   }
		});
		
		return rootView;
	}

	 
	/** 
	 * AsyncTask to connect to database in order to check login information and
	 * scheck user values in SQLite Database.
	 */
	public class LoginTask extends AsyncTask<String, Void, Void>
	{  
		 protected Void doInBackground(String... inputs)
		 {
			 HashMap<String, String> params = new HashMap<String, String>();
			 params.put(SQLiteDatabaseHandler.TEMP_USERNAME, inputs[0]);
		     params.put(SQLiteDatabaseHandler.USER_PASS, inputs[1]);
		     // convert params to Json
		     Gson gson = new Gson(); 
		     Log.v("Testing", gson.toJson(params));
		     // send HTTP post request
		     new HttpRequest(gson.toJson(gson.toJson(params)), 
					   HttpRequest.URL_LOGIN, "application/json");
		     
//		     SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(getActivity());
//		     db.addUser("1", inputs[0], inputs[1], DateTime.getCurrentDateTime());
		     return null;
		 }
		
		/**
		 * Get values back from server to put into SQLiteDatabase and log in.
		 */
		protected void onPostExecute(String ... params)
		{
			// put the SQLite stuff here? TODO
			Toast.makeText(getView().getContext(), 
					"Login complete. Welcome!", Toast.LENGTH_LONG).show();
		}
	}
}