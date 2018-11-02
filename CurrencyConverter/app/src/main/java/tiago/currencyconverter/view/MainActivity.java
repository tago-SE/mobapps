package tiago.currencyconverter.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import tiago.currencyconverter.R;
import tiago.currencyconverter.model.CurrencyDomain;
import tiago.currencyconverter.utility.NetworkUtil;
import tiago.currencyconverter.utility.SnackbarUtil;
import tiago.currencyconverter.view.CurrencyFragment;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "Main";

    private CurrencyDomain currencyDomain = CurrencyDomain.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /* Removed
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
        CurrencyFragment currencyFrag1 = (CurrencyFragment) getSupportFragmentManager().findFragmentById(R.id.main_c_frag1);
        CurrencyFragment currencyFrag2 = (CurrencyFragment) getSupportFragmentManager().findFragmentById(R.id.main_c_frag2);
    }

    /**
     * Inflate the menu; this adds items to the action bar if it is present.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh_rates:
                handleRefreshRates();
                return true;
            case R.id.action_settings:
                Log.w(LOG_TAG, "settings");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleRefreshRates() {
        Log.w(LOG_TAG, "refreshRates");
        if (NetworkUtil.CheckInternetConnection(this)) {
            Log.w(LOG_TAG, "isConnected");
            SnackbarUtil.makeSimpleActionSnackbar(findViewById(R.id.main_c_snackbar), R.string.refreshing_rates);

            currencyDomain.refreshRates();

            //PullCurrenciesTask task = new PullCurrenciesTask();



            //task.execute(null, null, null);
        } else {
            Log.w(LOG_TAG, "Not connected");
            SnackbarUtil.makeSimpleActionSnackbar(findViewById(R.id.main_c_snackbar), R.string.no_connection);
        }
    }


}
