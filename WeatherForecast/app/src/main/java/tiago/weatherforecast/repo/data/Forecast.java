package tiago.weatherforecast.repo.data;

import java.util.ArrayList;
import java.util.List;

public class Forecast {

    public final String date;
    public final  String time;
    public final  double longitude;
    public final  double latitude;
    public List<Weather> items = new ArrayList<>();

    public Forecast(double longitude, double latitude, String date, String time) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.date = date;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Forecast{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", items=" + items +
                '}';
    }
}
