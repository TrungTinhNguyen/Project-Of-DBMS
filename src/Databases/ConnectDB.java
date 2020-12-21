package Databases;

import Modules.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class ConnectDB {
    private static final String URL_DB = "jdbc:mysql://localhost:3307/QuanLyQuanCafe? user=root";

    public Connection conn_db () throws SQLException {
        return DriverManager.getConnection(URL_DB);
    }
    public int validate (String username, String password) throws SQLException {
        int checked = -1;
        try (
                Connection connection = conn_db();
                CallableStatement callableStatement = (CallableStatement) connection.prepareCall("call Login (?, ?, ?)")) {
            callableStatement.setString(1, username);
            callableStatement.setString(2, password);
            callableStatement.registerOutParameter(3, checked);
            callableStatement.execute();
            checked = callableStatement.getInt(3);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return checked;
    }

    public Account getUser (String username) {
        Account user = null;
        try (Connection connection = conn_db();
            CallableStatement statement = (CallableStatement) connection.prepareCall("call get_user(?)")) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String fullName = resultSet.getString(2);
                String position = resultSet.getString(3);
                String tell = resultSet.getString(4);
                String address = resultSet.getString("address");
                Date birthday = Date.valueOf(resultSet.getDate("birthday").toLocalDate());
                Date beginDate = Date.valueOf(resultSet.getDate("date_start").toLocalDate());
                float salary = resultSet.getFloat(9);

                if (!resultSet.getString("username").isEmpty()) {
                    String password = resultSet.getString("password");
                    int level = resultSet.getInt("level");
                    user = new Account(id, fullName, position, address, tell, birthday, beginDate,salary, username, password, level);
                } else
                    user = new Account(id, fullName, position, address, tell, birthday, beginDate, salary);


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public ObservableList <Drinks> getDrinksList () {
        ObservableList<Drinks> drinksList = FXCollections.observableArrayList();
        try (Connection connection = conn_db();
            Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select * from DrinksCategory, Drinks where id = idCategory");
            while (resultSet.next()) {
                Drinks drinks = new Drinks(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3), resultSet.getString(4), resultSet.getInt("price"));
                drinksList.add(drinks);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drinksList;
    }

    public ObservableList <Table> getTableList () {
        ObservableList<Table> tables = FXCollections.observableArrayList();
        try (Connection connection = conn_db();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select * from TableDrinks");
            while (resultSet.next()) {
                Table table = new Table(resultSet.getInt(1), resultSet.getString(2), resultSet.getBoolean(3));
                tables.add(table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

    public void payment (Bill bill) {
        try (Connection connection = conn_db();
             PreparedStatement billStt = connection.prepareStatement("insert into Bill (id_table, idStaff, dateCheckIn, status) values (?,?,?,?);");
             PreparedStatement billInfoStt = connection.prepareStatement("insert into BillInfo (idBill, idDrinks, amountOf) values (?,?,?);")) {
            billStt.setInt(1, bill.getTable().getTableID());
            billStt.setInt(2, bill.getStaff().getStaffID());
            billStt.setDate(3, bill.getDateCheckin());
            billStt.setInt(4, 1);
            billStt.executeUpdate();
            bill.getListDrinks().forEach(e -> {
                try {
                    billInfoStt.setInt(1, bill.getBillID());
                    billInfoStt.setInt(2, e.getID());
                    billInfoStt.setInt(3, e.getAmountOf());
                    billInfoStt.executeUpdate();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int countBill () {
        int i = 0;
        try (Connection connection = conn_db();
        Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("select count(id_bill) from Bill;");
            while (rs.next())
                i = rs.getInt(1);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    public ObservableList <Staff> getStaffList () {
        ObservableList <Staff> staffObservableList = FXCollections.observableArrayList();
        try (Connection connection = conn_db();
        CallableStatement callableStatement =  connection.prepareCall("call getStaffList();")) {
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String position = rs.getString(3);
                String address = rs.getString(4);
                String tell = rs.getString(5);
                Date birthday = rs.getDate(6);
                Date beginDay = rs.getDate(7);
                float salary = rs.getFloat(8);

                Staff staff = new Staff(id, name, position, address, tell, birthday, beginDay, salary);
                staffObservableList.add(staff);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffObservableList;
    }

    public void addStaff (Staff staff) throws SQLException {
        try (Connection connection = conn_db();
             CallableStatement callableStatement =  connection.prepareCall("call addStaff(?,?,?,?,?,?,?,?);")) {
            callableStatement.setInt(1, staff.getStaffID());
            callableStatement.setString(2, staff.getFull_name());
            callableStatement.setString(3, staff.getPosition());
            callableStatement.setString(4, staff.getAddress());
            callableStatement.setInt(5, Integer.parseInt(staff.getTell()));
            callableStatement.setDate(6, staff.getBirthday());
            callableStatement.setDate(7, staff.getBegin_date());
            callableStatement.setFloat(8, staff.getSalary());
            callableStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean checkAccount (int staffID) {
        boolean checked = false;
        try (Connection connection = conn_db();
             PreparedStatement statement = connection.prepareStatement("select hasAccount(?);")) {
            statement.setInt(1, staffID);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                checked = rs.getInt(1) == 1 ? true:false;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return checked;
    }

    public void deleteStaff (int staffID) {
        try (Connection connection = conn_db();
        CallableStatement statement = connection.prepareCall("call deleteStaff (?)")) {
            statement.setInt(1, staffID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
