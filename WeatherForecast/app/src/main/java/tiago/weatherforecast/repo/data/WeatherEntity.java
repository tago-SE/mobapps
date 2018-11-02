package tiago.weatherforecast.repo.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Objects;

/**
 * Weather entity stored inside DB
 */
@Entity
public class WeatherEntity {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "forecast")
    private int forecastId;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "time")
    private String time;

    @ColumnInfo(name = "sky")
    private int sky;

    @ColumnInfo(name = "temperature")
    private double temperature;

    public WeatherEntity() { }

    public WeatherEntity(int forecastId, String date, String time, int sky, double temperature) {
        this.forecastId = forecastId;
        this.date = date;
        this.time = time;
        this.sky = sky;
        this.temperature = temperature;
        id = hashCode();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSky() {
        return sky;
    }

    public void setSky(int sky) {
        this.sky = sky;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getForecastId() {
        return forecastId;
    }

    public void setForecastId(int forecastId) {
        this.forecastId = forecastId;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "id=" + id +
                ", forecastId=" + forecastId +
                ", sky=" + sky +
                ", time='" + time + '\'' +
                ", date='" + date + '\'' +
                ", temperature=" + temperature +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSky(), getTime(), getDate(), getTemperature(), getForecastId());
    }
}
