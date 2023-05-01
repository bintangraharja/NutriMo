package id.ac.umn.nutrimo.menu;

public class MenuModel {

    String cook, image, item, name;

    public MenuModel() {
    }

    public MenuModel(String cook, String image, String item, String name) {
        this.cook = cook;
        this.image = image;
        this.item = item;
        this.name = name;
    }

    public String getCook() {
        return cook;
    }

    public void setCook(String cook) {
        this.cook = cook;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
