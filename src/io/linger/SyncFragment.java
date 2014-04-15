package io.linger;

import java.sql.Date;
import java.util.ArrayList;

import com.google.gson.Gson;

import android.content.Context;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Fragment that handles syncing. Disables the button if the user is not
 * logged in; allows syncing with the database if the user is logged in.
 */
public class SyncFragment extends Fragment
{	
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		View rootView = inflater.inflate(R.layout.fragment_sync, container, false);

		// set title text font to our custom imported font
//		TextView titleText = (TextView) rootView.findViewById(R.id.title_label);
		TextView loginSwipeLabel = (TextView) rootView.findViewById(R.id.login_swipe_label);
		TextView registrationSwipeLabel = (TextView) rootView.findViewById(R.id.register_swipe_label);
		Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/century_gothic.ttf");
//		titleText.setTypeface(typeFace);
		loginSwipeLabel.setTypeface(typeFace);
		registrationSwipeLabel.setTypeface(typeFace);

		// create sync button
		Button syncButton = (Button) rootView.findViewById(R.id.button_sync);
		
		// disable button if user isn't logged in
//		SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(getActivity().getApplication());
//		if (db.getLoginRowCount() < 1) // if user is logged in
//			syncButton.setVisibility(View.INVISIBLE);
//		else // user is logged in
//		{
//			loginSwipeLabel.setVisibility(View.INVISIBLE);
//			registrationSwipeLabel.setVisibility(View.INVISIBLE);
//		}
			
		// set sync button listener
		syncButton.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v)
			{
				new SyncTask().execute();
				// TODO
				for(int i = 0; i < 3; i++)
				{
					Toast.makeText(getView().getContext(), "Syncing data...", 
							Toast.LENGTH_SHORT).show();
				}
				
			}
		});

		return rootView;
	}

	/** 
	 * AsyncTask to connect to database in order to upload the data from the
	 * user's phone.
	 */
	private class SyncTask extends AsyncTask<String, String, String>
	{   
		private ArrayList<Contact> contactList;
		private ArrayList<Message>	inbox;
		private ArrayList<Message>	outbox;

		/**
		 * Populate lists of contacts and messages and send HttpRequest for
		 * the contact list, inbox, and outbox.
		 */
		@Override
		protected String doInBackground(String... params) 
		{
			contactList = new ArrayList<Contact>();
			inbox = new ArrayList<Message>();
			outbox = new ArrayList<Message>();		

			Intent intentContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			getContactInfo(intentContact);
			retrieveMessages("inbox", inbox);
			retrieveMessages("sent", outbox);
			Gson gson = new Gson(); 
			
			TelephonyManager tMgr = (TelephonyManager) 
					getView().getContext().getSystemService(Context.TELEPHONY_SERVICE);
			String userPhoneNumber = tMgr.getLine1Number();
			
//			Log.v("TESTING PhoneMine", "My phone number is:" + mPhoneNumber);
			
//			new HttpRequest(gson.toJson(contactList).toString(), 
//					HttpRequest.SYNC_CONTACTS + userPhoneNumber, "Application/json");
//			new HttpRequest(gson.toJson(inbox).toString(), 
//					HttpRequest.SYNC_INBOX + userPhoneNumber, "inbox");
//			new HttpRequest(gson.toJson(outbox).toString(), 
//					HttpRequest.SYNC_OUTBOX + userPhoneNumber, "outbox");
			return null;
		}
		
		/**
		 * Called while doInBackground is running; shows progress bar while syncing.
		 */
//		protected void onProgressUpdate(String progress)
//		{
//			Toast.makeText(getView().getContext(), "Syncing data...", Toast.LENGTH_LONG).show();
//		}

		/**
		 * Confirm to user that the data has been synced.
		 */
		protected void onPostExecute(String result)
		{
			Toast.makeText(getView().getContext(), "Confirmed! Sync succesful.", Toast.LENGTH_LONG).show();
			// update text view with last sync time
			TextView lastSyncDate = (TextView) getActivity().findViewById(R.id.last_sync_date_label);
			lastSyncDate.setText("Last synced: " + DateTime.getCurrentDateTime());
		}
		
		//TODO Right now only grabs last contact email/phone
		// 		(overrides with every new number)
		//		needs to grab list of them and pass them somehow
		protected void getContactInfo(Intent intent) 
		{
			String[] projection = new String[] { 
					ContactsContract.Contacts._ID,
					ContactsContract.Contacts.DISPLAY_NAME };

			// return a cursor that points to the first row of a table 
			Cursor contact_cursor = getActivity().getContentResolver().query(intent.getData(),
					projection, // tells query which columns to return
					null, // tells query which rows to return 
					null, // give check_phn its parameter 
					null);	 // sort [optional] ~ not used

			// table structure - [ contactid - col1, lookup_key ,contactname - col3, contact_phn - col4 ]
			while (contact_cursor.moveToNext())
			{
				String con_id = contact_cursor.getString(0);
				String con_name = contact_cursor.getString(1);
				//				Log.v("con_id", con_id + " con_name "+ con_name);

				//TODO FIX!!! (natural-join later) 
				Cursor phones_cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ con_id, // The Query
						null, null);
				String phone_num = "none";
				if(phones_cursor.moveToNext()){
					phone_num = phones_cursor.getString(phones_cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				}
				phones_cursor.close();

				//TODO FIX!!! (natural-join later or something) 
				Cursor emails_cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, 
						null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + con_id, // The Query
						null,  null);
				String emailAddress = "none"; // default value if no email address
				if(emails_cursor.moveToNext())
				{
					emailAddress = emails_cursor.getString(emails_cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)); 
				}
				emails_cursor.close();
				//				Log.v("con_id", con_id + " con_name "+ con_name+" phone: "+ phone_num+" email: "+emailAddress);
				Contact newContact = new Contact(con_name, phone_num, emailAddress);
				contactList.add(newContact);
			} // while		
			contact_cursor.close();
		} // end method

		private void retrieveMessages(String whichBox, ArrayList<Message> messageList)
		{	

			Uri uriSMSURISent = Uri.parse("content://sms/" + whichBox);
			long last_day = new Date(System.currentTimeMillis() - 1L * 24 * 3600 * 1000).getTime();

			Cursor cursor = getActivity().getContentResolver().query(uriSMSURISent, null,
					"date" + ">?",new String[]{""+last_day},"date DESC");
			cursor.moveToFirst();
			while (cursor.moveToNext())
			{
				Message message;
				String threadId = "none";
				String phoneNumberAddress = "none";
				String content = "none";
				String date = "none";

				for(int thisMessage = 0; thisMessage < cursor.getColumnCount(); thisMessage++)
				{	
					String columnName = cursor.getColumnName(thisMessage);
					String value = cursor.getString(thisMessage);
					//					Log.v("columnName", whichBox + ", with columnName = " + columnName + ", value: " + value);
					if (columnName.equals("address"))
					{
						phoneNumberAddress = value;					
					}
					else if (columnName.equals("date"))
					{
						date = value;					
					}
					else if (columnName.equals("body"))
					{
						content = value;
					}
					else if (columnName.equals("thread_id"))
					{
						threadId = value;
					}
				}
//				Log.v("con_id", " which "+ whichBox +" phone: "+ phoneNumberAddress);
//						+" content: "+content + " date: " + date);
				message = new Message(threadId, phoneNumberAddress, content, date);
				messageList.add(message);
			} // end cursor while loop
		}
	} // end AsyncTask
}