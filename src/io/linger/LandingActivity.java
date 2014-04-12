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

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class LandingActivity extends Activity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_landing);

		final View rootView = findViewById(android.R.id.content);
		
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
	
	/** Called on clicking the registration text. */
	public void toRegistration(View view) 
	{
	    Intent intent = new Intent(this, RegistrationActivity.class);
	    startActivity(intent);
	}
}
