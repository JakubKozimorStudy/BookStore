package store.store.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import store.store.entity.Transaction;
import store.store.services.TransactionServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionForTableMapper {

    TransactionServiceImpl transactionService;

    @Autowired
    public TransactionForTableMapper(TransactionServiceImpl transactionService) {
        this.transactionService = transactionService;
    }

    public List<TransactionForTable> getTransactionsForTable() {
        return transactionService.getAllTransactions()
                .stream()
                .map(transaction ->
                        new TransactionForTable(transaction.getClient().getLastName(),
                                transaction.getStore().getShopName(),
                                transaction.getBook().getTitle(),
                                transaction.getDate(),
                                transaction.getPrice()))
                .collect(Collectors.toList());
    }

}
