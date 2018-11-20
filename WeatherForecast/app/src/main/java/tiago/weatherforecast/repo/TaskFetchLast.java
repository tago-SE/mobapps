package tiago.weatherforecast.repo;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import tiago.weatherforecast.repo.data.Forecast;

public abstract class TaskFetchLast extends AsyncTask<Void, Void, Forecast> {

    private static final String TAG = "UpdateOnStart";

    @Override
    protected Forecast doInBackground(Void... voids) {
        return DBHandler.fetchLastForecast();
    }

    protected abstract void onPostExecute(Forecast forecast);
}
