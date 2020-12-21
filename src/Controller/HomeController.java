package Controller;

import Modules.Account;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    private static Account user;

    @FXML private Label fullName;
    @FXML private Label today;

    public void toTable(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(new File("src/view/TablePage.fxml").toURI().toURL());
        stage.setScene(new Scene(root));
    }

    public void toStaffList (ActionEvent event) throws IOException {
        if (user.getAccount_type() == 0) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(new File("src/view/staffListPage.fxml").toURI().toURL());
            stage.setScene(new Scene(root));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Chức năng chỉ dành cho Quản lý");
            alert.setHeaderText("Không có quyền truy cập");
            alert.setContentText("Chức năng này chỉ dành cho Quản lý");
            alert.show();
        }

    }

    public void logout(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        user = null;
        URL url = new File("src/view/loginPage.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        user = Controller.getUser();
        Format formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date curDate = Date.valueOf(LocalDate.now());
        today.setText(formatter.format(curDate));
        fullName.setText(user.getFull_name());
    }
}
