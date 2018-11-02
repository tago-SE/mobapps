package tiago.weatherforecast.repo.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

// Ref: https://github.com/googlesamples/android-architecture-components

@Database(entities = {ForecastEntity.class, WeatherEntity.class}, version = 5, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = "AppDatabase";

    private static AppDatabase ourInstance;

    public static void buildDatabase(Context context) {
        ourInstance = Room
                .databaseBuilder(context, AppDatabase.class, "ex")
                .fallbackToDestructiveMigration()
                .build();
    }

    public static AppDatabase getInstance() {
        return ourInstance;
    }

    public AppDatabase() { }

    public abstract ForecastDao forecastDao();

    public abstract WeatherDao weatherDao();

}
