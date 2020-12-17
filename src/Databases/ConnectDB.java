package Databases;

import Modules.*;
import com.mysql.cj.jdbc.CallableStatement;
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

    public Staff getUser (String username) {
        Staff user = null;
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

                if (!resultSet.getString("username").isEmpty()) {
                    String password = resultSet.getString("password");
                    int level = resultSet.getInt("level");
                    user = new Account(id, fullName, position, address, tell, birthday, beginDate, username, password, level);
                } else
                    user = new Staff(id, fullName, position, address, tell, birthday, beginDate);


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
             PreparedStatement billStt = connection.prepareStatement("insert into Bill (id_table, idStaff, dateCheckIn, status) values (?,?,?,?)");
             PreparedStatement billInfoStt = connection.prepareStatement("insert into BillInfo (idBill, idDrinks, amountOf) values (?,?,?)")) {
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
}
