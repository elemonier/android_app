package io.linger;

//// monitoring battery life
//import java.util.Locale;
//import android.app.FragmentTransaction;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class LandingActivity extends FragmentActivity 
{
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_landing);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
				{					
					@Override
					public void onPageSelected(int position)
					{
						getFragment(position);
					}
				});
		mViewPager.setCurrentItem(1);
//	    registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_LOW));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.landing, menu);
		return true;
	}

    /**
     * On selecting action bar icons
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
        case R.id.action_about:
            startActivity(new Intent(getApplicationContext(), AboutActivity.class));
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
	/**
	 * Returns the fragment corresponding with whichever position on the
	 * scroll you're at.
	 * @param position (0 [login], 1 [landing], or 2 [registration])
	 * @return the correct fragment
	 */
	public Fragment getFragment(int position)
	{
		switch (position)
		{
		case 0:
			Fragment loginFragment = new LoginFragment();
			return loginFragment;
		case 1:
			Fragment landingFragment = new SyncFragment();
			return landingFragment;
		case 2:
			Fragment registrationFragment = new RegistrationFragment();
			return registrationFragment;
		default:
			Fragment defaultFragment = new LoginFragment();
			return defaultFragment;
		}
	}
	
//	public void toLogin(View view)
//	{
//		getFragment(0);
//	}
//	
//	public void toRegistration(View view)
//	{
//		getFragment(2);
//	}

//	private final BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver()
//	{
//	    @Override
//	    public void onReceive(Context context, Intent intent) {
//			Log.w("battery low", "post contacts");
//			Intent myIntent = new Intent(LandingActivity.this, ShowContactsActivity.class);
//			LandingActivity.this.startActivity(myIntent);
//	    }
//	};
//
////	public void onDestroy() 
////	{
////	     unregisterReceiver(this.mBatInfoReceiver);
////	}
//	
//		public void onPause()
//		{
//		super.onPause();
//		}
	
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter
	{
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		/**
		 * Select which fragment to access from the landing page.
		 */
		@Override
		public Fragment getItem(int position)
		{
			switch (position)
			{
			case 0:
				Fragment loginFragment = new LoginFragment();
				return loginFragment;
			case 1:
				Fragment landingFragment = new SyncFragment();
				return landingFragment;
			case 2:
				Fragment registrationFragment = new RegistrationFragment();
				return registrationFragment;
			default:
				Fragment defaultFragment = new LoginFragment();
				return defaultFragment;
			}
		}

		/**
		 * GetCount for the number of pages.
		 */
		@Override
		public int getCount() 
		{
			// Show 3 total pages.
			return 3;
		}
	}
}
