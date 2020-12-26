package Controller;

import Databases.ConnectDB;
import Modules.Account;
import Modules.Staff;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class StaffController implements Initializable {
    private static final Staff user = Controller.getUser();

    @FXML private Label today;
    @FXML private Label fullName;

    @FXML private TextField staffName;
    @FXML private TextField staffPosition;
    @FXML private TextField staffAddress;
    @FXML private TextField staffTell;
    @FXML private TextField staffBirthday;
    @FXML private TextField staffSalary;
    @FXML private Label staffBeginDate;

    @FXML private TableView<Staff> staffTableView;
    @FXML private TableColumn<Staff, Integer> staffIDColumn;
    @FXML private TableColumn <Staff, String> staffNameColumn;
    @FXML private TableColumn <Staff, String> staffPositionColumn;
    @FXML private TableColumn <Staff, Float> staffSalaryColumn;

    private ObservableList <Staff> staffObservableList;

    public void addStaff () {
        int ID = staffObservableList.size()+1;
        String name = staffName.getText();
        String pos = staffPosition.getText();
        String addr = staffAddress.getText();
        String tell = staffTell.getText();
        Date birthday = Date.valueOf(staffBirthday.getText());
        Date today = Date.valueOf(LocalDate.now());
        float salary = Float.parseFloat(staffSalary.getText());
        Staff newStaff = new Staff(ID, name, pos, addr, tell, birthday, today, salary);
        ConnectDB connectDB = new ConnectDB();
        connectDB.addStaff(newStaff);
        staffObservableList.add(newStaff);
    }
    public void createAccount () {
       Staff selected = staffTableView.getSelectionModel().getSelectedItem();
       ConnectDB connectDB = new ConnectDB();
        if (!connectDB.checkAccount(selected.getStaffID())) {
            Account newAccount = new Account(selected);
            Dialog<Pair <String, Pair <String, Integer>>> creationDialog = new Dialog<>();
            GridPane grid = new GridPane();
            TextField usernameTxt = new TextField();
            TextField passwordTxt = new TextField();
            usernameTxt.setPromptText("Tên đăng nhập");
            passwordTxt.setPromptText("Mật khẩu");
            CheckBox isAd = new CheckBox();
            isAd.setText("Cấp Quản Lý");
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            grid.add(new Label("Tên đăng nhập:"), 0,0);
            grid.add(usernameTxt, 1, 0);
            grid.add(new Label("Mật khẩu:"), 0,1);
            grid.add(passwordTxt, 1, 1);
            grid.add(isAd, 1, 2);

            ButtonType doneButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            creationDialog.getDialogPane().getButtonTypes().addAll(doneButtonType, ButtonType.CANCEL);

            Node doneButton = creationDialog.getDialogPane().lookupButton(doneButtonType);
            doneButton.setDisable(true);

            usernameTxt.textProperty().addListener((observable, oldValue, newValue) -> doneButton.setDisable(newValue.trim().isEmpty()));
            creationDialog.getDialogPane().setContent(grid);
            creationDialog.setTitle("Cấp tài khoản");
            creationDialog.setHeaderText(selected.getFull_name());

            creationDialog.setResultConverter(dialogButton -> {
                if (dialogButton == doneButtonType){
                    return new Pair<>(usernameTxt.getText(), new Pair<>(passwordTxt.getText(), (isAd.isSelected() ? 1 : 0)));
                }
                return null;
            } );
            Optional<Pair<String, Pair<String, Integer>>> result = creationDialog.showAndWait();
            result.ifPresent(account -> {
                newAccount.setUsername(account.getKey());
                newAccount.setPassword(account.getValue().getKey());
                newAccount.setAccount_type(account.getValue().getValue());
            });
        }
    }
    public void deleteStaff () {
        Staff selected = staffTableView.getSelectionModel().getSelectedItem();
        new ConnectDB().deleteStaff(selected.getStaffID());
        staffObservableList.remove(selected);
    }
    public void tableViewClicked() {
        Staff selected = staffTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            staffName.setText(selected.getFull_name());
            staffPosition.setText(selected.getPosition());
            staffAddress.setText(selected.getAddress());
            staffBirthday.setText(String.valueOf(selected.getBirthday()));
            staffTell.setText(selected.getTell());
            staffSalary.setText(String.valueOf(selected.getSalary()));
            staffBeginDate.setDisable(false);
            staffBeginDate.setText(String.valueOf(selected.getBegin_date()));
        }
    }
    public void updateStaffInfo () {
        Staff selected = staffTableView.getSelectionModel().getSelectedItem();
        int staffId = selected.getStaffID();
        boolean flag = false;
        String name = "";
        String pos = "";
        String adr = "";
        String birD = "";
        String tell = "";
        float salary = 0;
        if (!staffName.getText().equals(selected.getFull_name())) {
            name = staffName.getText();
            flag = true;
        }
        if (!staffPosition.getText().equals(selected.getPosition())) {
            pos = staffPosition.getText();
            flag = true;
        }
        if (!staffAddress.getText().equals(selected.getAddress())) {
            adr = staffAddress.getText();
            flag = true;
        }
        if (!String.valueOf(selected.getBirthday()).equals(staffBirthday.getText())) {
            birD = staffBirthday.getText();
            flag = true;
        }
        if (!staffTell.getText().equals(selected.getTell())) {
            tell = staffTell.getText();
            flag = true;
        }
        if (Float.parseFloat(staffSalary.getText()) != selected.getSalary()) {
            salary = Integer.parseInt(staffSalary.getText());
            flag = true;
        }
        if (flag) {
            if (name.isEmpty()) name = selected.getFull_name();
            if (pos.isEmpty()) pos = selected.getPosition();
            if (adr.isEmpty()) adr = selected.getAddress();
            if (tell.isEmpty()) tell = selected.getTell();
            if (birD.isEmpty()) birD = String.valueOf(selected.getBirthday());
            if (salary == 0) salary = selected.getSalary();

            Staff staff = new Staff(staffId, name, pos, adr, tell, Date.valueOf(birD), selected.getBegin_date(), salary);

            staffObservableList.add(staffObservableList.indexOf(selected), staff);
            staffObservableList.remove(selected);
            new ConnectDB().updateStaffInfo(staff);
        }

    }
    public void backToHome (ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(new File("src/view/homePage.fxml").toURI().toURL());
        stage.setScene(new Scene(root));
        stage.show();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Format formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date curDate = Date.valueOf(LocalDate.now());
        today.setText(formatter.format(curDate));
        fullName.setText(user.getFull_name());
        ConnectDB connectDB = new ConnectDB();
        staffObservableList = FXCollections.observableArrayList(connectDB.getStaffList());
        staffTableView.setItems(staffObservableList);
        staffIDColumn.setCellValueFactory(new PropertyValueFactory<>("staffID"));
        staffNameColumn.setCellValueFactory(new PropertyValueFactory<>("full_name"));
        staffPositionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        staffSalaryColumn.setCellValueFactory(col -> {
            if (col.getValue().getSalary() > 0)
                return new ReadOnlyObjectWrapper<>(col.getValue().getSalary());
            else return null;
        });
    }
}