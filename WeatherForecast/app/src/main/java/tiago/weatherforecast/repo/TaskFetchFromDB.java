package tiago.weatherforecast.repo;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import tiago.weatherforecast.repo.data.Forecast;
import tiago.weatherforecast.repo.data.ForecastDao;
import tiago.weatherforecast.repo.data.ForecastEntity;
import tiago.weatherforecast.repo.data.Weather;
import tiago.weatherforecast.repo.data.WeatherDao;
import tiago.weatherforecast.repo.data.WeatherEntity;
/**
 * Queries the application cache (db) for previously gathered weather data (which may or may not be
 * out of date) and returns it as a Forecast model. On failure it will return null.
 * Note that arguments are passed inside the constructor before execution of the AsynkTask.
 */
public abstract class TaskFetchFromDB extends AsyncTask<Void, Void, Forecast> {

    private static final String TAG = "TaskFromDB";
    private final double longitude;
    private final double latitude;

    public TaskFetchFromDB(double longitude, double latitude) {
        super();
        this.longitude = longitude;
        this.latitude = latitude;

    }
    /**
     * Executes a database query based on the constructor provided longitude and latitude data.
     * If nothing is found or no connection was established it will return null.
     * @param voids Arguments are passed inside the constructor parameter instead.
     * @return a Forecast model or null if nothing is found.
     */
    @Override
    protected Forecast doInBackground(Void... voids) {
        List<Forecast> forecasts = DBHandler.fetchAllForecasts(longitude, latitude);
        if (forecasts.size() == 0)
            return null;
        // By default it currently only retains the latest forecast
        Forecast forecast = forecasts.get(0);
        return forecast;
    }

    /**
     * Abstarct method created to handle the retrieved data.
     * @param forecast Forecast model
     */
    protected abstract void onPostExecute(Forecast forecast);


}
