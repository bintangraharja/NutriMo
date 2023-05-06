package id.ac.umn.nutrimo;

public class ChildModel {
    String name, birthdate, gender, weight, height;

    public ChildModel(String name, String birthdate, String gender, String weight, String height) {
        this.name = name;
        this.birthdate = birthdate;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
    }

    public ChildModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
