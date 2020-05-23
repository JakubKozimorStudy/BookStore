package store.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.store.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
