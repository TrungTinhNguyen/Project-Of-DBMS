package Controller;

import Databases.ConnectDB;
import Modules.Account;
import Modules.Bill;
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

    private static final Account user = Controller.getUser();

    @FXML private Label fullName;
    @FXML private Label today;
    @FXML private TextField staffName;

    @FXML private TableView <Bill> statisticsTable;
        @FXML private TableColumn <Bill, Integer> statisticsIDColumn;
        @FXML private TableColumn <Bill, String> dateColumn;
        @FXML private TableColumn <Bill, String> staffColumn;
        @FXML private TableColumn <Bill, Float> statisticsPricesColumn;

    private ObservableList<Bill> statisticsList;
    public void search() {
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
                sum.updateAndGet(v -> new Float((float) (v + e.getAmountOf() * e.getDrinks().getPrice())));
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
        fullName.setText(user.getFull_name());
        today.setText(new SimpleDateFormat("dd/MM/yyyy").format(Date.valueOf(LocalDate.now())));

        statisticsList = FXCollections.observableArrayList ( new ConnectDB().getStatistics());
        statisticsTable.setItems(statisticsList);
        statisticsTable.setRowFactory(tv -> {
            TableRow<Bill> row = new TableRow<>();
            row.setOnMouseClicked(event -> tableViewDoubleClicked(event));
            return row;
        });

        statisticsIDColumn.setCellValueFactory(new PropertyValueFactory<>("billID"));
        dateColumn.setCellValueFactory(col -> {
            return new ReadOnlyObjectWrapper<>(String.valueOf(col.getValue().getDateCheckin()));
        });
        staffColumn.setCellValueFactory(col->{
            return new ReadOnlyObjectWrapper<>(col.getValue().getStaff().getFull_name());
        });
        statisticsPricesColumn.setCellValueFactory(col -> {
            AtomicReference<Float> sum = new AtomicReference<>((float)0);
            col.getValue().getListDrinks().forEach(drink -> {
                sum.updateAndGet(v -> new Float((v + drink.getAmountOf() * drink.getDrinks().getPrice())));
            });
            return new ReadOnlyObjectWrapper<>(sum.get());
        });
    }
}
