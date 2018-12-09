package tago.a3b;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Date;
import java.util.Queue;


public class PulseManager {
    private static final PulseManager ourInstance = new PulseManager();
    public static PulseManager getInstance() {
        return ourInstance;
    }

    private PulseManager() {
    }

    private Queue<Integer> pulseQueue = new ArrayDeque<>();

    private Queue<Integer> valueQueue = new ArrayDeque<>();

    private int bpm;

    private double pulse;
    private double prevPulse;

    private double a = 0.2;
    private double b = 1.0 - a;

    private double topValue;
    private double botValue;
    private boolean prevIncrease = false;
    private boolean beatDetected = false;

    private long diffTime;
    private long prevTime;

    private double prevMeanPulse;

    public int beatsPerMin() {
        if (pulseQueue.isEmpty())
            return 0;
        int sum = 0;
        for (Integer v : pulseQueue) {
            sum += v;
        }
        int avg = sum/pulseQueue.size();
        bpm = 60000/avg;
        return bpm;
    }

    public void addValue(int value) {
        valueQueue.offer(value);
        if (valueQueue.size() > 100)
            valueQueue.poll();

        pulse = value;
        if (prevPulse == 0)
            prevPulse = pulse;

        double meanPulse = a*pulse + b*prevPulse;



        if (meanPulse > prevPulse) {


            // increasing state
            if (prevIncrease == false) {
                // previously decreasing, but now increasing
                if (meanPulse < 651) {          // need to be dynamically set
                    botValue = 10000;           // extremely high number
                    // potential minimum value
                }
            }
            prevIncrease = true;
            topValue = meanPulse;
        } else {
            // decreasing state
            if (prevIncrease == true) {
                // previously increasing, but now decreasing
                if (meanPulse > 651) {
                    // potential maximum value
                    long curTime = System.currentTimeMillis();
                    diffTime = curTime - prevTime;
                    prevTime = curTime;
                    topValue = 0;
                    // Add to stack
                    pulseQueue.offer((int) diffTime);
                    if (pulseQueue.size() > 10)
                        pulseQueue.poll();
                    bpm = beatsPerMin();
                    beatDetected = true;
                }
            }
            prevIncrease = false;
            botValue = meanPulse;
        }
        System.out.println(beatDetected + " " + bpm + " " + meanPulse);
        prevPulse = meanPulse;
        beatDetected = false;
    }

    public Collection<Integer> values() {
        return valueQueue;
    }


    public void clear() {
        pulseQueue.clear();
        pulse = 0;
        prevIncrease = false;
    }

}
