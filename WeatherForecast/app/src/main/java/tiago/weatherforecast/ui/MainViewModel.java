package tiago.weatherforecast.ui;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import tiago.weatherforecast.repo.TaskFetchFromDB;
import tiago.weatherforecast.repo.TaskFetchLast;
import tiago.weatherforecast.repo.data.Forecast;
import tiago.weatherforecast.repo.TaskFetchFromWeb;
import tiago.weatherforecast.repo.data.Weather;

/**
 * This is a view model used to share data between the MainActivity and the fragments
 * it uses. It contains an action: doSubmit to update the view with fetched data from
 * either the web or the database if no connection is established.
 */
public class MainViewModel extends ViewModel {

    private static final String TAG = "ViewModel";

    /* LiveData for updating the views when a new forecast or weather list is posted */
    public final MutableLiveData<Forecast> weatherForecast = new MutableLiveData<>();
    public final MutableLiveData<List<Weather>> weatherList = new MutableLiveData<>();

    public MainViewModel() {
    }

    /**
     * Pulls forecasts either from the web or if no connection is detected it searches the database
     * for previous entries. Database data might be outdated or not found, if the coordinates don't
     * exist.
     * @param context
     * @param longitude
     * @param latitude
     */
    public void doSubmit(final Context context, double longitude, double latitude) {
        if (isConnected(context)) {
            Log.e(TAG, "connected");
            fetchFromWeb(longitude, latitude);
        } else {
            Log.e(TAG, "not connected");
            Toast.makeText(context, "Offline: Data might be out of date.",
                    Toast.LENGTH_LONG).show();
            fetchFromDB(longitude, latitude);
        }
    }

    public void onStart(final Context context) {
        new TaskFetchLast() {
            @Override
            protected void onPostExecute(Forecast forecast) {
                Toast.makeText(context, "Refreshing...", Toast.LENGTH_LONG).show();
                SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd hh:mm");
                try {
                    Date oldDate = dt.parse(forecast.date + " " + forecast.time);
                    Date newDate = dt.parse(new SimpleDateFormat("yyyy-mm-dd hh:mm").format(new Date()));
                    Log.w(TAG, oldDate.toString() + " " + newDate.toString());
                    long diff = newDate.getTime() - oldDate.getTime();
                    String s = "time:\n" + newDate.getTime() + "\n" + oldDate.getTime() + "\n" +
                            diff;
                    Log.w(TAG, s);
                    if (diff >= 1000*60*60*2 && isConnected(context)) {

                        new TaskFetchFromWeb(forecast.longitude, forecast.latitude) {
                            @Override
                            protected void onPostExecute(Forecast forecast) {
                                if (forecast != null) {
                                    publishForecast(forecast);
                                    Toast.makeText(context, "New data...", Toast.LENGTH_LONG).show();
                                }
                            }
                        }.execute();
                    } else {
                        Toast.makeText(context, "Local data, may be out of date...", Toast.LENGTH_LONG).show();
                        publishForecast(forecast);
                    }
                    Log.w(TAG, oldDate.toString() + " " + newDate.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void publishForecast(Forecast forecast) {
        if (forecast == null) {
            Log.e(TAG, "No forecast found");
            weatherList.postValue(null);
            weatherForecast.postValue(null);
        } else {
            weatherList.postValue(forecast.items);
            weatherForecast.postValue(forecast);
        }
    }

    private boolean isConnected(final Context context) {
        ConnectivityManager netManager = (ConnectivityManager) context.
                getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = netManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    private void fetchFromWeb(double longitude, double latitude) {
        Log.w(TAG, "fetching from web...");
        TaskFetchFromWeb asyncTask = (TaskFetchFromWeb) new TaskFetchFromWeb(longitude, latitude) {
            @Override
            protected void onPostExecute(Forecast forecast) {
                publishForecast(forecast);
            }
        }.execute();
    }

    private void fetchFromDB(double longitude, double latitude) {
        Log.w(TAG, "fetching from db...");
        TaskFetchFromDB asyncTask = (TaskFetchFromDB) new TaskFetchFromDB(longitude, latitude) {
            @Override
            protected void onPostExecute(Forecast forecast) {
                publishForecast(forecast);
            }
        }.execute();
    }



}
