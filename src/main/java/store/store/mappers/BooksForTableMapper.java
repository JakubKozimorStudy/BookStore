package store.store.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import store.store.services.BookServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BooksForTableMapper {

    BookServiceImpl bookService;

    @Autowired
    public BooksForTableMapper(BookServiceImpl bookService) {
        this.bookService = bookService;
    }

    public List<BooksForTable> getBooksForTable() {
        return bookService.getAllBooks().stream()
                .map(book ->
                        new BooksForTable(book.getBookId(), book.getTitle(), book.getAuthor(), book.getCategory().getCategoryName(), book.getPrice()))
                .collect(Collectors.toList());
    }

    public List<BooksForTable> getMostPopularBooks() {
        return bookService.getMostPopularBooks().stream()
                .map(book ->
                        new BooksForTable(book.getBookId(), book.getTitle(), book.getAuthor(), book.getCategory().getCategoryName(), book.getPrice()))
                .collect(Collectors.toList());
    }
}
