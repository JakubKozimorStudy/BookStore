package store.store.contrtollers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import store.store.SceneManager;
import store.store.StoreApplication;
import store.store.mappers.ShopForTable;
import store.store.mappers.ShopForTableMapper;
import store.store.mappers.TransactionForTable;
import store.store.mappers.TransactionForTableMapper;

import java.time.LocalDate;

public class TransactionsController {

    @FXML
    private TableView<TransactionForTable> transactionTable;
    @FXML
    private TableColumn<TransactionForTable, String> clientTransactionTable;
    @FXML
    private TableColumn<TransactionForTable, String> shopTransactionTable;
    @FXML
    private TableColumn<TransactionForTable, String> bookTransactionTable;
    @FXML
    private TableColumn<TransactionForTable, LocalDate> dateTransactionTable;
    @FXML
    private TableColumn<TransactionForTable, Integer> priceTransactionTable;


    TransactionForTableMapper transactionForTableMapper;
    ObservableList<TransactionForTable> observableListAllTransactions = FXCollections.observableArrayList();

    @FXML
    void backButton(ActionEvent event) {
        SceneManager.renderScene("mainPage");
    }

    @FXML
    void initialize() {
        transactionForTableMapper = (TransactionForTableMapper) StoreApplication.getSpringContext().getBean("transactionForTableMapper");

        clientTransactionTable.setCellValueFactory(new PropertyValueFactory<>("client"));
        shopTransactionTable.setCellValueFactory(new PropertyValueFactory<>("shop"));
        bookTransactionTable.setCellValueFactory(new PropertyValueFactory<>("book"));
        dateTransactionTable.setCellValueFactory(new PropertyValueFactory<>("date"));
        priceTransactionTable.setCellValueFactory(new PropertyValueFactory<>("price"));
        transactionTable.getColumns().clear();
        transactionTable.setItems(getObservableListAllTransactions());
        transactionTable.getColumns().addAll(clientTransactionTable, shopTransactionTable, bookTransactionTable, dateTransactionTable, priceTransactionTable);

    }

    private ObservableList<TransactionForTable> getObservableListAllTransactions() {
        this.observableListAllTransactions.addAll(transactionForTableMapper.getTransactionsForTable());
        return this.observableListAllTransactions;
    }

}
