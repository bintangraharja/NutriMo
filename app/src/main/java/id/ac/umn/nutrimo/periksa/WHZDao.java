package id.ac.umn.nutrimo.periksa;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WHZDao {
    @Query("SELECT * FROM WHZ")
    LiveData<List<WhzEntity>> getAllWHZ();
}
