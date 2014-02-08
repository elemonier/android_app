package io.linger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

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
	public static final String ARG_SECTION_NUMBER = "section_number";

	Button button;

	public FirstFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		View rootView = inflater.inflate(R.layout.fragment_first,
				container, false);
		
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