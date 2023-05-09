package id.ac.umn.nutrimo.periksa;

public class HistoryModel {
    String date,heightStatus,nutrition,result;
    int age;
    double haz,height,weight,whz;
    public HistoryModel() {
    }

    public HistoryModel(int age, String date, double haz, double height, String heightStatus, String nutrition, String result, double weight, double whz) {
        this.age = age;
        this.date = date;
        this.haz = haz;
        this.height = height;
        this.heightStatus = heightStatus;
        this.nutrition = nutrition;
        this.result = result;
        this.weight = weight;
        this.whz = whz;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeightStatus() {
        return heightStatus;
    }

    public void setHeightStatus(String heightStatus) {
        this.heightStatus = heightStatus;
    }

    public String getNutrition() {
        return nutrition;
    }

    public void setNutrition(String nutrition) {
        this.nutrition = nutrition;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getHaz() {
        return haz;
    }

    public void setHaz(double haz) {
        this.haz = haz;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getWhz() {
        return whz;
    }

    public void setWhz(double whz) {
        this.whz = whz;
    }
}
