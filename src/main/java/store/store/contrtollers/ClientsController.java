package store.store.contrtollers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import store.store.SceneManager;
import store.store.StoreApplication;
import store.store.entity.Address;
import store.store.entity.Book;
import store.store.entity.Client;
import store.store.mappers.BooksForTable;
import store.store.mappers.ClientForTableMapper;
import store.store.mappers.ClientsForTable;
import store.store.services.AddressServiceImpl;
import store.store.services.ClientServiceImpl;
import store.store.services.ShopServiceImpl;

public class ClientsController {
    @FXML
    private TableView<ClientsForTable> clientsTable;
    @FXML
    private TableColumn<ClientsForTable, String> firstNameClientsTable;
    @FXML
    private TableColumn<ClientsForTable, String> lastNameClientsTable;
    @FXML
    private TableColumn<ClientsForTable, Integer> phoneNumberClientsTable;
    @FXML
    private TableColumn<ClientsForTable, String> emailClientsTable;
    @FXML
    private TableColumn<ClientsForTable, String> streetClientsTable;
    @FXML
    private TableColumn<ClientsForTable, String> cityClientsTable;
    @FXML
    private TableColumn<ClientsForTable, String> zipCodeClientsTable;
    @FXML
    private TextField streetInput;
    @FXML
    private TextField phoneNumberInput;
    @FXML
    private TextField emailInput;
    @FXML
    private TextField lastNameInput;
    @FXML
    private TextField firstNameInput;
    @FXML
    private TextField cityInput;
    @FXML
    private TextField zipCodeInput;

    AddressServiceImpl addressService;
    ClientServiceImpl clientService;
    ClientForTableMapper clientForTableMapper;

    ObservableList<ClientsForTable> observableListClients = FXCollections.observableArrayList();

    @FXML
    void addClientButton(ActionEvent event) {
        Address newAddress = new Address();
        newAddress.setCity(cityInput.getText());
        newAddress.setStreet(streetInput.getText());
        newAddress.setZipCode(zipCodeInput.getText());
        addressService.addNewAddress(newAddress);
        Client newClient = new Client();
        newClient.setAddressId(newAddress);
        newClient.setEmail(emailInput.getText());
        newClient.setFirstName(firstNameInput.getText());
        newClient.setLastName(lastNameInput.getText());
        newClient.setPhoneNumber(Integer.valueOf(phoneNumberInput.getText()));
        clientService.addClient(newClient);
        refreshTable();
    }

    @FXML
    void backButton(ActionEvent event) {
        SceneManager.renderScene("mainPage");
    }

    @FXML
    void initialize() {
        addressService = (AddressServiceImpl) StoreApplication.getSpringContext().getBean("addressServiceImpl");
        clientService = (ClientServiceImpl) StoreApplication.getSpringContext().getBean("clientServiceImpl");
        clientForTableMapper = (ClientForTableMapper) StoreApplication.getSpringContext().getBean("clientForTableMapper");

        firstNameClientsTable.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameClientsTable.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        phoneNumberClientsTable.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        emailClientsTable.setCellValueFactory(new PropertyValueFactory<>("email"));
        streetClientsTable.setCellValueFactory(new PropertyValueFactory<>("street"));
        cityClientsTable.setCellValueFactory(new PropertyValueFactory<>("city"));
        zipCodeClientsTable.setCellValueFactory(new PropertyValueFactory<>("zipCode"));
        clientsTable.getColumns().clear();
        clientsTable.setItems(getObservableListAllClients());
        clientsTable.getColumns().addAll(firstNameClientsTable, lastNameClientsTable, phoneNumberClientsTable, emailClientsTable,streetClientsTable,cityClientsTable,zipCodeClientsTable);

    }
    public ObservableList<ClientsForTable> getObservableListAllClients() {
        this.observableListClients.addAll(clientForTableMapper.getClientsForTable());
        return this.observableListClients;
    }

    private void refreshTable() {
        this.observableListClients = FXCollections.observableArrayList();
        clientsTable.getColumns().clear();
        clientsTable.setItems(getObservableListAllClients());
        clientsTable.getColumns().addAll(firstNameClientsTable, lastNameClientsTable, phoneNumberClientsTable, emailClientsTable,streetClientsTable,cityClientsTable,zipCodeClientsTable);
    }
}
