package store.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.store.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
