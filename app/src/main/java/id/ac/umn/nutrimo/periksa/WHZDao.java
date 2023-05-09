package id.ac.umn.nutrimo.periksa;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WHZDao {
    @Query("SELECT * FROM WHZ")
    LiveData<List<WhzEntity>> getAllWHZ();
    @Query("SELECT median FROM WHZ WHERE tinggi IS :height AND gender IS :gender")
    Double getMedian(double height, String gender);
    @Query("SELECT nsd1 FROM WHZ WHERE tinggi IS :height  AND gender IS :gender")
    Double getNsdp1(double height, String gender);
    @Query("SELECT psd1 FROM WHZ WHERE tinggi IS :height  AND gender IS :gender")
    Double getPsd1(double height, String gender);
    @Query("SELECT * FROM WHZ WHERE gender IS :gender ")
    List<WhzEntity> getWhzByGender(String gender);
}
