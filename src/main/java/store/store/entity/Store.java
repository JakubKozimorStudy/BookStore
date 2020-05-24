package store.store.entity;

import javax.persistence.*;

@Entity
@Table(name = "stores")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shop_id")
    private Long shopId;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "adress_id")
    private Address address;
    @Column(name = "shop_name")
    private String shopName;

    public Store() {
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    @Override
    public String toString() {
        return "Store{" +
                "shopId=" + shopId +
                ", address=" + address +
                ", shopName='" + shopName + '\'' +
                '}';
    }
}
