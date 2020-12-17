package Modules;

import java.sql.Date;
import java.util.ArrayList;

public class Bill {
    private int billID;
    private Table table;
    private Staff staff;
    private ArrayList <BillInfo> listDrinks;
    private Date dateCheckin;
    private boolean status;

    public Bill(int billID, Table table, Staff staff, ArrayList<BillInfo> listDrinks, Date dateCheckin, boolean status) {
        this.billID = billID;
        this.table = table;
        this.staff = staff;
        listDrinks.forEach(drinks -> this.listDrinks.add(drinks));
        this.dateCheckin = dateCheckin;
        this.status = status;
    }

    public int getBillID() {
        return billID;
    }

    public void setBillID(int billID) {
        this.billID = billID;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public ArrayList<BillInfo> getListDrinks() {
        return listDrinks;
    }

    public void setListDrinks(ArrayList<BillInfo> listDrinks) {
        listDrinks.forEach(drinks -> this.listDrinks.add(drinks));
    }

    public Date getDateCheckin() {
        return dateCheckin;
    }

    public void setDateCheckin(Date dateCheckin) {
        this.dateCheckin = dateCheckin;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
