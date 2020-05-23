package store.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.store.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
