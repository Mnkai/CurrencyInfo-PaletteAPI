package moe.minori.currencyinfo;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingFragment()).commit();
    }


    public static class SettingFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.pref_general);

            findPreference("enabledCheckBox").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    if ((boolean) newValue) {
                        // plugin enabled

                        getActivity().startService(new Intent(getActivity(), CurrencyLookupService.class));

                    } else {
                        // plugin disabled

                        getActivity().stopService(new Intent(getActivity(), CurrencyLookupService.class));
                    }
                    return true;
                }
            });

        }
    }

}



