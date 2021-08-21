package jpabook.jpbshop.domain;

import javax.persistence.*;

@Entity
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name="delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    public Long getId() {
        return id;
    }

    public Delivery setId(Long id) {
        this.id = id;
        return this;
    }

    public Order getOrder() {
        return order;
    }

    public Delivery setOrder(Order order) {
        this.order = order;
        return this;
    }

    public Address getAddress() {
        return address;
    }

    public Delivery setAddress(Address address) {
        this.address = address;
        return this;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public Delivery setStatus(DeliveryStatus status) {
        this.status = status;
        return this;
    }
}
