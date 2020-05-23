package store.store.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import store.store.entity.Book;
import store.store.entity.Client;
import store.store.repository.TransactionRepository;

import java.util.List;

@Service
public class TransactionServiceImpl {

    TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void addToTransaction(List<Book> bookList, Client client) {

    }
}
