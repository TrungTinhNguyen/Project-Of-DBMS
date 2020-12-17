package Modules;

import java.sql.Date;

public class Account extends Staff{
    private String username;
    private String password;
    private int account_type;

    public Account(int staffID, String full_name, String position, String address, Date birthday, Date begin_date, String username, String password, int account_type) {
        super(staffID, full_name, position, address, birthday, begin_date);
        this.username = username;
        this.password = password;
        this.account_type = account_type;
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
