package id.ac.umn.nutrimo.periksa;

public class HistoryModel {
    String date,result,heightStatus, nutrition;

    public HistoryModel(String date, String result, String heightStatus, String nutrition) {
        this.date = date;
        this.result = result;
        this.heightStatus = heightStatus;
        this.nutrition = nutrition;
    }

    public HistoryModel() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getheightStatus() {
        return heightStatus;
    }

    public void setheightStatus(String heightStatus) {
        this.heightStatus = heightStatus;
    }

    public String getNutrition() {
        return nutrition;
    }

    public void setNutrition(String nutrition) {
        this.nutrition = nutrition;
    }
}
