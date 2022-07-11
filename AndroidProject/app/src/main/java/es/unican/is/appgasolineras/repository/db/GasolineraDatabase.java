package es.unican.is.appgasolineras.repository.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import es.unican.is.appgasolineras.model.Gasolinera;

/**
 * Class to persist gas stations into a local DB using Room
 * Usage: https://developer.android.com/training/data-storage/room
 */
@Database(entities = {Gasolinera.class}, version = 1, exportSchema = false)
public abstract class GasolineraDatabase extends RoomDatabase {

    public static final String GASOLINERAS_DB_NAME = "gasolineras-database";

    private static GasolineraDatabase db;

    public static GasolineraDatabase getDB(Context context) {
        if (db == null || !db.isOpen()) {
            db = Room
                    .databaseBuilder(context, GasolineraDatabase.class, GasolineraDatabase.GASOLINERAS_DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return db;
    }

    public abstract GasolineraDao gasolineraDao();

}
