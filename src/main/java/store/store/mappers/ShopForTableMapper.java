package store.store.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import store.store.services.ShopServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopForTableMapper {

    ShopServiceImpl shopService;

    @Autowired
    public ShopForTableMapper(ShopServiceImpl shopService) {
        this.shopService = shopService;
    }

    public List<ShopForTable> getShopsForTable() {
        return shopService.getAllShops().stream()
                .map(shop ->
                        new ShopForTable(shop.getShopId(), shop.getShopName(), shop.getAddress().getCity(), shop.getAddress().getStreet()))
                .collect(Collectors.toList());
    }
}
