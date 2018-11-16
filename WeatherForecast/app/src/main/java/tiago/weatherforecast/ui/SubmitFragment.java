package tiago.weatherforecast.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import tiago.weatherforecast.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SubmitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubmitFragment extends Fragment {

    private static final String TAG = "SubmitFrag";
    private static final String ARG_LONG = "long";
    private static final String ARG_LAT = "lat";

    // cycle interval (in milliseconds)
    private static final int INTERVAL = 1000;

    // Time for updating data if wifi-connection (in seconds)
    private static final int WIFI_INTERVAL = 10; //10*60;

    // Time for updating data if 3G/4G-connection (in seconds)
    private static final int NET_INTERVAL = 60; //60*60;

    private static final double INVALID_ENTRY = 999;
    private double longitude = INVALID_ENTRY;
    private double latitude = INVALID_ENTRY;
    private Button buttonSubmit = null;
    private EditText textLong = null;
    private EditText textLat = null;
    private MainViewModel mainViewModel;
    private Context context;
    private int elapsedSeconds;

    public SubmitFragment() {
        // Required empty public constructor
    }

    /**
     * Injects shared ViewModel to update the fragment
     * @param vm
     */
    public void injectMainViewModel(Context context, MainViewModel vm) {
        mainViewModel = vm;
        this.context = context;
        pullDataPeriodically();
    }

    /**
     * Factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param longitude Default Longitude
     * @param latitude Default Latitude
     * @return A new instance of fragment Submit.
     */
    public static SubmitFragment newInstance(double longitude, double latitude) {
        SubmitFragment fragment = new SubmitFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_LONG, longitude);
        args.putDouble(ARG_LAT, latitude);
        fragment.setArguments(args);
        return fragment;
    }

    private void pullDataPeriodically() {
        elapsedSeconds = 0;
        final Handler handler = new Handler();
        final ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(context.CONNECTIVITY_SERVICE);

        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                try {
                    elapsedSeconds += INTERVAL / 1000;
                    if (elapsedSeconds >= WIFI_INTERVAL) {
                        if (validateSubmission(false)) {
                            Log.w(TAG, "VALIDATED");
                            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                                mainViewModel.doSubmit(context, longitude, latitude);
                                elapsedSeconds = 0;
                                Toast.makeText(context, "Refreshing...", Toast.LENGTH_LONG).show();
                            } else if (elapsedSeconds >= NET_INTERVAL &&
                                    activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                                mainViewModel.doSubmit(context, longitude, latitude);
                                elapsedSeconds = 0;
                                Toast.makeText(context, "Refreshing...", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            elapsedSeconds = 0;
                        }
                    }
                } catch(Exception e) {
                    Log.e(TAG, "error: " + e.getMessage());
                } finally {
                    handler.postDelayed(this, INTERVAL);
                }
            }
        };
        handler.postDelayed(runnableCode, INTERVAL);
    }

    private boolean validateSubmission(boolean showError) {
        boolean valid = true;
        String errMsg = "";

        String s1 = textLong.getText().toString();
        String s2 = textLat.getText().toString();
        if (s1.isEmpty() || s2.isEmpty()) {
            valid = false;
            errMsg = "Invalid Entry";
        } else {
            longitude = Double.parseDouble(s1);
            latitude = Double.parseDouble(s2);
        }
        if (valid && (longitude < -180. || longitude> 180)) {
            valid = false;
            errMsg = "Invalid longitude [-180, 180]: " + longitude;
        }
        if (valid && (latitude < -90 || latitude > 90)) {
            valid = false;
            errMsg = "Invalid latitude [-180, 180]: " + latitude;
        }
        if (!valid && showError) {
            Log.w(TAG, "Toast: " + errMsg);
            Toast.makeText(context, errMsg, Toast.LENGTH_LONG).show();
        }
        return valid;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        textLong.setText("" + longitude);
        textLat.setText("" + latitude);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            longitude = getArguments().getDouble(ARG_LONG);
            latitude = getArguments().getDouble(ARG_LAT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_submit, container, false);
        buttonSubmit = view.findViewById(R.id.submit);
        textLong = view.findViewById(R.id.sLong);
        textLat = view.findViewById(R.id.sLat);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (validateSubmission(true)) {
                        mainViewModel.doSubmit(context, longitude, latitude);
                        elapsedSeconds = 0;
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
        return view;
    }
}
