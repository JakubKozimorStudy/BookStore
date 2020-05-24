package store.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import store.store.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
