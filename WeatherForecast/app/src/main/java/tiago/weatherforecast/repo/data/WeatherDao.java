package tiago.weatherforecast.repo.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface WeatherDao {

    @Query("Select * FROM weatherentity")
    List<WeatherEntity> getAll();

    @Query("SELECT * FROM weatherentity WHERE forecast IN (:id) ORDER BY date, time")
    List<WeatherEntity> loadAllByIds(int id);

    @Insert
    void insertAll(WeatherEntity... weathers);

    @Delete
    void delete(WeatherEntity weather);

    @Query("DELETE FROM weatherentity")
    void dropAll();

    @Query("DELETE FROM weatherentity WHERE forecast IN (:id)")
    void deleteMatchingForecast(int id);
}
