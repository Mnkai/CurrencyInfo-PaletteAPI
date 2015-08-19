package moe.minori.currencyinfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

/**
 * Created by minori on 15. 8. 19.
 */
public class CurrencyModule {

    static Context context = null;

    public static Double getCurrency (String input)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        final StringBuilder urlBuilder = new StringBuilder();

        urlBuilder.append("http://www.webservicex.net/CurrencyConvertor.asmx/ConversionRate?FromCurrency=");
        urlBuilder.append(input);
        urlBuilder.append("&ToCurrency=");
        urlBuilder.append(preferences.getString("mainCurrency", String.valueOf(R.string.mainCurrencyValue)));

        AsyncTask<Void, Void, Double> task = new AsyncTask<Void, Void, Double>() {
            @Override
            protected Double doInBackground(Void... params) {

                URLConnection tempURLConnection = null;
                try {
                    tempURLConnection = new URL(urlBuilder.toString()).openConnection();
                    tempURLConnection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");

                    BufferedReader in = new BufferedReader(new InputStreamReader(tempURLConnection.getInputStream()));

                    String tempLine;

                    StringBuilder responseBuilder = new StringBuilder();

                    while ( (tempLine = in.readLine()) != null )
                    {
                        responseBuilder.append(tempLine);
                    }

                    String response = responseBuilder.toString();

                    response = response.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?><double xmlns=\"http://www.webserviceX.NET/\">", "");
                    response = response.replace("</double>", "");

                    return Double.parseDouble(response);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return (double) -1;
            }
        };

        try {
            return task.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return (double) -1;
        }
    }

    public static void register(Context applicationContext) {
        context = applicationContext;
    }

    public static void unRegister() {
        context = null;
    }
}
