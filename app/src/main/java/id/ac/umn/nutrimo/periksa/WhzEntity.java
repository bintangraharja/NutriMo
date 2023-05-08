package id.ac.umn.nutrimo.periksa;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "WHZ")

public class WhzEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idWhz")
    private int idWhz;
    @NonNull
    @ColumnInfo(name = "gender")
    private String gender;

    @ColumnInfo(name = "tinggi")
    private float tinggi;

    @ColumnInfo(name = "nsd3")
    private float nsd3;

    @ColumnInfo(name = "nsd2")
    private float nsd2;

    @ColumnInfo(name = "nsd1")
    private float nsd1;

    @ColumnInfo(name = "median")
    private float median;

    @ColumnInfo(name = "psd1")
    private float psd1;

    @ColumnInfo(name = "psd2")
    private float psd2;

    @ColumnInfo(name = "psd3")
    private float psd3;

    public int getIdWhz() {
        return idWhz;
    }

    public void setIdWhz(int idWhz) {
        this.idWhz = idWhz;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public float getTinggi() {
        return tinggi;
    }

    public void setTinggi(float tinggi) {
        this.tinggi = tinggi;
    }

    public float getNsd3() {
        return nsd3;
    }

    public void setNsd3(float nsd3) {
        this.nsd3 = nsd3;
    }

    public float getNsd2() {
        return nsd2;
    }

    public void setNsd2(float nsd2) {
        this.nsd2 = nsd2;
    }

    public float getNsd1() {
        return nsd1;
    }

    public void setNsd1(float nsd1) {
        this.nsd1 = nsd1;
    }

    public float getMedian() {
        return median;
    }

    public void setMedian(float median) {
        this.median = median;
    }

    public float getPsd1() {
        return psd1;
    }

    public void setPsd1(float psd1) {
        this.psd1 = psd1;
    }

    public float getPsd2() {
        return psd2;
    }

    public void setPsd2(float psd2) {
        this.psd2 = psd2;
    }

    public float getPsd3() {
        return psd3;
    }

    public void setPsd3(float psd3) {
        this.psd3 = psd3;
    }
}
