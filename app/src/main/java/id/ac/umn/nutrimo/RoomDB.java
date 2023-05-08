package id.ac.umn.nutrimo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import id.ac.umn.nutrimo.periksa.HAZDao;
import id.ac.umn.nutrimo.periksa.HazEntity;
import id.ac.umn.nutrimo.periksa.WHZDao;
import id.ac.umn.nutrimo.periksa.WhzEntity;

@Database(entities = {HazEntity.class, WhzEntity.class}, version = 1)
public abstract class RoomDB extends RoomDatabase {
    public abstract HAZDao hazDao();
    public abstract WHZDao whzDao();

    private static RoomDatabase instance;

    public static synchronized RoomDB getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),RoomDB.class,"NutriMoDB")
                    .allowMainThreadQueries()
                    .createFromAsset("database/antropometri.db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return (RoomDB) instance;
    }
}
