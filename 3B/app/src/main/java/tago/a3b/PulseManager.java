package tago.a3b;

import android.util.Log;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Date;
import java.util.Queue;


public class PulseManager {
    private static final PulseManager ourInstance = new PulseManager();
    public static PulseManager getInstance() {
        return ourInstance;
    }

    private static final String TAG = "Pulse";

    private PulseManager() {
    }

    private Queue<Integer> pulseQueue = new ArrayDeque<>();

    private int lastBpm;

    private double pulse;
    private double prevPulse;

    private static final int MIN_HEART_RATE = 30;
    private static final int MAX_HEART_RATE = 150;

    private static final double TRESH = 520;
    private double tresh = TRESH;
    private double treshTop;
    private double treshBot;

    private double a = 0.5;
    private double b = 1.0 - a;

    private double topValue;
    private double botValue;
    private boolean prevIncrease = false;


    private long diffTime;
    private long prevTime;
    private int peakCounter;
    private int flukeCounter;
    private boolean flukeDetected;

    public int beatsPerMin() {
        if (pulseQueue.size() == 0)
            return 0;
        int totalTime = 0;
        for (Integer timeDiff : pulseQueue)
            totalTime += timeDiff;
        int avgTime = totalTime/pulseQueue.size();
        return 60000/avgTime;
    }

    public int getLastBpm() {
        return lastBpm;
    }

    public void addValue(int pulse) {
    //    if (prevPulse == 0)
    //        prevPulse = pulse;

        double pulseFiltered = a*pulse + b*prevPulse;

        // Debug var

        if (pulseFiltered > prevPulse) {
            //Log.d(TAG, "increasing...");
            // increasing state
            if (prevIncrease == false) {
                // previously decreasing, but now increasing
                if (prevPulse < tresh) {          // need to be dynamically set
                    botValue = 1000;           // extremely high number

                    //treshBot = pulseFiltered*1.2;
                    //tresh = treshTop;
                    System.out.println("bot");

                }
            }
            prevIncrease = true;
            topValue = pulseFiltered;
        } else {
            // decreasing state
            if (prevIncrease == true) {
                System.out.println("top");
                // previously increasing, but now decreasing
                peakCounter++;
                if (prevPulse > tresh) {
                    // potential maximum value
                    long curTime = System.currentTimeMillis();
                    diffTime = curTime - prevTime;
                    prevTime = curTime;
                    topValue = 0;

                    // Time elapsed since last detected pulse
                    //bpm = (int) (60000/diffTime);

                    lastBpm = (int) (60000/diffTime);
                    if (lastBpm > MIN_HEART_RATE && lastBpm < MAX_HEART_RATE) {
                        pulseQueue.offer((int) diffTime);
                        if (pulseQueue.size() >= 10)
                            pulseQueue.poll();
                    } else {
                        flukeCounter++;
                    }
                    Log.e(TAG, "HEART BEAT " + lastBpm);
                    peakCounter = 0;

                   // treshTop = pulseFiltered*0.8;
                    //tresh = treshBot;

                    // Add pulse to sum
                }
                flukeDetected = false;
                if (peakCounter > 3) {
                    flukeDetected = true;
                    flukeCounter++;
                }
            }
            prevIncrease = false;
            botValue = pulseFiltered;
        }
        System.out.println(lastBpm + " " + beatsPerMin() + " " + pulseFiltered);
        prevPulse = pulseFiltered;
    }

    public boolean isFlukeDetected() {
        return flukeDetected;
    }

    public int getNumberFlukes() {
        return flukeCounter;
    }

    public void reset() {
        pulse = 0;
        prevIncrease = false;
        flukeCounter = 0;
        tresh = treshTop = treshBot = TRESH;
        pulseQueue.clear();
    }

}
