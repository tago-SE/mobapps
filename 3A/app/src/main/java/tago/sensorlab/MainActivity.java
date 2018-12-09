package tago.sensorlab;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "Main";

    private TextView tvA;

    final Handler handler = new Handler();

    private SensorManager sensorManager;

    private double x = 0;
    private double y = 0;
    private double z = 0;
    private double lastX, lastY, lastZ;

    private final double SHAKE_TRESHOLD = 800;

    private double F = 0.3;

    float gravity[], magnetic[], accels[], mags[], values[], azimuth, pitch, roll;

    private Sensor accelerometerSensor;
    private Sensor magneticSensor;

    private double prevInclination;
    private long lastUpdate;
    private long lastShake;
    private long firstShake;

    private boolean killThread = false;

    // Reference: https://www.ssaurel.com/blog/get-android-device-rotation-angles-with-accelerometer-and-geomagnetic-sensors/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        tvA = findViewById(R.id.angle);

        sensorManager= (SensorManager) getSystemService(this.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        accels = new float[3];
        mags = new float[3];
        values = new float[3];

        tvA.setTextSize(40);
        tvA.setTextColor(Color.RED);
        tvA.setTextColor(Color.BLACK);

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long curTime = System.currentTimeMillis();
                if (lastShake - firstShake > 1000 && !(lastShake + 200 < curTime)) {
                    tvA.setTextColor(Color.RED);
                } else {
                    tvA.setTextColor(Color.BLACK);
                }
                if (!killThread)
                    handler.postDelayed(this, 200);
            }
        };
        handler.post(runnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onDestroy() {
        killThread = true;
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_MAGNETIC_FIELD: {
                mags = event.values.clone();
            }
            break;
            case Sensor.TYPE_ACCELEROMETER: {
                accels = event.values.clone();
                long curTime = System.currentTimeMillis();
                if (curTime - lastUpdate > 1) {
                    long diffTime = curTime - lastUpdate;
                    lastUpdate = curTime;
                    values = event.values.clone();

                    // Filter
                    x = F*lastX + (1 - F)*values[0];
                    y = F*lastY + (1 - F)*values[1];
                    z = F*lastZ + (1 - F)*values[2];

                    // Display angle
                    // 57.2957795f alternative????

                    // Omit gravity
                    // double k = 90 / 9.82;
                    double angle = Math.toDegrees(Math.atan2(x, z));
                    if (tvA != null)
                        tvA.setText("" + (int) angle + " °");
                    // Shake detection
                    double delta = Math.abs(x + y + z - lastX - lastY - lastZ);
                    double speed = delta / diffTime * 10000.;
                    Log.d(TAG, "" + delta + " " + speed + " " + SHAKE_TRESHOLD);
                    if (speed > SHAKE_TRESHOLD) {
                        if (lastShake + 200 < curTime) {
                            lastShake = 0;
                            firstShake = 0;
                        }
                        if (firstShake == 0) {
                            firstShake = curTime;
                        }
                        lastShake = curTime;
                        long shakeTime = lastShake - firstShake;
                        Log.d(TAG, "SHAKING for " + shakeTime);
                        if (lastShake - firstShake > 1000) {
                            Log.d(TAG, "SUCCESS");
                        }
                    }
                    lastX = x;
                    lastY = y;
                    lastZ = z;
                }
            }
            break;
        }
    }
        /*
        if (mags != null && accels != null) {
            gravity = new float[9];
            magnetic = new float[9];
            // Computes the inclination matrix I (magnetic) and rotation matrix R (gravity)
            // transforming a vector from the device coordinate system to the worlds coordinate
            // system, which is defined as a direct orthonormal  basis where:
            // x == vector product Y.Z
            // y == tangential to the ground at the devices current location
            // z == points towards the sky
            SensorManager.getRotationMatrix(gravity, magnetic, accels, mags);


            //float[] outGravity = new float[9];
            //SensorManager.remapCoordinateSystem(gravity, SensorManager.AXIS_X,SensorManager.AXIS_Z, outGravity);

            SensorManager.getOrientation(gravity, values);

            double inclination = Math.toDegrees(SensorManager.getInclination(gravity));

            double rotation = Math.toDegrees(SensorManager.getRotati);
           // double k = 90 / 9.82;
            //double angle = Math.atan2(x * k, z * k) * 180 / Math.PI;
            if (tvA != null)
                tvA.setText("" + (int) inclination + " °");


            azimuth = values[0];
            pitch = values[1];
            roll = values[2];
            mags = null;
            accels = null;
            Log.d(TAG, (pitch + " " + Math.toDegrees(values[1])));

        }


            /*
            float[] g = event.values.clone();
                float norm_Of_g = (float) Math.sqrt(g[0] * g[0] + g[1] * g[1] + g[2] * g[2]);
                g[0] = g[0] / norm_Of_g;
                g[1] = g[1] / norm_Of_g;
                g[2] = g[2] / norm_Of_g;

                double inclination =  Math.round(Math.toDegrees(Math.acos(g[2])));
                double rotation = Math.round(Math.toDegrees(Math.atan2(g[0], g[1])));

                if (prevInclination == 0)
                    prevInclination = inclination;
                inclination = F*prevInclination + (1 - F)*inclination;
                prevInclination = inclination;

                float[] f = new float[3];
                f[0] = g[0]; f[1] = g[1]; f[2] = g[2];
                Log.d(TAG, " " + Math.round(inclination) + " " + (int) rotation + " " +  SensorManager.getInclination(f));

                double k = 90 / 9.82;
                double degree = Math.atan2(x * k, z * k) * 180 / Math.PI;

                if (tvA != null)
                    tvA.setText("" + (int) degree + " " + inc);
             */


            /*
                float[] g = event.values.clone();
                float norm_Of_g = (float) Math.sqrt(g[0] * g[0] + g[1] * g[1] + g[2] * g[2]);
                g[0] = g[0] / norm_Of_g;
                g[1] = g[1] / norm_Of_g;
                g[2] = g[2] / norm_Of_g;

                double inclination =  Math.round(Math.toDegrees(Math.acos(g[2])));
                double rotation = Math.round(Math.toDegrees(Math.atan2(g[0], g[1])));

                if (prevInclination == 0)
                    prevInclination = inclination;
                inclination = F*prevInclination + (1 - F)*inclination;
                prevInclination = inclination;

                float[] f = new float[3];
                f[0] = g[0]; f[1] = g[1]; f[2] = g[2];
                Log.d(TAG, " " + Math.round(inclination) + " " + (int) rotation + " " +  SensorManager.getInclination(f));
                long curTime = System.currentTimeMillis();
            } break; */






        /*
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            WindowManager mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display mDisplay = mWindowManager.getDefaultDisplay();
            float x = 0, y = 0, z = 0;
            switch (mDisplay.getRotation()) {
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
                tvX.setText(" " + x);
            }
            if (tvY != null) {
                tvY.setText(" " + y);
            }
            if (tvZ != null) {
                tvZ.setText(" " + z);
            }

        }
 */
    //}


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
