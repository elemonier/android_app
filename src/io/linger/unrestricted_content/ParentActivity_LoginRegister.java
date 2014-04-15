package io.linger.unrestricted_content;

import io.linger.R;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public abstract class ParentActivity_LoginRegister extends Activity
{
	protected View rootView;
	
	public void setRootView(int viewID)
	{
		rootView = findViewById(viewID);
	}
	
	/**
	 * Set the font for our custom font for the text in the view.
	 * @param viewID view whose font will be changed
	 */
	public void setFont(int viewID)
	{
		TextView titleText = (TextView) rootView.findViewById(viewID);
		Typeface typeFace = Typeface.createFromAsset(this.getAssets(), 
				"fonts/century_gothic_bold.ttf");
		titleText.setTypeface(typeFace);
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.landing, menu);
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
	        case (R.id.action_settings):
	            // openSettings();
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
	
	public abstract void submitFields();
}
