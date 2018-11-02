package tiago.lab1;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.io.IOException;
import java.util.ArrayList;

import tiago.lab1.model.URLHandler;
import tiago.lab1.model.XMLParseTask;

public class MainActivity extends AppCompatActivity {

    private Spinner fromSpinner;
    private ArrayAdapter<String> fromAdapter;

    private static final int NUM_CURRENCY = 2;
    private LinearLayout layout;
    private CurrencySelectorFragment[] currencyFrag;

    private static final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        Log.w(LOG_TAG, "onCreate");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //RelativeLayout containerCurrencyConverter1 = findViewById(R.id.main_cc_container1);
        Fragment ccFrag1 = CurrencySelectorFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_cc_container1,ccFrag1).commit();

        RelativeLayout containerCurrencyConverter2 = findViewById(R.id.main_cc_container2);




        /*
        fromSpinner = findViewById(R.id.spinner_from);

        ArrayList<String> list = new ArrayList<>();
        list.add("Stockholm");
        list.add("Enköping");
        fromAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, list);
        fromSpinner.setAdapter(fromAdapter);
        fromSpinner.setVisibility(View.VISIBLE);
        */


    }

    /*
    private void setupCurrencyFragments() {
        layout = findViewById(R.id.drawer_layout_ll);
        int[] containerId = new int[NUM_CURRENCY];
        containerId[0] = R.id.drawer_layout_container1;
        containerId[1] = R.id.drawer_layout_container2;
        currencyFrag = new CurrencySelectorFragment[NUM_CURRENCY];
        for (int i = 0; i < NUM_CURRENCY; i++) {
            currencyFrag[i] = new CurrencySelectorFragment();
            getSupportFragmentManager().beginTransaction().replace(containerId[i], currencyFrag[i]).commit();
        }

    }
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            XMLParseTask parseXML = new XMLParseTask();
            parseXML.execute(null, null, null);

            /*
            URLHandler urlHandler = new URLHandler();
            try {
                urlHandler.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            */

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
