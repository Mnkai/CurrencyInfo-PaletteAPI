package moe.minori.currencyinfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;

/**
 * Created by minori on 15. 8. 19.
 */
public class BootListener extends BroadcastReceiver {

    public final static String ANDROID_ON = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        // BOOT_COMPLETED” start Service
        if (intent.getAction().equals(ANDROID_ON)) {

            // get preference
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

            boolean isEnabled = preferences.getBoolean("enabledCheckBox", false);

            Intent serviceIntent = new Intent(context, CurrencyLookupService.class);

            //Service
            if ( isEnabled )
            {

                context.startService(serviceIntent);
            }
            else
            {
                context.stopService(serviceIntent);
            }

        }
    }
}
