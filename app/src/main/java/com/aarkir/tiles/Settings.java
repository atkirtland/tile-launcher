package com.aarkir.tiles;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class Settings extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Call super :
        super.onCreate(savedInstanceState);

        // Set the activity's fragment :
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }


    public static class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

        private SeekBarPreference columnDivisionsPref;
        private SeekBarPreference columnsPref;
        private SeekBarPreference largestSizePref;
        //private SeekBarPreference marginPref;
        private Preference resetFrequencies;

        @Override
        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);

            // Get widgets :
            columnDivisionsPref = (SeekBarPreference) this.findPreference("columnDivisions");
            columnsPref = (SeekBarPreference) this.findPreference("columns");
            largestSizePref = (SeekBarPreference) this.findPreference("largestSize");
            //marginPref = (SeekBarPreference) this.findPreference("margin");
            //backgroundsPref = (CheckBoxPreference) this.findPreference("backgrounds");

            resetFrequencies = (Preference) this.findPreference("resetFrequencies");
            resetFrequencies.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AppLauncher.resetFrequencies();
                    return false;
                }
            });

            // Set listener :
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

            // Set seekbar summary :
            setSummary("columns", columnsPref, R.string.columns_summary);
            setSummary("columnDivisions", columnDivisionsPref, R.string.columnDivisions_summary);
            setSummary("largestSize", largestSizePref, R.string.largestSize_summary);
            //setSummary("margin", marginPref, R.string.margin_summary);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            //SharedPreferences.Editor editor = sharedPreferences.edit();
            switch (key) {
                case "columns":
                    //change the main activity's value
                    AppLauncher.setColumnCount(sharedPreferences.getInt("columns", 6));
                    //put the new values
                    //editor.putString("columns", AppLauncher.myValue); //where ever or whatever new value is
                    setSummary("columns", columnsPref, R.string.columns_summary);
                    break;
                case "columnDivisions":
                    AppLauncher.setMaximumApps(sharedPreferences.getInt("columnDivisions", 1));
                    setSummary("columnDivisions", columnDivisionsPref, R.string.columnDivisions_summary);
                    break;
                case "largestSize":
                    AppLauncher.setLargestSize(sharedPreferences.getInt("largestSize", 50));
                    setSummary("largestSize", largestSizePref, R.string.largestSize_summary);
                    break;
                /*
                case "margin":
                    AppLauncher.setMargin(sharedPreferences.getInt("margin", 10));
                    setSummary("margin", marginPref, R.string.margin_summary);
                    break;
                    */
                case "backgrounds":
                    AppLauncher.setBackgrounds(sharedPreferences.getBoolean("backgrounds", false));
                    break;
                case "sizeSort":
                    AppLauncher.setSizeSort(sharedPreferences.getBoolean("sizeSort", false));
                    break;
            }
            //editor.commit();
        }

        //set the summary under the preference showing the current value
        private void setSummary(String key, SeekBarPreference seekBarPreference, int summaryID) {
            int value = PreferenceManager.getDefaultSharedPreferences(this.getActivity()).getInt(key, 5);
            seekBarPreference.setSummary(this.getString(summaryID).replace("$1", "" + value));
        }
    }
}
