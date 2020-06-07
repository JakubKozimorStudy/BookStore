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
import store.store.entity.Book;
import store.store.entity.Client;
import store.store.mappers.BooksForTable;
import store.store.mappers.BooksForTableMapper;
import store.store.podstawy.BasicTools;
import store.store.services.BookServiceImpl;
import store.store.services.ClientServiceImpl;
import store.store.services.TransactionServiceImpl;
import weka.associations.Apriori;
import weka.associations.AssociationRule;
import weka.associations.AssociationRules;
import weka.associations.Item;
import weka.core.Instances;
import weka.core.Utils;

import java.util.*;


public class MainController {
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
    private TableView<Client> clientsTable;
    @FXML
    private TableColumn<Client, String> firstNameClientTable;
    @FXML
    private TableColumn<Client, String> lastNameClientTable;
    @FXML
    private TableColumn<Client, String> emailClientTable;
    @FXML
    private TableColumn<Client, Integer> phoneNumberClientTable;
    @FXML
    private TableView<Book> billTable;
    @FXML
    private TableColumn<Book, String> titleBillTable;
    @FXML
    private TableColumn<Book, String> authorBillTable;
    @FXML
    private TableColumn<Book, String> priceBillTable;

    @FXML
    private Label sum;
    @FXML
    private Label nextBook;

    StringBuilder recommendedBook = new StringBuilder();

    private ClientServiceImpl clientService;
    private BooksForTableMapper booksForTableMapper;
    private BookServiceImpl bookService;
    private TransactionServiceImpl transactionService;

    ObservableList<BooksForTable> observableListAllBooks = FXCollections.observableArrayList();
    ObservableList<Client> observableListAllClients = FXCollections.observableArrayList();
    ObservableList<Book> observableListBillBooks = FXCollections.observableArrayList();
    List<Book> billList = new ArrayList<>();

    @FXML
    void addToBill(ActionEvent event) throws Exception {
        if (booksTable.getSelectionModel().getSelectedItem() != null) {
            Optional<Book> bookOptional = bookService.getAllBooks().stream()
                    .filter(book -> book.getBookId().equals(booksTable.getSelectionModel().getSelectedItem().getBookId()))
                    .findFirst();
            bookOptional.ifPresent(book -> billList.add(book));
        }
        refreshBillTable();
        refreshBillSum();
        regulyAsocjacyjne();
    }

    @FXML
    void deleteFromBill(ActionEvent event) {
        billList.remove(billTable.getSelectionModel().getSelectedItem());
        refreshBillTable();
        refreshBillSum();
    }

    @FXML
    void acceptPayment(ActionEvent event) {
        if (clientsTable.getSelectionModel().getSelectedItem() != null) {
            transactionService.addToTransaction(billList, clientsTable.getSelectionModel().getSelectedItem());
            billList = new ArrayList<>();
            refreshBillTable();
            refreshBillSum();
        }
    }

    @FXML
    void transactionButton(ActionEvent event) {
        SceneManager.renderScene("transactionPage");
    }

    @FXML
    void booksButton(ActionEvent event) {
        SceneManager.renderScene("bookPage");
    }

