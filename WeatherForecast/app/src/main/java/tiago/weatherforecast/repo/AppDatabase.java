package tiago.weatherforecast.repo;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import tiago.weatherforecast.repo.data.ForecastDao;
import tiago.weatherforecast.repo.data.ForecastEntity;
import tiago.weatherforecast.repo.data.WeatherDao;
import tiago.weatherforecast.repo.data.WeatherEntity;

// Ref: https://github.com/googlesamples/android-architecture-components

/**
 * The AppDatabase is a singleton class utilizing Room Persistence library, to create a cache for
 * the apps database using SQLite syntax. It contains a getInstance method to access the Singleton,
 * as well as a factory method for building the database and accessing the Database access objects:
 * @ForecastDao and @WeatherDao.
 */
@Database(entities = {ForecastEntity.class, WeatherEntity.class}, version = 5, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = "AppDatabase";

    private static AppDatabase ourInstance;

    /**
     * A factory method for instantiating the shared database. Should ideally only be executed once.
     * @param context the context in which the database should be created to.
     */
    public static void buildDatabase(Context context) {
        ourInstance = Room
                .databaseBuilder(context, AppDatabase.class, "ex")
                .fallbackToDestructiveMigration()
                .build();
    }

    /**
     * Access method.
     * @return AppDatabase instance
     */
    public static AppDatabase getInstance() {
        return ourInstance;
    }

    /**
     * Constructor.
     */
    public AppDatabase() { }

    /**
     * An abstract database access method for Forecast items.
     * @return a database access object - dao.
     */
    public abstract ForecastDao forecastDao();

    /**
     * An abstract database access method for Weather items.
     * @return a database access object - dao.
     */
    public abstract WeatherDao weatherDao();

}
