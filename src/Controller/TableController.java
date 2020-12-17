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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
import java.util.ResourceBundle;

public class TableController implements Initializable {

    private static final Staff user = Controller.getUser();

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
        statusLbl.setText((tables.get(tableID-1).isStatus()) ? "Có Người" : "Trống");
        infoPane.setDisable(false);
        drinksOrderedTable.setItems(drinksOrderedList.get(tableID-1));
        sumLbl.textProperty().bind(new SimpleStringProperty(String.valueOf(totalPrices[tableID-1])));
    }

    public void addFood() {
        if (!tables.get(tableID-1).isStatus()) {
            tables.get(tableID-1).setStatus(true);
            statusLbl.setText("Có Người");
        }
        BillInfo drinks = new BillInfo(drinksSelectedNumber[tableID-1], drinksList.get(drinksItemID), countDrinks);
        drinksOrderedList.get(tableID-1).add(drinks);
        drinksSelectedNumber[tableID-1] ++;
        totalPrices[tableID-1] += drinks.getDrinks().getPrice() * drinks.getAmountOf();
        sumLbl.textProperty().bind(new SimpleStringProperty(String.valueOf(totalPrices[tableID-1])));
    }

    public void deleteTable() {
        drinksOrderedList.get(tableID-1).remove(0, drinksSelectedNumber[tableID-1]-1);
        totalPrices[tableID-1] = 0;
        sumLbl.textProperty().bind(new SimpleStringProperty(String.valueOf(totalPrices[tableID-1])));
    }

    public void backToHome(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(new File("src/view/homePage.fxml").toURI().toURL());
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void pay() {
        Bill bill = new Bill(Bill.getCount(), tables.get(tableID-1), user, Date.valueOf(LocalDate.now()));
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
        drinksOrderedList.get(tableID-1).remove(0, drinksOrderedList.get(tableID-1).size());
        totalPrices[tableID-1] = 0;
        sumLbl.textProperty().bind(new SimpleStringProperty(String.valueOf(totalPrices[tableID-1])));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Format formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date curDate = Date.valueOf(LocalDate.now());
        today.setText(formatter.format(curDate));
        fullName.setText(user.getFull_name());
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
        }
        for (int i = 0; i < tables.size(); i++)
            drinksOrderedList.add(FXCollections.observableArrayList());

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
