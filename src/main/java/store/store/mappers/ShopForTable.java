package store.store.mappers;

public class ShopForTable {
    Long shopId;
    String name;
    String city;
    String street;

    public ShopForTable(Long shopId, String name, String city, String street) {
        this.shopId = shopId;
        this.name = name;
        this.city = city;
        this.street = street;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "ShopForTable{" +
                "shopId=" + shopId +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                '}';
    }
}
