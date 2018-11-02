package tiago.weatherforecast.repo.data;

public class Weather {

    public final String date;
    public final String time;
    public final int sky;
    public final float temperature;

    public Weather(String date, String time, int sky, float temperature) {
        this.date = date;
        this.time = time;
        this.sky = sky;
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", sky=" + sky +
                ", temperature=" + temperature +
                '}';
    }
}
