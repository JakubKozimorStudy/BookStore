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
import store.store.entity.Book;
import store.store.entity.Category;
import store.store.mappers.BooksForTable;
import store.store.mappers.BooksForTableMapper;
import store.store.services.BookServiceImpl;
import store.store.services.CategoryServiceImpl;

public class BooksController {

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
    @FXML
    private TextField priceInput;
    @FXML
    private TextField categoryInput;
    @FXML
    private TextField ageGroupInput;
    @FXML
    private TextField authorInput;
    @FXML
    private TextField titleInput;

    private BooksForTableMapper booksForTableMapper;
    ObservableList<BooksForTable> observableListAllBooks = FXCollections.observableArrayList();

    BookServiceImpl bookService;
    CategoryServiceImpl categoryService;

    @FXML
    void addNewBookButton(ActionEvent event) {
        Category newCategory = new Category();
        newCategory.setAgeGroup(ageGroupInput.getText());
        newCategory.setCategoryName(categoryInput.getText());
        categoryService.addCategory(newCategory);
        Book newBook = new Book();
        newBook.setAuthor(authorInput.getText());
        newBook.setCategory(newCategory);
        newBook.setPrice(Integer.valueOf(priceInput.getText()));
        newBook.setTitle(titleInput.getText());
        bookService.addBook(newBook);
        refreshTable();
    }

    @FXML
    void backButton(ActionEvent event) {
        SceneManager.renderScene("mainPage");
    }
    @FXML
    void initialize() {
        booksForTableMapper = (BooksForTableMapper) StoreApplication.getSpringContext().getBean("booksForTableMapper");
        bookService = (BookServiceImpl) StoreApplication.getSpringContext().getBean("bookServiceImpl");
        categoryService = (CategoryServiceImpl) StoreApplication.getSpringContext().getBean("categoryServiceImpl");

        titleBooksTable.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorBooksTable.setCellValueFactory(new PropertyValueFactory<>("author"));
        categoryBooksTable.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceBooksTable.setCellValueFactory(new PropertyValueFactory<>("price"));
        booksTable.getColumns().clear();
        booksTable.setItems(getObservableListAllBooks());
        booksTable.getColumns().addAll(titleBooksTable, authorBooksTable, categoryBooksTable, priceBooksTable);
    }

    public ObservableList<BooksForTable> getObservableListAllBooks() {
        this.observableListAllBooks.addAll(booksForTableMapper.getBooksForTable());
        return this.observableListAllBooks;
    }
    private void refreshTable() {
        this.observableListAllBooks = FXCollections.observableArrayList();
        booksTable.getColumns().clear();
        booksTable.setItems(getObservableListAllBooks());
        booksTable.getColumns().addAll(titleBooksTable, authorBooksTable, categoryBooksTable, priceBooksTable);
    }

}
