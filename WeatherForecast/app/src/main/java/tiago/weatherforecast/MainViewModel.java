package tiago.weatherforecast;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import tiago.weatherforecast.repo.TaskFetchFromDB;
import tiago.weatherforecast.repo.data.Forecast;
import tiago.weatherforecast.repo.TaskFetchFromWeb;
import tiago.weatherforecast.repo.data.Weather;

public class MainViewModel extends ViewModel {

    private static final String TAG = "ViewModel";


    public final MutableLiveData<Forecast> weatherForecast = new MutableLiveData<>();
    public final MutableLiveData<List<Weather>> weatherList = new MutableLiveData<>();




    public MainViewModel() {
    }

    private boolean isConnected(final Context context) {
        ConnectivityManager netManager = (ConnectivityManager) context.
                getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = netManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public void doSubmit(final Context context, double longitude, double latitude) {
        if (isConnected(context)) {
            Log.e(TAG, "connected");
            fetchFromWeb(longitude, latitude);
        } else {
            Log.e(TAG, "not connected");
            Toast.makeText(context, "Offline: Data might be out of date.", Toast.LENGTH_LONG).show();
            fetchFromDB();
        }
    }

    private void fetchFromWeb(double longitude, double latitude) {
        Log.w(TAG, "fetching from web...");
        TaskFetchFromWeb asyncTask = (TaskFetchFromWeb) new TaskFetchFromWeb(longitude, latitude) {
            @Override
            protected void onPostExecute(Forecast forecast) {
                if (forecast == null) {
                    Log.e(TAG, "No forecast found");
                    weatherList.postValue(null);
                    weatherForecast.postValue(null);
                } else {
                    weatherList.postValue(forecast.items);
                    weatherForecast.postValue(forecast);
                }
            }
        }.execute();
    }

    private void fetchFromDB() {
        Log.w(TAG, "fetching from db...");
        TaskFetchFromDB asyncTask = (TaskFetchFromDB) new TaskFetchFromDB() {
            @Override
            protected void onPostExecute(Forecast forecast) {
                if (forecast == null) {
                    Log.e(TAG, "No forecast found");
                    weatherList.postValue(null);
                    weatherForecast.postValue(null);
                } else {
                    weatherList.postValue(forecast.items);
                    weatherForecast.postValue(forecast);
                }
            }
        }.execute();
    }



}
