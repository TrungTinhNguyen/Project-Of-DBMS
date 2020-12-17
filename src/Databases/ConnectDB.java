package Databases;

import Modules.Account;
import Modules.Staff;
import com.mysql.cj.jdbc.CallableStatement;

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
            CallableStatement statement = (CallableStatement) connection.prepareCall("call getUser(?)")) {
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
}
