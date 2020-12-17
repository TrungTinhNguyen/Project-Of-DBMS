package Controller;

import Modules.Staff;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import javax.swing.text.DateFormatter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    private static Staff user;

    @FXML private Label fullName;
    @FXML private Label today;

    public void toTable(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(new File("src/view/TablePage.fxml").toURI().toURL());
        stage.setScene(new Scene(root));
    }

    public void logout(ActionEvent event) {
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
