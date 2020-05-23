package store.store.contrtollers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import store.store.SceneManager;
import store.store.StoreApplication;
import store.store.mappers.ShopForTable;
import store.store.mappers.ShopForTableMapper;

public class ShopsController {

    @FXML
    private TableView<ShopForTable> shopTable;
    @FXML
    private TableColumn<ShopForTable, String> nameShopTable;
    @FXML
    private TableColumn<ShopForTable, String> cityShopTable;
    @FXML
    private TableColumn<ShopForTable, String> streetShopTable;
    @FXML
    private Label warningLabel;

    ShopForTableMapper booksForTableMapper;
    ObservableList<ShopForTable> observableListAllShops = FXCollections.observableArrayList();

    public static Long actualShopId = 0L;

    @FXML
    void goInButton(ActionEvent event) {
        if (shopTable.getSelectionModel().getSelectedItem() != null) {
            actualShopId = shopTable.getSelectionModel().getSelectedItem().getShopId();
            SceneManager.renderScene("mainPage");
        }else {
            warningLabel.setText("Wybierz sklep");
        }
    }


    @FXML
    void initialize() {
        booksForTableMapper = (ShopForTableMapper) StoreApplication.getSpringContext().getBean("shopForTableMapper");

        nameShopTable.setCellValueFactory(new PropertyValueFactory<>("name"));
        cityShopTable.setCellValueFactory(new PropertyValueFactory<>("city"));
        streetShopTable.setCellValueFactory(new PropertyValueFactory<>("street"));
        shopTable.getColumns().clear();
        shopTable.setItems(getObservableListAllShops());
        shopTable.getColumns().addAll(nameShopTable, cityShopTable, streetShopTable);

    }
    private ObservableList<ShopForTable> getObservableListAllShops() {
        this.observableListAllShops.addAll(booksForTableMapper.getShopsForTable());
        return this.observableListAllShops;
    }

}
