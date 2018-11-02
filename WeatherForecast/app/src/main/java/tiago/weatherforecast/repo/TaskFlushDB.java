package tiago.weatherforecast.repo;

import android.os.AsyncTask;
import android.util.Log;

import tiago.weatherforecast.repo.data.AppDatabase;
import tiago.weatherforecast.repo.data.Forecast;
import tiago.weatherforecast.repo.data.ForecastDao;
import tiago.weatherforecast.repo.data.WeatherDao;

public class TaskFlushDB extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "FlushDB";

    @Override
    protected Void doInBackground(Void... voids) {
        Log.w(TAG, "Flushing..");
        AppDatabase db = AppDatabase.getInstance();
        ForecastDao fDao = db.forecastDao();
        fDao.dropAll();
        WeatherDao wDao = db.weatherDao();
        wDao.dropAll();
        return null;
    }

}
