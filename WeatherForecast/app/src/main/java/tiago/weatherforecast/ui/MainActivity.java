package tiago.weatherforecast.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tiago.weatherforecast.R;
import tiago.weatherforecast.repo.TaskFlushDB;
import tiago.weatherforecast.repo.data.Forecast;
import tiago.weatherforecast.repo.AppDatabase;
import tiago.weatherforecast.repo.data.Weather;

/**
 * This is the Main activity, its responsible inflating the fragments it uses and injecting the
 * shared ViewModel into the fragments so that they can be updated internally. It also instantiates
 * the database reference with the necessary context.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main";

    private RecyclerView recyclerView;

    private TimestampFragment timestampFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Shared View Model
        MainViewModel vm = ViewModelProviders.of(this).get(MainViewModel.class);


        SubmitFragment submitFragment = SubmitFragment.newInstance(0.00,0.00);
        submitFragment.injectMainViewModel(this, vm);
        getSupportFragmentManager().beginTransaction().replace(R.id.submitFragment, submitFragment).commit();

        timestampFragment = TimestampFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.timestampFrag, timestampFragment).commit();

        setupOnWeatherForecastChange(vm, this);

        // RecycleView used by the adapter
        recyclerView = findViewById(R.id.mainRecycleView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Create Database
        AppDatabase.buildDatabase(this);
    }

    /**
     * Updates the context with the current weather items pulled from the ViewModel.
     * @param vm MainViewModel
     * @param context Current context
     */

    private void setupOnWeatherForecastChange(MainViewModel vm, final Context context) {
        vm.weatherForecast.observe(this, new Observer<Forecast>() {
            @Override
            public void onChanged(@Nullable Forecast forecast) {
                List<Weather> list;
                if (forecast == null) {
                    Toast.makeText(context, "No forecast found", Toast.LENGTH_LONG).show();
                   list = new ArrayList<Weather>();
                    timestampFragment.setTimeStamp(" ");
                } else {
                    list = forecast.items;
                    timestampFragment.setTimeStamp(forecast.date + "  " +
                            forecast.time);
                }
                RecyclerView.Adapter adapter = new WeatherAdapter(list, context);
                recyclerView.setAdapter(new WeatherAdapter(list, context));
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void dropDb() {
        Toast.makeText(this, "Database Flushed", Toast.LENGTH_LONG).show();
        TaskFlushDB asyncTask = (TaskFlushDB) new TaskFlushDB();
        asyncTask.execute();
        clearAdapter();
    }

    private void clearAdapter() {
        List<Weather> list = new ArrayList<Weather>();
        recyclerView.setAdapter(new WeatherAdapter(list, this));
        timestampFragment.setTimeStamp(" ");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drop_db:
                dropDb();
                return true;
            case R.id.clear:
                clearAdapter();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
