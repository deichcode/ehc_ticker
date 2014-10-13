package de.deichcode.ehc_neuwiedticker;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class TickerSettingsActivity extends PreferenceActivity {
	public static final String PREFS_KEY_AUTO_UPDATE_ONSCREEN = "pref_key_auto_update_onscreen";
	public static final String PREFS_KEY_AUTO_UPDATE_ONSCREEN_PERIOD = "pref_key_auto_update_onscreen_period";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
		getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
	}
		
	public static class SettingsFragment extends PreferenceFragment  implements OnSharedPreferenceChangeListener {
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        // Load the preferences from an XML resource
	        addPreferencesFromResource(R.xml.activity_settings);
	        
	        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	        
	        Preference findPreference = findPreference(PREFS_KEY_AUTO_UPDATE_ONSCREEN_PERIOD);
			String summary = "Aktualisierung alle %d Sekunden";
			findPreference.setSummary(String.format(summary, getPreferenceScreen().getSharedPreferences().getInt(PREFS_KEY_AUTO_UPDATE_ONSCREEN_PERIOD, 30)));
	    }
	    
	    @Override
	    public void onPause() {
			super.onPause();
			getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
		}
	    
	    @Override
	    public void onResume() {
			super.onResume();
			getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		}
	    
		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			if(key.equals(PREFS_KEY_AUTO_UPDATE_ONSCREEN_PERIOD)){
				Preference findPreference = findPreference(key);
				String summary = "Aktualisierung alle %d Sekunden";
				findPreference.setSummary(String.format(summary, sharedPreferences.getInt(key, 30)));
			}
			
		}
	}
}
