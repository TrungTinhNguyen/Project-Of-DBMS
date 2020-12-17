package Modules;

public class BillInfo {
    private int ID;
    private Drinks drinks;
    private int amountOf;

    public BillInfo(int ID, Drinks drinks, int amountOf) {
        this.ID = ID;
        this.drinks = drinks;
        this.amountOf = amountOf;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Drinks getDrinks() {
        return drinks;
    }

    public void setDrinks(Drinks drinks) {
        this.drinks = drinks;
    }

    public int getAmountOf() {
        return amountOf;
    }

    public void setAmountOf(int amountOf) {
        this.amountOf = amountOf;
    }
}
