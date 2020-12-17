package Controller;

import Modules.Staff;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    private static Staff user;

    @FXML private Label fullName;
    @FXML private Label today;

    public void toTable(ActionEvent event) {

    }

    public void logout(ActionEvent event) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        user = Controller.getUser();
        Date curDate = Date.valueOf(LocalDate.now());
        today.setText(curDate.toString());
        fullName.setText(user.getFull_name());
    }
}
