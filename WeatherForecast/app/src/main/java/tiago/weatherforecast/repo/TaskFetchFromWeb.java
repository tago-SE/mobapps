package tiago.weatherforecast.repo;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import tiago.weatherforecast.repo.data.AppDatabase;
import tiago.weatherforecast.repo.data.Forecast;
import tiago.weatherforecast.repo.data.ForecastDao;
import tiago.weatherforecast.repo.data.ForecastEntity;
import tiago.weatherforecast.repo.data.Weather;
import tiago.weatherforecast.repo.data.WeatherDao;
import tiago.weatherforecast.repo.data.WeatherEntity;


public abstract class TaskFetchFromWeb extends AsyncTask<Void, Void, Forecast> {

    private final String TAG = "WeatherAsyncTask";

    private final double longitude;
    private final double latitude;

    public TaskFetchFromWeb(double longitude, double latitude) {
        super();
        this.longitude = longitude;
        this.latitude = latitude;

    }

    @Override
    protected Forecast doInBackground(Void... voids) {
        Log.w(TAG, "WeatherAsyncTask:started");

        //String sURL = "https://maceo.sth.kth.se/api/category/pmp3g/version/2/geotype/point/lon/14.333/lat/60.383/";
        //String sURL = "https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/14.333/lat/60.383/data.json";
        String sLong = "" + longitude;
        String sLat = "" + latitude;
        Log.w(TAG, "Searching" + sLong + ", " + sLat);
        String sURL = "https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/" + sLong + "/lat/" + sLat + "/data.json";

        Forecast forecast = null;
        try {
            URL url = new URL(sURL);
            URLConnection request = url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            JsonObject rootObj = root.getAsJsonObject();

            JsonArray coordinates = (JsonArray) rootObj.getAsJsonObject("geometry")
                    .getAsJsonArray("coordinates")
                    .get(0);

            String approved = rootObj.getAsJsonPrimitive("approvedTime").toString();
            String reference = rootObj.getAsJsonPrimitive("referenceTime").toString();
            String approvedDate = approved.substring(1,11);
            String approvedTime = approved.substring(12, 17);
            //double longitude = coordinates.get(0).getAsFloat();
            //double latitude = coordinates.get(1).getAsFloat();

            forecast = new Forecast(
                    Double.parseDouble(sLong), Double.parseDouble(sLat), approvedDate, approvedTime);

            for (Object o : rootObj.getAsJsonArray("timeSeries")) {
                JsonObject node = (JsonObject) o;
                String validTime = node.getAsJsonPrimitive("validTime").toString();

                // Is this able to handle calender days < 10?
                String date = validTime.substring(1, 11);
                String time = validTime.substring(12, 17);

                JsonArray parameters = node.getAsJsonArray("parameters");
                float temperature = ((JsonObject) parameters.get(11)).getAsJsonArray("values").getAsFloat();
                int sky = ((JsonObject) parameters.get(18)).getAsJsonArray("values").getAsInt();

                Weather weather = new Weather(date, time, sky, temperature);
                Log.w(TAG, "weather: " + weather);
                forecast.items.add(weather);
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        saveForecast(forecast);

        return forecast;
    }

    /**
     * Inserts forecast as DB entity, purging all previous forecasts
     * @param forecast
     */
    private void saveForecast(Forecast forecast) {
        if (forecast == null)
            return;
        AppDatabase db = AppDatabase.getInstance();
        try {
            ForecastDao fDao = db.forecastDao();
            WeatherDao wDao = db.weatherDao();

            ForecastEntity foundEntity = fDao.findByCoordinates(longitude, latitude);
            if (foundEntity != null) {
                Log.w(TAG, "Deleted previous forecast");
                wDao.deleteMatchingForecast(foundEntity.getId());
                fDao.delete(foundEntity);
            }
            ForecastEntity fEnt = new ForecastEntity(forecast.date, forecast.time, forecast.longitude, forecast.latitude);
            Log.w(TAG, "Saving: " + fEnt.toString());
            fDao.insertAll(fEnt);
            for (Weather w : forecast.items)
                wDao.insertAll(new WeatherEntity(fEnt.getId(), w.date, w.time, w.sky, w.temperature));
        } catch (Exception e) {
            Log.e(TAG, "DB error:" + e.getMessage());
        }
    }


    protected abstract void onPostExecute(Forecast forecast);
}