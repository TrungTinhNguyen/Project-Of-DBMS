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

    public Drinks(Drinks drinks) {
        super(drinks.getCategoryID(), drinks.getCategoryName());
        this.drinksID = drinks.getDrinksID();
        this.name = drinks.getName();
        this.price = drinks.getPrice();
    }

    public int getDrinksID() {
        return drinksID;
    }

    public void setDrinksID(int drinksID) {
        this.drinksID = drinksID;
    }

    public String getName() {
        return name;
    }

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
