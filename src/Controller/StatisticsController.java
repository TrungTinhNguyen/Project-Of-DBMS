package Controller;

import Databases.ConnectDB;
import Modules.Account;
import Modules.Bill;
import Modules.Staff;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class StatisticsController implements Initializable {

    private static Account user;

    @FXML private Label fullName;
    @FXML private Label today;
    @FXML private TextField staffName;

    @FXML private RadioButton findByStaff;
    @FXML private RadioButton findByDate;

    @FXML private SplitMenuButton yearChoose;
    @FXML private SplitMenuButton monthChoose;
    @FXML private SplitMenuButton dayChoose;

    @FXML private TableView <Bill> statisticsTable;
        @FXML private TableColumn <Bill, Integer> statisticsIDColumn;
        @FXML private TableColumn <Bill, String> dateColumn;
        @FXML private TableColumn <Bill, String> staffColumn;
        @FXML private TableColumn <Bill, Float> statisticsPricesColumn;

    private ObservableList<Bill> statisticsList;

    private ObservableList<MenuItem> dayList;

    public void setDayList () {
        dayChoose.setDisable(false);
        int[] dayOfMonth = {0,31, 28,31,30,31,30,31,31,30,31,30,31};
        try {
            int year = Integer.parseInt(yearChoose.getText());
            int month = Integer.parseInt(monthChoose.getText());
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
                dayOfMonth[2] = 29;

            for (int i = 1; i <= dayOfMonth[month]; i++) {
                MenuItem item = new MenuItem(String.valueOf(i));
                item.setOnAction(e->dayChoose.setText(item.getText()));
                dayList.add(item);
            }
           dayChoose.getItems().clear();
            dayChoose.getItems().addAll(dayList);
        }catch (NumberFormatException e) {}
    }
    @Deprecated
    public void search() {
        ObservableList<Bill> searchList = FXCollections.observableArrayList();
        if (findByStaff.isSelected()) {
            statisticsList.forEach(bill -> {
                if (bill.getStaff().getFull_name().equals(staffName.getText()))
                    searchList.add(bill);
            });
        } else {
            int year = Integer.parseInt(yearChoose.getText());
            int month = Integer.parseInt(monthChoose.getText());
            int day = Integer.parseInt(dayChoose.getText());
            statisticsList.forEach(bill -> {
                if (bill.getDateCheckin().getYear()+1900 == year)
                    if (bill.getDateCheckin().getMonth()+1 == month)
                        if (bill.getDateCheckin().getDate() == day)
                            searchList.add(bill);
            });
        }
        statisticsTable.setItems(searchList);
    }
    public void tableViewDoubleClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            TableRow<Bill> rowSelected = (TableRow<Bill>) event.getSource();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Hoá đơn "+rowSelected.getItem().getBillID());
            alert.setHeaderText("Ngày: "+rowSelected.getItem().getDateCheckin()+"\nNhân viên: "+rowSelected.getItem().getStaff().getFull_name());
            GridPane gridBill = new GridPane();
            gridBill.setPadding(new Insets(20));
            gridBill.setVgap(10);
            gridBill.setHgap(10);

            Label id = new Label("STT");
            Label drinksName = new Label("Tên");
            drinksName.setPrefWidth(300);
            Label count = new Label("Số lượng");
            Label price = new Label ("Đơn giá");
            gridBill.add(id, 0,0);
            gridBill.add(drinksName, 1, 0);
            gridBill.add(count, 2, 0);
            gridBill.add(price, 3, 0);
            AtomicInteger j = new AtomicInteger(1);
            AtomicReference<Float> sum = new AtomicReference<>((float) 0);
            rowSelected.getItem().getListDrinks().forEach(e -> {
                Label i = new Label(String.valueOf(j.getAndIncrement()));
                Label name = new Label(e.getDrinks().getName());
                name.setPrefWidth(300);
                Label c = new Label(String.valueOf(e.getAmountOf()));
                Label p = new Label (String.valueOf(e.getDrinks().getPrice()));
                gridBill.add(i, 0, j.get()+1);
                gridBill.add(name, 1, j.get()+1);
                gridBill.add(c, 2, j.get()+1);
                gridBill.add(p, 3, j.get()+1);
                sum.updateAndGet(v -> (v + e.getAmountOf() * e.getDrinks().getPrice()));
            });
            gridBill.add(new Label("Tổng"),2, j.get()+2);
            gridBill.add(new Label(String.valueOf(sum.get())), 3, j.get()+2);
            alert.getDialogPane().setContent(gridBill);
            alert.show();
        }
    }

    public void backToHome(ActionEvent event) throws IOException {
        new TableController().backToHome(event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        user = Controller.getUser();
        HomeController.headerInit(user, fullName, today);
        ObservableList<MenuItem> yearList = FXCollections.observableArrayList();
        ObservableList<MenuItem> monthList = FXCollections.observableArrayList();
        dayList = FXCollections.observableArrayList();
        for (int i = 2019; i <= LocalDate.now().getYear(); i++) {
            MenuItem item = new MenuItem(String.valueOf(i));
            item.setOnAction(e -> yearChoose.setText(item.getText()));
            yearList.add(item);
        }

        for (int i = 1; i<= 12; i++){
            MenuItem item = new MenuItem(String.valueOf(i));
            item.setOnAction(e -> monthChoose.setText(item.getText()));
            monthList.add(item);
        }

        staffName.setDisable(false);
        yearChoose.setDisable(true);
        monthChoose.setDisable(true);

        findByDate.setOnMouseClicked(e -> {
                yearChoose.setDisable(false);
                monthChoose.setDisable(false);
                dayChoose.setDisable(false);
                staffName.setText("");
                staffName.setDisable(true);
        });

        findByStaff.setOnMouseClicked(e -> {
           staffName.setDisable(false);
            yearChoose.setDisable(true);
            monthChoose.setDisable(true);
            dayChoose.setDisable(true);
        });

        yearChoose.getItems().addAll(yearList);
        monthChoose.getItems().addAll(monthList);
        yearChoose.textProperty().addListener((observable, oldValue, newValue) -> setDayList());
        monthChoose.textProperty().addListener((observable, oldValue, newValue) -> setDayList());
        statisticsList = FXCollections.observableArrayList ( new ConnectDB().getStatistics());
        statisticsTable.setItems(statisticsList);
        statisticsTable.setRowFactory(tv -> {
            TableRow<Bill> row = new TableRow<>();
            row.setOnMouseClicked(this::tableViewDoubleClicked);
            return row;
        });

        statisticsIDColumn.setCellValueFactory(new PropertyValueFactory<>("billID"));
        dateColumn.setCellValueFactory(col -> new ReadOnlyObjectWrapper<>(String.valueOf(col.getValue().getDateCheckin())));
        staffColumn.setCellValueFactory(col-> new ReadOnlyObjectWrapper<>(col.getValue().getStaff().getFull_name()));
        statisticsPricesColumn.setCellValueFactory(col -> {
            AtomicReference<Float> sum = new AtomicReference<>((float)0);
            col.getValue().getListDrinks().forEach(drink -> sum.updateAndGet(v -> (v + drink.getAmountOf() * drink.getDrinks().getPrice())));
            return new ReadOnlyObjectWrapper<>(sum.get());
        });
    }
}