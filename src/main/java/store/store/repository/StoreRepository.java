package store.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.store.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
