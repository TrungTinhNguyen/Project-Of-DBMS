package Controller;

import Modules.Account;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Screen;
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
            alert.setTitle("Quản Lý Nhân Sự");
            alert.setHeaderText("Không có quyền truy cập");
            alert.setContentText("Chức năng này chỉ dành cho Quản lý");
            alert.show();
        }
    }

    public void toStatistics (ActionEvent event) throws IOException {
        if (user.getAccount_type() == 0) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(new File("src/view/statisticsPage.fxml").toURI().toURL());
            stage.setScene(new Scene(root));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thống Kê");
            alert.setHeaderText("Không có quyền truy cập");
            alert.setContentText("Chức năng này chỉ dành cho Quản lý");
            alert.show();
        }
    }
    public void toDrinksList (ActionEvent event) throws IOException{
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(new File("src/view/drinksList.fxml").toURI().toURL());
        stage.setScene(new Scene(root));
    }
    public void toInformation (ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(new File("src/view/informationPage.fxml").toURI().toURL());
        stage.setScene(new Scene(root));
    }

    public void logout(ActionEvent event) throws Exception {
           Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        user = null;
        URL url = new File("src/view/loginPage.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        stage.setScene(new Scene(root));
        setLocateStage(stage);
    }

    public static void setLocateStage(Stage stage) {
        DoubleProperty locate_x = new SimpleDoubleProperty();
        DoubleProperty locate_y = new SimpleDoubleProperty();
        Rectangle2D primaryScreen = Screen.getPrimary().getVisualBounds();
        locate_x.bind(new SimpleDoubleProperty((primaryScreen.getWidth()-stage.getWidth())/2));
        locate_y.bind(new SimpleDoubleProperty((primaryScreen.getHeight()-stage.getHeight())/2));
        stage.setY(locate_y.doubleValue());
        stage.setX(locate_x.doubleValue());
        stage.show();
    }

    public static void headerInit (Account user, Label fullName, Label today) {
        fullName.setText(user.getFull_name());
        today.setText(new SimpleDateFormat("dd/MM/yyyy").format(Date.valueOf(LocalDate.now())));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        user = Controller.getUser();
        headerInit(user, fullName, today);
        fullName.setText(user.getFull_name());
    }
}