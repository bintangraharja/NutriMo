package id.ac.umn.nutrimo.periksa;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HAZDao {
    @Query("SELECT * FROM HAZ")
    LiveData<List<HazEntity>> getAllHAZ();
    @Query("SELECT * FROM HAZ WHERE gender IS :gender ")
    List<HazEntity> getHazByGender(String gender);

}
