package store.store.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import store.store.contrtollers.ShopsController;
import store.store.entity.Book;
import store.store.entity.Client;
import store.store.entity.Store;
import store.store.entity.Transaction;
import store.store.repository.StoreRepository;
import store.store.repository.TransactionRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl {

    TransactionRepository transactionRepository;
    StoreRepository storeRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, StoreRepository storeRepository) {
        this.transactionRepository = transactionRepository;
        this.storeRepository = storeRepository;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public void addToTransaction(List<Book> bookList, Client client) {
        Optional<Store> actualShop = storeRepository.findById(ShopsController.actualShopId);
        actualShop.ifPresent(store -> {
            bookList.forEach(book -> {
                Transaction newTransaction = new Transaction();
                newTransaction.setBook(book);
                newTransaction.setClient(client);
                newTransaction.setStore(store);
                newTransaction.setDate(LocalDate.now());
                newTransaction.setPrice(book.getPrice());
                transactionRepository.save(newTransaction);
            });
        });
    }

    public List<Transaction> getByMonth() {
        return transactionRepository.findAll()
                .stream()
                .filter(transaction -> transaction.getDate().getMonth().equals(LocalDate.now().getMonth()))
                .collect(Collectors.toList());
    }
}
