package store.store.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import store.store.entity.Book;
import store.store.podstawy.BasicTools;
import store.store.repository.BookRepository;
import store.store.repository.TransactionRepository;
import weka.associations.Apriori;
import weka.associations.AssociationRule;
import weka.associations.AssociationRules;
import weka.associations.Item;
import weka.core.Instances;
import weka.core.Utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl {

    BookRepository bookRepository;
    TransactionRepository transactionRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, TransactionRepository transactionRepository) {
        this.bookRepository = bookRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public void addBook(Book newBook) {
        bookRepository.save(newBook);
    }

    public List<Book> getMostPopularBooks() {
        return bookRepository.findAll()
                .stream()
                .filter(book ->
                        transactionRepository.findAll()
                                .stream()
                                .filter(transaction ->
                                        transaction.getBook().getBookId().equals(book.getBookId()))
                                .count() > 3L
                )
                .collect(Collectors.toList());
    }

}
