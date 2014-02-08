package io.linger;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class FirstFragment extends Fragment
{
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		View rootView = inflater.inflate(R.layout.fragment_first,
				container, false);

		// set title text font to our custom imported font
		TextView titleText = (TextView) rootView.findViewById(R.id.title_label);
		TextView loginText = (TextView) rootView.findViewById(R.id.login_swipe_label);
		TextView registrationText = (TextView) rootView.findViewById(R.id.register_swipe_label);
		Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/grandhotel_regular.ttf");
		titleText.setTypeface(typeFace);
		loginText.setTypeface(typeFace);
		registrationText.setTypeface(typeFace);
		// set listener for sync button
		Button syncButton = (Button) rootView.findViewById(R.id.button_test);
		syncButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				Log.w("button click", "test");
				Intent myIntent = new Intent(getActivity(), SyncDataActivity.class);
				FirstFragment.this.startActivity(myIntent);
			}
		});

		return rootView;
	}

}