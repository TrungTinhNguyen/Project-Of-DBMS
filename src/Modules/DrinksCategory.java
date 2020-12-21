package Modules;

public class DrinksCategory {
    private int ID;
    private String name;

    public DrinksCategory(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public int getCategoryID() {
        return ID;
    }

    public void setCategoryID(int ID) {
        this.ID = ID;
    }

    public String getCategoryName() {
        return name;
    }

    public void setCategoryName(String name) {
        this.name = name;
    }
}
