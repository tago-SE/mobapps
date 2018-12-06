package tago.sensorlab;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "Main";

    private TextView tvX;
    private TextView tvY;
    private TextView tvZ;

    private SensorManager sensorManager;

    private double x = 0;
    private double y = 0;
    private double z = 0;

    private Sensor accelerometer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvX = findViewById(R.id.x);
        tvY = findViewById(R.id.y);
        tvZ = findViewById(R.id.z);

        sensorManager= (SensorManager) getSystemService(this.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Lists all sensors on the device
        for (Sensor s : sensorManager.getSensorList(Sensor.TYPE_ALL)) {
            Log.w(TAG, "" + s.getName() + ", " + s.getVendor());
        }

        /*
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (accelerometer == null) {
            Log.e(TAG, "Accelerometer does not exist on the device.");
        }
        */
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Display display = getWindowManager().getDefaultDisplay();
        switch (display.getRotation()) {
            case Surface.ROTATION_0:
                x = event.values[0];
                y = event.values[1];
                z = event.values[2];
                break;
            case Surface.ROTATION_90:
                x = -event.values[1];
                y = event.values[0];
                z = event.values[2];
                break;
            case Surface.ROTATION_180:
                x = -event.values[0];
                y = -event.values[1];
                z = event.values[2];
                break;
            case Surface.ROTATION_270:
                x = event.values[1];
                y = -event.values[0];
                z = event.values[2];
                break;
        }
        if (tvX != null) {
            tvX.setText("" + (int) x);
        }
        if (tvY != null) {
            tvY.setText("" + (int) y);
        }
        if (tvZ != null) {
            tvZ.setText("" + (int) z);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
