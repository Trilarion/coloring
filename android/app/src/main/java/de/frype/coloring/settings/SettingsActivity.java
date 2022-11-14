package de.frype.coloring.settings;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import de.frype.coloring.R;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */ // TODO this is deprecated use the new preferred way (https://developer.android.com/reference/android/preference/PreferenceActivity)
public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // minSdkVersion 10
        //noinspection deprecation
        addPreferencesFromResource(R.xml.settings);
    }
}
