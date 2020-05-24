package store.store.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import store.store.entity.Store;
import store.store.repository.StoreRepository;

import java.util.List;

@Service
public class ShopServiceImpl {

    StoreRepository storeRepository;

    @Autowired
    public ShopServiceImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public List<Store> getAllShops() {
        return storeRepository.findAll();
    }

    public void addNewStore(Store store) {
        storeRepository.save(store);
    }
}
