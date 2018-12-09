package tago.a3b;

import com.jjoe64.graphview.series.DataPoint;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect()
    {
        PulseManager pm = PulseManager.getInstance();

        pm.addValue(55);
        pm.addValue(77);
        List<DataPoint> list = new ArrayList<>();
        int x = 0;
        for (Integer v : pm.values()) {
            list.add(new DataPoint(x, v));
            x++;
        }
        DataPoint[] dataPoints = new DataPoint[x];
        for (int i = 0; i < x; i++) {
            dataPoints[i] = list.get(i);
        }
        System.out.println(dataPoints.toString());

        // LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>((DataPoint[]) dataPoints.toArray());
        //graph.addSeries(series);
    }
}