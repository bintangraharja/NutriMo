package id.ac.umn.nutrimo.periksa;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "HAZ")
public class HazEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idHaz")
    private int idHaz;
    @NonNull
    @ColumnInfo(name = "gender")
    private String gender;

    @ColumnInfo(name = "usia")
    private int usia;

    @ColumnInfo(name = "nsd3")
    private double nsd3;

    @ColumnInfo(name = "nsd2")
    private double nsd2;

    @ColumnInfo(name = "nsd1")
    private int nsd1;

    @ColumnInfo(name = "median")
    private double median;

    @ColumnInfo(name = "psd1")
    private double psd1;

    @ColumnInfo(name = "psd2")
    private double psd2;

    @ColumnInfo(name = "psd3")
    private int psd3;

    public HazEntity(String gender, int usia, double nsd3, double nsd2, int nsd1, double median, double psd1, double psd2, int psd3) {
        this.gender = gender;
        this.usia = usia;
        this.nsd3 = nsd3;
        this.nsd2 = nsd2;
        this.nsd1 = nsd1;
        this.median = median;
        this.psd1 = psd1;
        this.psd2 = psd2;
        this.psd3 = psd3;
    }

    public int getIdHaz() {
        return idHaz;
    }

    public void setIdHaz(int idHaz) {
        this.idHaz = idHaz;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getUsia() {
        return usia;
    }

    public void setUsia(int usia) {
        this.usia = usia;
    }

    public double getNsd3() {
        return nsd3;
    }

    public void setNsd3(double nsd3) {
        this.nsd3 = nsd3;
    }

    public double getNsd2() {
        return nsd2;
    }

    public void setNsd2(double nsd2) {
        this.nsd2 = nsd2;
    }

    public int getNsd1() {
        return nsd1;
    }

    public void setNsd1(int nsd1) {
        this.nsd1 = nsd1;
    }

    public double getMedian() {
        return median;
    }

    public void setMedian(double median) {
        this.median = median;
    }

    public double getPsd1() {
        return psd1;
    }

    public void setPsd1(double psd1) {
        this.psd1 = psd1;
    }

    public double getPsd2() {
        return psd2;
    }

    public void setPsd2(double psd2) {
        this.psd2 = psd2;
    }

    public int getPsd3() {
        return psd3;
    }

    public void setPsd3(int psd3) {
        this.psd3 = psd3;
    }
}

