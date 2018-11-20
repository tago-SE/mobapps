package tiago.weatherforecast.repo;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import tiago.weatherforecast.repo.data.Forecast;
import tiago.weatherforecast.repo.data.ForecastDao;
import tiago.weatherforecast.repo.data.ForecastEntity;
import tiago.weatherforecast.repo.data.Weather;
import tiago.weatherforecast.repo.data.WeatherDao;
import tiago.weatherforecast.repo.data.WeatherEntity;

public class DBHandler {

    private static final String TAG = "DBHandler";

    public static List<Weather> fetchForecastedWeather(ForecastEntity fEnt) {
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

    public static List<Forecast> fetchAllForecasts(double longitude, double latitude) {
        AppDatabase db = AppDatabase.getInstance();
        ForecastDao fDao = db.forecastDao();
        List<ForecastEntity> entities = new ArrayList<>();
        ForecastEntity found = fDao.findByCoordinates(longitude, latitude);
        List<Forecast> forecasts = new ArrayList<>();
        if (found == null)
            return forecasts;
        entities.add(found);
        for (ForecastEntity fEnt: entities) {
            forecasts.add(toModel(fEnt));
        }
        return forecasts;
    }

    public static Forecast toModel(ForecastEntity e) {
        Forecast forecast = new Forecast(e.getLongitude(), e.getLatitude(), e.getDate(), e.getTime());
        forecast.items = DBHandler.fetchForecastedWeather(e);
        return forecast;
    }

    public static Forecast fetchLastForecast() {
        AppDatabase db = AppDatabase.getInstance();
        ForecastDao fDao = db.forecastDao();
        ForecastEntity forecastEntity = fDao.getLast();
        if (forecastEntity == null)
            return null;
        return toModel(forecastEntity);
    }

}
