package Modules;

import java.sql.Date;

public class Staff {
    private int staffID;
    private String full_name;
    private String position;
    private String address;
    private Date birthday;
    private Date begin_date;

    private static int count = 0;

    public Staff(int staffID, String full_name, String position, String address, Date birthday, Date begin_date) {
        this.staffID = staffID;
        this.full_name = full_name;
        this.position = position;
        this.address = address;
        this.birthday = Date.valueOf(birthday.toLocalDate());
        this.begin_date = Date.valueOf(begin_date.toLocalDate());
        count ++;
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

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        Staff.count = count;
    }
}
