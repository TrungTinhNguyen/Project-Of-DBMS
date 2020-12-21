package Modules;

import java.sql.Date;

public class Account extends Staff{
    private String username;
    private String password;
    private int account_type;

    public Account(int staffID, String full_name, String position, String address, String tell, Date birthday, Date begin_date,float salary, String username, String password, int account_type) {
        super(staffID, full_name, position, address, tell, birthday, begin_date, salary);
        this.username = username;
        this.password = password;
        this.account_type = account_type;
    }

    public Account (Account other) {
        super(other.getStaffID(), other.getFull_name(), other.getPosition(), other.getAddress(), other.getTell(), other.getBirthday(), other.getBegin_date(), other.getSalary());
        this.username = other.getUsername();
        this.password = other.getPassword();
        this.account_type = other.account_type;
    }

    public Account (Staff staff) {
        super(staff);
    }

    public Account(int id, String fullName, String position, String address, String tell, Date birthday, Date beginDate, float salary) {
        super(id,fullName,position,address,tell,birthday,beginDate,salary);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAccount_type() {
        return account_type;
    }

    public void setAccount_type(int account_type) {
        this.account_type = account_type;
    }
}
