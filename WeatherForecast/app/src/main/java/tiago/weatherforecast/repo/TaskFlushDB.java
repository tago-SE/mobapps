package tiago.weatherforecast.repo;

import android.os.AsyncTask;
import android.util.Log;

import tiago.weatherforecast.repo.data.ForecastDao;
import tiago.weatherforecast.repo.data.WeatherDao;

/**
 * Async Task used to flush the cache (db) that the app uses.
 */
public class TaskFlushDB extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "FlushDB";

    /**
     * Flushes the database.
     * @param voids nothing
     * @return nothing
     */
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
