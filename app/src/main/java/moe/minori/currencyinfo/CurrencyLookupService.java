package moe.minori.currencyinfo;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by minori on 15. 8. 19.
 */
public class CurrencyLookupService extends Service {

    BroadcastReceiver receiver;
    BroadcastReceiver myInfoReceiver;

    SharedPreferences preferences;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("CurrencyLookupService", "Started Service");

        CurrencyModule.register(getApplicationContext());
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // register listener

        IntentFilter filter = new IntentFilter();
        filter.addAction(Consts.NOTIFY_NEW_STATUS);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.d("CurrencyLookupService", "Received status");

                String FROMTEXT = intent.getStringExtra("FROMTEXT");
                final String FROMTEXTSCREEN = intent.getStringExtra("FROMTEXTSCREEN");
                final long FROMLONG = intent.getLongExtra("FROMLONG", -1);
                final String TEXT = intent.getStringExtra("TEXT");
                final long TEXTLONG = intent.getLongExtra("TEXTLONG", -1);
                long REPLY_TO = intent.getLongExtra("REPLY_TO", -1);

                if (preferences.getBoolean("userLimitCheckBox", true)) {
                    // user limit on

                    // find out if this tweet is mine
                    IntentFilter myInfoFilter = new IntentFilter(Consts.MYINFO_RESULT);

                    myInfoReceiver = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {

                            unregisterReceiver(this);

                            Log.d("UUID", intent.getLongExtra("UUID", -1) + "");
                            if (intent.getLongExtra("UUID", -1) == FROMLONG)
                                processor(TEXT, TEXTLONG, FROMTEXTSCREEN);
                        }
                    };

                    registerReceiver(myInfoReceiver, myInfoFilter, Consts.EXTERNAL_BROADCAST_API, null);

                    sendBroadcast(new Intent(Consts.MYINFO_QUERY), Consts.EXTERNAL_BROADCAST_API);
                } else {
                    // user limit off

                    processor(TEXT, TEXTLONG, FROMTEXTSCREEN);
                }
            }
        };

        registerReceiver(receiver, filter, Consts.EXTERNAL_BROADCAST_API, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("CurrencyLookupService", "Stopped Service");

        CurrencyModule.unRegister();
        if (receiver != null)
            unregisterReceiver(receiver);
        if ( myInfoReceiver != null )
            unregisterReceiver(myInfoReceiver);
    }

    private String preprocessor(String input) {
        Log.d("Preprocessor", "Started");
        if (input.startsWith(preferences.getString("keywordToActivate", String.valueOf(R.string.keywordValue)))) {
            String[] array = input.split(" ");

            return array[1];
        } else {
            return null;
        }
    }

    private void processor(String TEXT, long TEXTLONG, String FROMTEXTSCREEN) {
        Log.d("Processor", "Started");
        String processResult = preprocessor(TEXT);
        if (processResult != null) {
            double currency = CurrencyModule.getCurrency(processResult);

            if (currency == -1) {
                StringBuilder responseBuilder = new StringBuilder();

                responseBuilder.append("@");
                responseBuilder.append(FROMTEXTSCREEN);
                responseBuilder.append(" ");
                responseBuilder.append("Unknown error");

                sendBroadcast(new Intent(Consts.REQ_WRITE_TWEET)
                                .putExtra("DATA", responseBuilder.toString())
                                .putExtra("REPLY_TO", TEXTLONG),
                        Consts.EXTERNAL_BROADCAST_API);
            } else {
                StringBuilder responseBuilder = new StringBuilder();

                responseBuilder.append("@");
                responseBuilder.append(FROMTEXTSCREEN);
                responseBuilder.append(" ");
                responseBuilder.append(processResult);
                responseBuilder.append(" -> ");
                responseBuilder.append(getResources().getString(R.string.mainCurrencyValue));
                responseBuilder.append("\n");
                responseBuilder.append(currency);

                sendBroadcast(new Intent(Consts.REQ_WRITE_TWEET)
                                .putExtra("DATA", responseBuilder.toString())
                                .putExtra("REPLY_TO", TEXTLONG),
                        Consts.EXTERNAL_BROADCAST_API);
            }
        }
    }
}
