package Databases;

import com.mysql.cj.jdbc.CallableStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    private static final String URL_DB = "jdbc:mysql://localhost/QuanLyQuanCafe? user=root";

    public Connection conn_db () throws SQLException {
        Connection connection = DriverManager.getConnection(URL_DB);
         return connection;
    }
    public int validate (String username, String password) throws SQLException {
        int checked = -1;
        try (
                Connection connection = conn_db();
                CallableStatement callableStatement = (CallableStatement) connection.prepareCall("call Login (?, ?, ?)");) {
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
}
