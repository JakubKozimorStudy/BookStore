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
import store.store.mappers.BooksForTable;
import store.store.mappers.BooksForTableMapper;
import store.store.services.BookServiceImpl;

public class MostPopularBooksController {

    @FXML
    private TableView<BooksForTable> booksTable;
    @FXML
    private TableColumn<BooksForTable, String> titleBooksTable;
    @FXML
    private TableColumn<BooksForTable, String> authorBooksTable;
    @FXML
    private TableColumn<BooksForTable, String> categoryBooksTable;
    @FXML
    private TableColumn<BooksForTable, Integer> priceBooksTable;

    private BooksForTableMapper booksForTableMapper;
    private BookServiceImpl bookService;
    ObservableList<BooksForTable> observableListAllBooks = FXCollections.observableArrayList();

    @FXML
    void backButton(ActionEvent event) {
        SceneManager.renderScene("transactionPage");
    }

    @FXML
    void initialize() {
        bookService = (BookServiceImpl) StoreApplication.getSpringContext().getBean("bookServiceImpl");
        booksForTableMapper = (BooksForTableMapper) StoreApplication.getSpringContext().getBean("booksForTableMapper");

        titleBooksTable.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorBooksTable.setCellValueFactory(new PropertyValueFactory<>("author"));
        categoryBooksTable.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceBooksTable.setCellValueFactory(new PropertyValueFactory<>("price"));
        booksTable.getColumns().clear();
        booksTable.setItems(getObservableListAllBooks());
        booksTable.getColumns().addAll(titleBooksTable, authorBooksTable, categoryBooksTable, priceBooksTable);
    }

    public ObservableList<BooksForTable> getObservableListAllBooks() {
        this.observableListAllBooks.addAll(booksForTableMapper.getMostPopularBooks());
        return this.observableListAllBooks;
    }
}
