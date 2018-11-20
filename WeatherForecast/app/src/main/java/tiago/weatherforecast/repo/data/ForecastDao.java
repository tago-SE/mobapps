package tiago.weatherforecast.repo.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ForecastDao {

    @Query("SELECT * FROM forecastentity ORDER BY id DESC LIMIT 1")
    ForecastEntity getLast();

    @Query("Select * FROM forecastentity")
    List<ForecastEntity> getAll();

    @Query("SELECT * FROM forecastentity WHERE id IN (:id)")
    List<ForecastEntity> loadAllByIds(String[] id);

    @Query("SELECT * FROM forecastentity WHERE date LIKE :date AND "
            + "time LIKE :time LIMIT 1")
    ForecastEntity FindByApprovedTime(String date, String time);

    @Query("SELECT * FROM forecastentity WHERE longitude LIKE :longitude AND "
            + "latitude LIKE :latitude LIMIT 1")
    ForecastEntity findByCoordinates(double longitude, double latitude);

    @Insert
    void insertAll(ForecastEntity... forecasts);

    @Update
    void update(ForecastEntity forecast);

    @Delete
    void delete(ForecastEntity forecast);

    @Query("DELETE FROM forecastentity")
    void dropAll();
}
