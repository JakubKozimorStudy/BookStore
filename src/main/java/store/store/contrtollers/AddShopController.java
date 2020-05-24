package store.store.contrtollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import store.store.SceneManager;
import store.store.StoreApplication;
import store.store.entity.Address;
import store.store.entity.Store;
import store.store.services.AddressServiceImpl;
import store.store.services.ClientServiceImpl;
import store.store.services.ShopServiceImpl;

public class AddShopController {

    @FXML
    private TextField street;
    @FXML
    private TextField shopName;
    @FXML
    private TextField city;
    @FXML
    private TextField zipCode;
    @FXML
    private Label infoLabel;

    AddressServiceImpl addressService;
    ShopServiceImpl storeService;

    @FXML
    void addShopButton(ActionEvent event) {
        Address newAddress = new Address();
        newAddress.setCity(city.getText());
        newAddress.setStreet(street.getText());
        newAddress.setZipCode(zipCode.getText());
        addressService.addNewAddress(newAddress);
        Store newStore = new Store();
        newStore.setAddress(newAddress);
        newStore.setShopName(shopName.getText());
        storeService.addNewStore(newStore);
        infoLabel.setText("Dodano");
    }

    @FXML
    void backButton(ActionEvent event) {
        SceneManager.renderScene("shopsPanel");
    }

    @FXML
    void initialize() {
        addressService = (AddressServiceImpl) StoreApplication.getSpringContext().getBean("addressServiceImpl");
        storeService = (ShopServiceImpl) StoreApplication.getSpringContext().getBean("shopServiceImpl");
    }
}
