package io.linger;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.Menu;
import android.widget.TextView;

/**
 * Class that contains information about the app.
 *
 */

public class AboutActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		// set title text font to our custom imported font
		TextView titleText = (TextView) findViewById(R.id.title_label);
		Typeface typeFace = Typeface.createFromAsset(getAssets(),
				"fonts/grandhotel_regular.ttf");
		titleText.setTypeface(typeFace);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
	}
}
