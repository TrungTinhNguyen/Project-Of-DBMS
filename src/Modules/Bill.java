package Modules;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;

public class Bill {
    private int billID;
    private Table table;
    private Staff staff;
    private ObservableList<BillInfo> listDrinks;
    private Date dateCheckin;
    private boolean status;

    private static int count = 1;

    public Bill(int billID, Table table, Staff staff, ObservableList<BillInfo> listDrinks, Date dateCheckin, boolean status) {
        this.billID = billID;
        this.table = table;
        this.staff = staff;
        this.listDrinks.addAll(listDrinks);
        this.dateCheckin = Date.valueOf(dateCheckin.toLocalDate());
        this.status = status;
        count ++;
    }

    public Bill(int billID, Table table, Staff staff, Date dateCheckin) {
        this.billID = billID;
        this.table = table;
        this.staff = staff;
        this.dateCheckin = Date.valueOf(dateCheckin.toLocalDate());
        this.status = false;
        listDrinks = FXCollections.observableArrayList();
        count++;
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

    public ObservableList<BillInfo> getListDrinks() {
        return listDrinks;
    }

    public void setListDrinks(ObservableList<BillInfo> listDrinks) {
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

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        Bill.count = count;
    }
}
