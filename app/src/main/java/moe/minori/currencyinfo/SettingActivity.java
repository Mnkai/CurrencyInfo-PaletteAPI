package moe.minori.currencyinfo;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class SettingActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_general);

        findPreference("enabledCheckBox").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                if ((boolean) newValue) {
                    // plugin enabled

                    startService(new Intent(SettingActivity.this, CurrencyLookupService.class));

                } else {
                    // plugin disabled

                    stopService(new Intent(SettingActivity.this, CurrencyLookupService.class));
                }
                return true;
            }
        });

    }
}
