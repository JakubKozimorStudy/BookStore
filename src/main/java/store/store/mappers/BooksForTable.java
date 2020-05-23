package store.store.mappers;


public class BooksForTable {
    private Long bookId;
    private String title;
    private String author;
    private String name;
    private Integer price;

    public BooksForTable(Long bookId, String title, String author, String name, Integer price) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.name = name;
        this.price = price;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "BooksForTable{" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
