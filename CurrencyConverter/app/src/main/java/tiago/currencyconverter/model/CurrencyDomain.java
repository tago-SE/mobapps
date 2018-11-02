package tiago.currencyconverter.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CurrencyDomain {
    private static final CurrencyDomain ourInstance = new CurrencyDomain();

    public static CurrencyDomain getInstance() {
        return ourInstance;
    }

    private static final String LOG_TAG = "CurrencyDomain";

    private List<Currency> listCurrencies = new ArrayList<>();
    private String date;


    private CurrencyDomain() {
    }




    public void refreshRates() {
        LoadCurrencies asyncTask = (LoadCurrencies) new LoadCurrencies() {
            @Override
            protected void onPostExecute(CurrenciesPackage currencyPackage) {
                Log.w(LOG_TAG, "onPostExecute");
                if (currencyPackage == null) {
                    return; // Error
                }
                for (Currency c : currencyPackage.currencies) {
                    Log.d(LOG_TAG, c.toString());
                }
            }
        }.execute();
    }
}
