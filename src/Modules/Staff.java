package Modules;

import java.sql.Date;

public class Staff {
    private int staffID;
    private String full_name;
    private String position;
    private String address;
    private String tell;
    private Date birthday;
    private Date begin_date;
    private float salary;

    public Staff(int staffID, String full_name, String position, String address, String tell, Date birthday, Date begin_date, float salary) {
        this.staffID = staffID;
        this.full_name = full_name;
        this.position = position;
        this.address = address;
        this.tell = tell;
        this.birthday = Date.valueOf(birthday.toLocalDate());
        this.begin_date = Date.valueOf(begin_date.toLocalDate());
        this.salary = salary;
    }

    public Staff (Staff staff) {
        this.staffID = staff.getStaffID();
        this.full_name = staff.getFull_name();
        this.position = staff.getPosition();
        this.address = staff.getAddress();
        this.tell = staff.getTell();
        this.birthday = staff.getBirthday();
        this.begin_date = staff.getBegin_date();
        this.salary = staff.getSalary();
    }

    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTell() {
        return tell;
    }

    public void setTell(String tell) {
        this.tell = tell;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = Date.valueOf(birthday.toLocalDate());
    }

    public Date getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(Date begin_date) {
        this.begin_date = Date.valueOf(begin_date.toLocalDate());
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }
}
