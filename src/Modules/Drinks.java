package Modules;

public class Drinks extends DrinksCategory{
    private int drinksID;
    private String name;
    private float price;

    public Drinks(int ID, String name, int drinksID, String name1, float price) {
        super(ID, name);
        this.drinksID = drinksID;
        this.name = name1;
        this.price = price;
    }

    public int getDrinksID() {
        return drinksID;
    }

    public void setDrinksID(int drinksID) {
        this.drinksID = drinksID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
