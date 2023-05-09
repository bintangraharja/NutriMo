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
    @Query("SELECT median FROM HAZ WHERE usia IS :umur AND gender IS :gender")
    Double getMedian(int umur, String gender);
    @Query("SELECT nsd1 FROM HAZ WHERE usia IS :umur AND gender IS :gender")
    Double getNsdp1(int umur, String gender);
    @Query("SELECT psd1 FROM HAZ WHERE usia IS :umur AND gender IS :gender")
    Double getPsd1(int umur, String gender);

}
