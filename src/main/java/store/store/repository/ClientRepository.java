package store.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.store.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
