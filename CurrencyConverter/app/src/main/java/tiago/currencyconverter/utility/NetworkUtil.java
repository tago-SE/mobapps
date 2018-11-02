package tiago.currencyconverter.utility;

import android.content.Context;
import android.net.ConnectivityManager;

import android.net.NetworkInfo;

public class NetworkUtil {
    public static boolean CheckInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isAvailable() && netInfo.isConnected();
    }
}
