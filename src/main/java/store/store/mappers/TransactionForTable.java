package store.store.mappers;

import java.time.LocalDate;

public class TransactionForTable {
    private String client;
    private String shop;
    private String book;
    private LocalDate date;
    private Integer price;

    public TransactionForTable(String client, String shop, String book, LocalDate date, Integer price) {
        this.client = client;
        this.shop = shop;
        this.book = book;
        this.date = date;
        this.price = price;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
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
        return "TransactionForTable{" +
                "client='" + client + '\'' +
                ", shop='" + shop + '\'' +
                ", book='" + book + '\'' +
                ", date=" + date +
                ", price=" + price +
                '}';
    }
}
