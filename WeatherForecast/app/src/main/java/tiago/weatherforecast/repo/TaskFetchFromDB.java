package tiago.weatherforecast.repo;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import tiago.weatherforecast.repo.data.AppDatabase;
import tiago.weatherforecast.repo.data.Forecast;
import tiago.weatherforecast.repo.data.ForecastDao;
import tiago.weatherforecast.repo.data.ForecastEntity;
import tiago.weatherforecast.repo.data.Weather;
import tiago.weatherforecast.repo.data.WeatherDao;
import tiago.weatherforecast.repo.data.WeatherEntity;

public abstract class TaskFetchFromDB extends AsyncTask<Void, Void, Forecast> {

    private static final String TAG = "TaskFromDB";
    private final double longitude;
    private final double latitude;

    public TaskFetchFromDB(double longitude, double latitude) {
        super();
        this.longitude = longitude;
        this.latitude = latitude;

    }

    @Override
    protected Forecast doInBackground(Void... voids) {
        List<Forecast> forecasts = fetchAllForecasts(longitude, latitude);
        if (forecasts.size() == 0)
            return null;
        // By default it currently only retains the latest forecast
        Forecast forecast = forecasts.get(0);
        return forecast;
    }

    // findByCoordinates


    private List<Forecast> fetchAllForecasts(double longitude, double latitude) {
        AppDatabase db = AppDatabase.getInstance();
        ForecastDao fDao = db.forecastDao();
        List<ForecastEntity> entities = new ArrayList<>();

        ForecastEntity found = fDao.findByCoordinates(longitude, latitude);
        List<Forecast> forecasts = new ArrayList<>();
        if (found == null)
            return forecasts;
        entities.add(found);
        for (ForecastEntity fEnt: entities) {
            Forecast forecast = new Forecast(fEnt.getLongitude(), fEnt.getLatitude(), fEnt.getDate(), fEnt.getTime());
            forecasts.add(forecast);
            forecast.items = fetchForecastedWeather(fEnt);
            Log.w(TAG, forecast.toString());
        }
        return forecasts;
    }

    private List<Weather> fetchForecastedWeather(ForecastEntity fEnt) {
        AppDatabase db = AppDatabase.getInstance();
        WeatherDao wDao = db.weatherDao();
        List<Weather> result = new ArrayList<>();

        Log.e(TAG, "FORECAST KEY: " + fEnt.getId());
        for (WeatherEntity wEnt: wDao.loadAllByIds(fEnt.getId())) {
            Log.w(TAG, "Found: " + wEnt);

            Weather weather = new Weather(wEnt.getDate(), wEnt.getTime(), wEnt.getSky(), (float) wEnt.getTemperature());
            result.add(weather);
        }
        return result;
    }

    protected abstract void onPostExecute(Forecast forecast);


}
