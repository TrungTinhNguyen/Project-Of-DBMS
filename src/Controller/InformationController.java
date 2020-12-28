package Controller;

import Databases.ConnectDB;
import Modules.Account;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class InformationController implements Initializable {

    private static  Account user;

    @FXML private Label fullName;
    @FXML private Label today;

    @FXML private Label staffIDField;
    @FXML private Label staffNameField;
    @FXML private Label staffBirthdayField;
    @FXML private Label staffAddressField;
    @FXML private Label staffTellField;
    @FXML private Label staffPositionField;
    @FXML private Label staffBeginDateField;
    @FXML private Label staffSalaryField;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        user = Controller.getUser();
        HomeController.headerInit(user, fullName, today);
        user.setSalary(new ConnectDB().getSalary(user.getStaffID()));
        staffIDField.setText(String.valueOf(user.getStaffID()));
        staffNameField.setText(user.getFull_name());
        staffBirthdayField.setText(new SimpleDateFormat("dd/MM/yyyy").format(Date.valueOf(user.getBirthday().toLocalDate())));
        staffAddressField.setText(user.getAddress());
        staffTellField.setText(user.getTell());
        staffPositionField.setText(user.getPosition());
        staffBeginDateField.setText(new SimpleDateFormat("dd/MM/yyyy").format(Date.valueOf(user.getBegin_date().toLocalDate())));
        staffSalaryField.setText(String.valueOf(user.getSalary()));
    }

    public void backToHome(ActionEvent event) throws IOException {
        new TableController().backToHome(event);
    }
}
