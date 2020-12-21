package Controller;

import Databases.ConnectDB;
import Modules.Account;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Controller {

    @FXML private TextField txtFUsername;
    @FXML private PasswordField txtFPassword;

    private static Account user;

    public void login (ActionEvent event) {
        String username = txtFUsername.getText();
        String password = txtFPassword.getText();
        int checked = -1;
        ConnectDB connectDB = null;
        try{
            connectDB = new ConnectDB();
            checked = connectDB.validate(username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (checked > -1) {
            user = connectDB.getUser(username);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            try{
                Parent root = FXMLLoader.load(new File("src/view/homePage.fxml").toURI().toURL());
                stage.setScene(new Scene(root));
                stage.setTitle("TRANG CHỦ");
                DoubleProperty locate_x = new SimpleDoubleProperty();
                DoubleProperty locate_y = new SimpleDoubleProperty();
                Rectangle2D primaryScreen = Screen.getPrimary().getVisualBounds();
                locate_x.bind(new SimpleDoubleProperty((primaryScreen.getWidth()-stage.getWidth())/2));
                locate_y.bind(new SimpleDoubleProperty((primaryScreen.getHeight()-stage.getHeight())/2));
                stage.setY(locate_y.doubleValue());
                stage.setX(locate_x.doubleValue());
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ĐĂNG NHẬP THẤT BẠI");
            alert.setHeaderText("Đăng nhập không thành công");
            alert.setContentText("Tên đăng nhập hoặc mật khẩu không chính xác\nvui long kiểm tra lại");
            alert.show();
        }
    }
    public static Account getUser() {
        return user;
    }
}