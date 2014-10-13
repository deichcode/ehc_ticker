package de.deichcode.ehc_neuwiedticker;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	private String home;
	private String guest;
	public Context mContext;
	private Timer timer = new Timer();
	private RefreshTimer rt = new RefreshTimer();
	private int activeTimerTime;
	private boolean isActiveTimer;

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	public String getGuest() {
		return guest;
	}

	public void setGuest(String guest) {
		this.guest = guest;
	}

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		isActiveTimer =  sharedPref.getBoolean(TickerSettingsActivity.PREFS_KEY_AUTO_UPDATE_ONSCREEN, true);
		if (sharedPref.getInt(TickerSettingsActivity.PREFS_KEY_AUTO_UPDATE_ONSCREEN_PERIOD, 301) == 301){
			sharedPref = this.getPreferences(Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putInt(TickerSettingsActivity.PREFS_KEY_AUTO_UPDATE_ONSCREEN_PERIOD, 30);
			editor.apply();
		}
		activeTimerTime = (sharedPref.getInt(TickerSettingsActivity.PREFS_KEY_AUTO_UPDATE_ONSCREEN_PERIOD, 30))*1000;
		ParsTeams teams = new ParsTeams(this);
		teams.execute();
		if(isActiveTimer){
			timer.schedule(rt, activeTimerTime, activeTimerTime);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		mViewPager.setCurrentItem(1);
		// The activity is about to become visible.
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		mViewPager.setCurrentItem(1);
		refresh();
		if(isActiveTimer){
			rt = new RefreshTimer();
			timer = new Timer();
			timer.schedule(rt, activeTimerTime, activeTimerTime);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// The activity has become visible (it is now "resumed").
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Another activity is taking focus (this activity is about to be
		// "paused").
	}

	@Override
	protected void onStop() {
		super.onStop();
		if(timer != null)
			timer.cancel();
		timer = null;
		rt = null;
		// The activity is no longer visible (it is now "stopped")
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.close:
			new CloseDialogFragment().show(getFragmentManager(), "CloseDialog");
			break;
		case R.id.f5:
			if(timer != null)
			timer.cancel();
			timer = null;
			rt = null;
			refresh();
			rt = new RefreshTimer();
			timer = new Timer();
			timer.schedule(rt, activeTimerTime, activeTimerTime);
			break;
		case R.id.settings:
			Intent settings = new Intent(this, TickerSettingsActivity.class);
			startActivity(settings);
		}

		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment;
			switch (position) {
			case 0:
				fragment = new GoalsFragment();
				return fragment;
			case 1:
				fragment = new TickerFragment();
				return fragment;
			case 2:
				fragment = new PenaltiesFragment();
				return fragment;
			default:
				return null;
			}
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}
	
	class RefreshTimer extends TimerTask {
		@Override
		public void run() {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					refresh();
				}
			});
		}
	}

	public void teamName(View v) {
		switch (v.getId()) {
		case (R.id.imageViewGuest):
			Toast.makeText(this, guest, Toast.LENGTH_SHORT).show();
			break;
		case (R.id.imageViewHome):
			Toast.makeText(this, home, Toast.LENGTH_SHORT).show();
			break;
		}

	}

	public void refresh() {
		int index = mViewPager.getCurrentItem();
		if (index == 0) {
			setTeamLogo();
			GoalsFragment.refresh();
			TickerFragment.refresh();
		}
		else if (index == 1) {
			setTeamLogo();
			GoalsFragment.refresh();
			TickerFragment.refresh();
			PenaltiesFragment.refresh();
		}
		else if (index == 2) {
			setTeamLogo();
			TickerFragment.refresh();
			PenaltiesFragment.refresh();
		}
	}

	private void setTeamLogo() {
		this.findViewById(R.id.progressGuest).setVisibility(View.VISIBLE);
		this.findViewById(R.id.progressHome).setVisibility(View.VISIBLE);
		Toast.makeText(this, "Aktuallisiert", Toast.LENGTH_SHORT).show();
		DownloadImage setHomeImage = new DownloadImage(0, (ImageView) this.findViewById(R.id.imageViewHome), this.findViewById(R.id.progressHome)); // Load
																																					// Home
																																					// Logo
		DownloadImage setGuestImage = new DownloadImage(1, (ImageView) this.findViewById(R.id.imageViewGuest), this.findViewById(R.id.progressGuest)); // Load
																																						// Guest
																																						// Logo
		setHomeImage.execute();
		setGuestImage.execute();
	}
	
}
