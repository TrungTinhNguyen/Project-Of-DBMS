package Controller;

import Databases.ConnectDB;
import Modules.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class TableController implements Initializable {

    private static Account user;

    @FXML private Label fullName;
    @FXML private Label today;

    @FXML private VBox infoPane;
    @FXML private Label tableName;
    @FXML private Label statusLbl;
    @FXML private Label sumLbl;


    @FXML private SplitMenuButton drinksSplitMenu;
    @FXML private SplitMenuButton countSplitMenu;

    @FXML private TableView <BillInfo> drinksOrderedTable;
    @FXML private TableColumn <BillInfo, Integer> columnID;
    @FXML private TableColumn <BillInfo, String> columnName;
    @FXML private TableColumn <BillInfo, Integer> columnCount;
    @FXML private TableColumn <BillInfo, Float> columnPrice;

    private ObservableList <Drinks> drinksList;
    private ObservableList <MenuItem> drinksItems;
    private ObservableList <MenuItem> countItems;
    private ObservableList <Table> tables;
    private ArrayList<ObservableList<BillInfo>> drinksOrderedList;

    private int drinksItemID;
    private int countDrinks;
    private int tableID;
    private static int[] drinksSelectedNumber;
    private static float[] totalPrices;
    public TableController() {
    }

    private void getDrinksItem (ActionEvent event) {
        MenuItem selected = (MenuItem) event.getSource();
        drinksSplitMenu.setText(selected.getText());
        drinksItemID = Integer.parseInt(selected.getId());
    }
    private void getCountItem (ActionEvent event){
        MenuItem selected = (MenuItem) event.getSource();
        countSplitMenu.setText(selected.getText());
        countDrinks = Integer.parseInt(selected.getText());
    }
    public void tablePressed(ActionEvent event) {
        Button button = (Button) event.getSource();
        tableID = Integer.parseInt(String.valueOf(button.getText().charAt(4)));
        tableName.setText(button.getText());
        statusLbl.setText( tables.get(tableID-1).getStatus() ? "Có Người" : "Trống");
        infoPane.setDisable(false);
        drinksOrderedTable.setItems(drinksOrderedList.get(tableID-1));
        sumLbl.textProperty().bind(new SimpleStringProperty(String.valueOf(totalPrices[tableID-1])));
    }

    public void addFood() {
        if (countDrinks > 0) {
            if (!tables.get(tableID-1).getStatus()) {
                tables.get(tableID-1).setStatus(true);
                statusLbl.setText("Có Người");
            }
            boolean flag = false;
            BillInfo drinks = new BillInfo(drinksSelectedNumber[tableID-1], drinksList.get(drinksItemID), countDrinks);
            for (BillInfo e : drinksOrderedList.get(tableID - 1)) {
                if (e.getDrinks().getDrinksID() == drinks.getDrinks().getDrinksID()) {
                    e.setAmountOf(e.getAmountOf() + drinks.getAmountOf());
                    flag = true;
                    break;
                }
            }
            if (!flag){
                drinksOrderedList.get(tableID-1).add(drinks);
                drinksSelectedNumber[tableID-1] ++;
            }
            totalPrices[tableID-1] += drinks.getDrinks().getPrice() * drinks.getAmountOf();
            sumLbl.textProperty().bind(new SimpleStringProperty(String.valueOf(totalPrices[tableID-1])));
            drinksOrderedTable.refresh();
        }
    }

    public void deleteTable() {
        drinksOrderedList.get(tableID-1).clear();
        totalPrices[tableID-1] = 0;
        sumLbl.textProperty().bind(new SimpleStringProperty(String.valueOf(totalPrices[tableID-1])));
    }

    public void backToHome(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(new File("src/view/homePage.fxml").toURI().toURL());
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void changeTable () {
        AtomicInteger changeIntoTableID = new AtomicInteger(tableID);
        Dialog <Integer> changeTableDialog = new Dialog<>();
        changeTableDialog.setTitle("Chuyển bàn");
        changeTableDialog.setHeaderText("Chuyển đến bàn");

        GridPane tableGrid = new GridPane();
        tableGrid.setVgap(10);
        tableGrid.setHgap(20);
        tableGrid.setPadding(new Insets(30, 20, 30, 20));

        SplitMenuButton tableSplit = new SplitMenuButton();
        tableSplit.setText("Chọn bàn");
        ObservableList <MenuItem> changeIntoTableList = FXCollections.observableArrayList();
        tables.forEach(table -> {
            if (table.getTableID() != tableID) {
                MenuItem item = new MenuItem();
                item.setId(String.valueOf(table.getTableID()));
                item.setText(table.getName());
                item.setOnAction((event) -> {
                    tableSplit.setText(item.getText());
                    tableSplit.setId(item.getId());
                });
                changeIntoTableList.add(item);
            }
        });
        tableSplit.getItems().addAll(changeIntoTableList);
        tableGrid.add(tableSplit, 0, 0);
        changeTableDialog.getDialogPane().setContent(tableGrid);

        ButtonType changeTo = new ButtonType("Chuyển", ButtonType.OK.getButtonData());
        changeTableDialog.getDialogPane().getButtonTypes().addAll(changeTo, new ButtonType("Hủy", ButtonType.CANCEL.getButtonData()));

        Node changeButton = changeTableDialog.getDialogPane().lookupButton(changeTo);
        changeButton.setDisable(true);
        tableSplit.textProperty().addListener((observable, oldValue, newValue) -> changeButton.setDisable(newValue.equals("Chọn bàn")));
        changeTableDialog.setResultConverter(clicked -> {
            if (clicked == changeTo)
                return Integer.parseInt(tableSplit.getId());
            return tableID;
        });

        Optional <Integer> intoTable = changeTableDialog.showAndWait();
        intoTable.ifPresent(changeIntoTableID::set);
        if (changeIntoTableID.get() != tableID) {
            ObservableList<BillInfo> curTableOrder = FXCollections.observableArrayList(drinksOrderedList.get(tableID-1));
            drinksOrderedList.set(changeIntoTableID.get()-1, curTableOrder);
            drinksOrderedList.get(tableID-1).clear();
            totalPrices[changeIntoTableID.get()-1] = totalPrices[tableID-1];
            totalPrices[tableID-1] = 0;
            sumLbl.textProperty().bind(new SimpleStringProperty(String.valueOf(totalPrices[tableID-1])));
        }

    }

    public void pay() {

        Bill bill = new Bill(Bill.getCount(), tables.get(tableID-1), user, drinksOrderedList.get(tableID-1), Date.valueOf(LocalDate.now()), true);
        bill.setListDrinks(drinksOrderedList.get(tableID-1));

        ConnectDB connectDB = new ConnectDB();
        connectDB.payment(bill);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thanh Toán");
        alert.setHeaderText(tables.get(tableID-1).getName());
        alert.setContentText(String.valueOf(totalPrices[tableID-1]));
        alert.show();

        tables.get(tableID-1).setStatus(false);
        statusLbl.setText("Trống");
        drinksOrderedList.get(tableID-1).clear();
        totalPrices[tableID-1] = 0;
        sumLbl.textProperty().bind(new SimpleStringProperty(String.valueOf(totalPrices[tableID-1])));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        user = Controller.getUser();
        HomeController.headerInit(user, fullName, today);
        ConnectDB connectDB = new ConnectDB();
        drinksList = FXCollections.observableArrayList(connectDB.getDrinksList());
        ObservableList<Integer> countList = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        drinksItems = FXCollections.observableArrayList();
        countItems = FXCollections.observableArrayList();
        Bill.setCount(connectDB.countBill()+1);
        tables = connectDB.getTableList();
        drinksOrderedList = new ArrayList<>();
        drinksSelectedNumber = new int[tables.size()];
        totalPrices = new float[tables.size()];
        for (int i = 0; i < tables.size(); i++) {
            drinksSelectedNumber[i] = 1;
            totalPrices[i] = 0;
            drinksOrderedList.add(FXCollections.observableArrayList());
        }

        infoPane.setDisable(true);

        drinksList.forEach(e -> {
            MenuItem item = new MenuItem();
            item.setText(e.getName());
            item.setId(String.valueOf(e.getDrinksID()-1));
            item.setOnAction(this::getDrinksItem);
            drinksItems.add(item);
        });
        drinksSplitMenu.getItems().addAll(drinksItems);
        drinksSplitMenu.setText(drinksItems.get(0).getText());
        countList.forEach(i -> {
            MenuItem item = new MenuItem();
            item.setText(String.valueOf(i));
            item.setOnAction(this::getCountItem);
            countItems.add(item);
        });
        countSplitMenu.getItems().addAll(countItems);
        columnID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        columnName.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getDrinks().getName()));
        columnCount.setCellValueFactory(new PropertyValueFactory<>("amountOf"));
        columnPrice.setCellValueFactory(col -> new ReadOnlyObjectWrapper<>(col.getValue().getDrinks().getPrice()));
    }
}
