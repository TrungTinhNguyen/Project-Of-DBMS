package Controller;

import Databases.ConnectDB;
import Modules.Account;
import Modules.Drinks;
import Modules.DrinksCategory;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class DrinksListController implements Initializable {

    private static Account user;

    @FXML private Label fullName;
    @FXML private Label today;

    @FXML private RadioButton categoryRadioBtn;
    @FXML private RadioButton drinksNameRadioBtn;

    @FXML private TextField drinksName;
    @FXML private TextField categoryName;

    @FXML private Button updateBtn;
    @FXML private Button deleteBtn;

    @FXML private TableView <Drinks> drinksTableView;
        @FXML private TableColumn<Drinks, Integer> drinksIDColumn;
        @FXML private TableColumn<Drinks, String> drinksNameColumn;
        @FXML private TableColumn<Drinks, String> drinksCategoryColumn;
        @FXML private TableColumn<Drinks, Float> drinksPriceColumn;

    private ObservableList<Drinks> drinksObservableList;
    private Drinks curDrinks;

    public void search () {
        ObservableList<Drinks> searchList = FXCollections.observableArrayList();
        if (categoryRadioBtn.isSelected()) {
            drinksObservableList.forEach(drinks -> {
                if (drinks.getCategoryName().equalsIgnoreCase(categoryName.getText()))
                    searchList.add(drinks);
            });
        } else {
            drinksObservableList.forEach(drinks -> {
                if (drinks.getName().equalsIgnoreCase(drinksName.getText()))
                    searchList.add(drinks);
            });
        }
        drinksTableView.setItems(searchList);
    }
    public void sortList (ObservableList<Drinks> oList) {
        oList.sort(Comparator.comparingInt(Drinks::getDrinksID));
    }
    public void refresh () {
        drinksObservableList = FXCollections.observableArrayList(new ConnectDB().getDrinksList());
        sortList(drinksObservableList);
        drinksTableView.setItems(drinksObservableList);
        updateBtn.setDisable(true);
        deleteBtn.setDisable(true);
    }
    public void addDrinks () {
        if (user.getAccount_type()==0) {
            ConnectDB connectDB = new ConnectDB();
            AtomicReference<Drinks> newDrinks = new AtomicReference<>();
            Dialog<Drinks> addDrinksDialog = new Dialog<>();
            GridPane gridPane = new GridPane();
            gridPane.setHgap(15);
            gridPane.setVgap(15);
            gridPane.setPadding(new Insets(10));

            TextField dname = new TextField();
            dname.setPromptText("Tên");
            dname.setPrefWidth(300);
            SplitMenuButton cname = new SplitMenuButton();
            cname.setText("Loại");
            cname.setPrefWidth(150);
            ObservableList<DrinksCategory> categories = FXCollections.observableArrayList(connectDB.getDrinksCategory());
            ObservableList<MenuItem> categoriesItem = FXCollections.observableArrayList();
            categories.forEach(e->{
                MenuItem item = new MenuItem();
                item.setText(e.getCategoryName());
                item.setOnAction(event-> cname.setText(item.getText()));
                categoriesItem.add(item);
            });
            cname.getItems().addAll(categoriesItem);
            TextField price = new TextField();
            price.setPromptText("Giá");
            price.setPrefWidth(300);

            gridPane.add(dname, 0, 0);
            gridPane.add(cname, 0, 1);
            gridPane.add(price, 0, 2);

            addDrinksDialog.getDialogPane().setContent(gridPane);

            ButtonType addOK = new ButtonType("Thêm", ButtonBar.ButtonData.OK_DONE);
            addDrinksDialog.getDialogPane().getButtonTypes().addAll(addOK, ButtonType.CANCEL);
            addDrinksDialog.setTitle("Thêm đồ uống");
            addDrinksDialog.setResultConverter(buttonType -> {
                if (buttonType == addOK){
                    AtomicInteger cateID = new AtomicInteger();
                    for (DrinksCategory e : categories)
                        if (e.getCategoryName().equals(cname.getText())){
                            cateID.set(e.getCategoryID());
                            break;
                        }
                    return new Drinks(cateID.get(), cname.getText(), drinksObservableList.size()+1, dname.getText(), Integer.parseInt(price.getText()));
                } return null;
            });
            Optional<Drinks> result = addDrinksDialog.showAndWait();
            result.ifPresent(drinks -> {
                if (drinks != null)
                    newDrinks.set(new Drinks(drinks));
            });
            if (newDrinks.get() != null) {
                Alert alert = new Alert(Alert.AlertType.NONE);
                if (!connectDB.addNewDrinks(newDrinks.get())){
                    alert.setTitle("Lỗi");
                    alert.setHeaderText("Lỗi cập nhật");
                    alert.setContentText("Vui lòng kiểm tra lại");
                    alert.setAlertType(Alert.AlertType.ERROR);
                } else {
                    alert.setTitle("Thành công");
                    alert.setHeaderText("Thành công");
                    alert.setContentText("Đã thêm");
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    refresh();
                }
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Cập Nhật Giá");
            alert.setHeaderText("Không có quyền truy cập");
            alert.setContentText("Chức năng này chỉ dành cho Quản lý");
            alert.show();
        }
    }
    public void updateDrinksInfo () {
        if (user.getAccount_type()==0) {
            Dialog<Float> updateDrinksPriceDialog = new Dialog<>();

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(30);
            gridPane.setPadding(new Insets(20, 10, 40, 10));

            Label dName = new Label(curDrinks.getName());
            dName.setPrefWidth(300);
            Label cName = new Label(curDrinks.getCategoryName());
            cName.setPrefWidth(300);
            TextField price = new TextField();
            price.setPrefWidth(300);
            price.setPromptText(String.valueOf(curDrinks.getPrice()));

            gridPane.add(dName, 0,0);
            gridPane.add(cName, 0, 1);
            gridPane.add(price, 0, 2);

            updateDrinksPriceDialog.setTitle("Cập nhật giá");
            updateDrinksPriceDialog.setHeaderText("Cập nhật giá cho đồ uống");
            updateDrinksPriceDialog.getDialogPane().setContent(gridPane);

            ButtonType doneButtonType = new ButtonType("Cập nhật", ButtonBar.ButtonData.OK_DONE);
            updateDrinksPriceDialog.getDialogPane().getButtonTypes().addAll(doneButtonType, ButtonType.CANCEL);
            Node doneButton = updateDrinksPriceDialog.getDialogPane().lookupButton(doneButtonType);
            price.textProperty().addListener((observable, oldValue, newValue) -> {
                doneButton.setDisable(false);
            });
            doneButton.setDisable(true);

            updateDrinksPriceDialog.setResultConverter(buttonType -> {
                if (buttonType == doneButtonType) {
                    return Float.parseFloat(price.getText());
                } return null;
            });
            Optional<Float> result = updateDrinksPriceDialog.showAndWait();
            result.ifPresent(newPrice -> {
                if (newPrice != null && newPrice != curDrinks.getPrice()) {
                    curDrinks.setPrice(newPrice);
                    new ConnectDB().updateDrinksPrice(curDrinks);
                    this.refresh();
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Cập Nhật Giá");
            alert.setHeaderText("Không có quyền truy cập");
            alert.setContentText("Chức năng này chỉ dành cho Quản lý");
            alert.show();
        }
    }
    public void deleteDrinks() {
        if (user.getAccount_type()==0) {
            new ConnectDB().deleteDrinks(curDrinks);
            drinksObservableList.remove(curDrinks);
            this.refresh();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xóa Đồ Uống");
            alert.setHeaderText("Không có quyền truy cập");
            alert.setContentText("Chức năng này chỉ dành cho Quản lý");
            alert.show();
        }

    }
    public void tableViewClicked (MouseEvent event) {
        Drinks selected = drinksTableView.getSelectionModel().getSelectedItem();
        updateBtn.setDisable(false);
        deleteBtn.setDisable(false);
        curDrinks = new Drinks(selected);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        user = Controller.getUser();
        HomeController.headerInit(user, fullName, today);
        this.refresh();

        categoryRadioBtn.setOnMouseClicked(event -> {
            categoryName.setDisable(false);
            drinksName.setDisable(true);
        });
        drinksNameRadioBtn.setOnMouseClicked(event -> {
            categoryName.setDisable(true);
            drinksName.setDisable(false);
        });

        drinksTableView.setRowFactory(tv -> {
            TableRow<Drinks> row = new TableRow<>();
            row.setOnMouseClicked(this::tableViewClicked);
            return row;
        });

        drinksIDColumn.setCellValueFactory(new PropertyValueFactory<>("drinksID"));
        drinksNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        drinksCategoryColumn.setCellValueFactory(col -> new ReadOnlyObjectWrapper<>(col.getValue().getCategoryName()));
        drinksPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    public void backToHome(ActionEvent event) throws IOException {
        new TableController().backToHome(event);
    }
}
