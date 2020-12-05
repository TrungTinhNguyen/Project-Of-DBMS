package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Controller {

    @FXML private TextField txtFUsername;
    @FXML private PasswordField txtFPassword;

    public void login (ActionEvent event) {
        System.out.println(txtFUsername.getText());
    }
}