    @FXML
    void clientsButton(ActionEvent event) {
        SceneManager.renderScene("clientsPage");
    }
    public void regulyAsocjacyjne()
            throws Exception
    {
        Instances data = BasicTools.loadData("./src/main/java/store/store/podstawy/data/ksiazki.arff");
        data.setClassIndex(data.numAttributes() - 1);

        //Opcje liczenia regul asocjacyjnych
        //-N ->Liczba regul do policzenia (standardowo: 10)
        //-C ->Minmalna ufnosc reguly (standardowo: 0.9).
        String[] options = Utils.splitOptions("-N 5 -C 0.6");
        Apriori apriori = new Apriori();
        apriori.setOptions(options);
        apriori.buildAssociations(data); //Generowanie regul asocjacyjnych

        System.out.println("Liczba regul=" + apriori.getNumRules());


        //===== POBRANIE INFORMACJI O REGULACH ========

        AssociationRules rules = apriori.getAssociationRules();
        List<AssociationRule> ruleList  = rules.getRules();

        for (int i=0; i<ruleList.size(); i++)
        {
            AssociationRule rule = ruleList.get(i); //Pobranie pojedynczej reguly


            //Pobranie opisu poprzednika reguly
            Collection<Item> poprzednik = rule.getPremise();
            Iterator<Item> iteratorPoprzednik = poprzednik.iterator();
            String poprzednikText = new String();
            while (iteratorPoprzednik.hasNext())
            {
                poprzednikText = poprzednikText + "("+iteratorPoprzednik.next().toString()+")";
                if (iteratorPoprzednik.hasNext()) poprzednikText = poprzednikText +"&";
            }


            //Pobranie opisu nastepnika reguly
            Collection<Item> nastepnik = rule.getConsequence();
            Iterator<Item> iteratorNastepnik = nastepnik.iterator();
            String nastepnikText = new String();
            while (iteratorNastepnik.hasNext())
            {
                nastepnikText = nastepnikText + "("+iteratorNastepnik.next().toString()+")";
                if (iteratorNastepnik.hasNext()) nastepnikText = nastepnikText +"&";
            }


            //Pobranie wsparcie i obliczenia ufnosci
            int wsparciePoprzednika = rule.getPremiseSupport();
            int wsparcieCalosci = rule.getTotalSupport();
            double ufnosc = (double)wsparcieCalosci/wsparciePoprzednika;

            recommendedBook.append(poprzednikText);
            recommendedBook.append("=>");
            recommendedBook.append(nastepnikText);
            recommendedBook.append(", ");
            recommendedBook.append("Wsparcie:");
            recommendedBook.append(wsparcieCalosci);
            recommendedBook.append(", ");
            recommendedBook.append("Ufnosc:");
            recommendedBook.append(ufnosc);
            recommendedBook.append("\n");
            System.out.print(poprzednikText+"=>"+nastepnikText+", ");
            System.out.print("Wsparcie:"+wsparcieCalosci+", ");
            System.out.println("Ufnosc:"+ufnosc);

        }

        nextBook.setText(recommendedBook.toString());

        //To jest niezbyt czytelne, ale wiarygodne
        //System.out.println(apriori.toString()); //Wypisanie informacji o regulach
    }
    @FXML
    void initialize() {
        clientService = (ClientServiceImpl) StoreApplication.getSpringContext().getBean("clientServiceImpl");
        bookService = (BookServiceImpl) StoreApplication.getSpringContext().getBean("bookServiceImpl");
        booksForTableMapper = (BooksForTableMapper) StoreApplication.getSpringContext().getBean("booksForTableMapper");
        transactionService = (TransactionServiceImpl) StoreApplication.getSpringContext().getBean("transactionServiceImpl");

        sum.setText("0");

        titleBooksTable.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorBooksTable.setCellValueFactory(new PropertyValueFactory<>("author"));
        categoryBooksTable.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceBooksTable.setCellValueFactory(new PropertyValueFactory<>("price"));
        booksTable.getColumns().clear();
        booksTable.setItems(getObservableListAllBooks());
        booksTable.getColumns().addAll(titleBooksTable, authorBooksTable, categoryBooksTable, priceBooksTable);

        firstNameClientTable.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameClientTable.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailClientTable.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneNumberClientTable.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        clientsTable.getColumns().clear();
        clientsTable.setItems(getObservableListAllClients());
        clientsTable.getColumns().addAll(firstNameClientTable, lastNameClientTable, emailClientTable, phoneNumberClientTable);

        titleBillTable.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorBillTable.setCellValueFactory(new PropertyValueFactory<>("author"));
        priceBillTable.setCellValueFactory(new PropertyValueFactory<>("price"));
        billTable.getColumns().clear();
        billTable.setItems(getObservableListBillBooks());
        billTable.getColumns().addAll(titleBillTable, authorBillTable, priceBillTable);
    }
    private void refreshBillSum() {
        int billSum = billList.stream()
                .map(Book::getPrice)
                .mapToInt(Integer::intValue)
                .sum();
        sum.setText(String.valueOf(billSum));
    }

    private void refreshBillTable() {
        this.observableListBillBooks = FXCollections.observableArrayList();
        billTable.getColumns().clear();
        billTable.setItems(getObservableListBillBooks());
        billTable.getColumns().addAll(titleBillTable, authorBillTable, priceBillTable);
    }

    public ObservableList<BooksForTable> getObservableListAllBooks() {
        this.observableListAllBooks.addAll(booksForTableMapper.getBooksForTable());
        return this.observableListAllBooks;
    }

    public ObservableList<Book> getObservableListBillBooks() {
        this.observableListBillBooks.addAll(billList);
        return this.observableListBillBooks;
    }

    public ObservableList<Client> getObservableListAllClients() {
        this.observableListAllClients.addAll(clientService.getAllClients());
        return this.observableListAllClients;
    }
}
