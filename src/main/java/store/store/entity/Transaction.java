package store.store.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "client_id")
    private Client client;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "shop_id")
    private Store store;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "book_id")
    private Book book;
    @Column(name = "transaction_date")
    private LocalDate date;
    @Column(name = "price")
    private Integer price;

    public Transaction() {
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", client=" + client +
                ", store=" + store +
                ", book=" + book +
                ", date=" + date +
                ", price=" + price +
                '}';
    }
}
