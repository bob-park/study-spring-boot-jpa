package jpabook.jpbshop.repository.order.query;

public class OrderItemQueryDto {

    private final Long orderId;
    private final String itemName;
    private final int orderPrice;
    private final int count;

    public OrderItemQueryDto(Long orderId, String itemName, int orderPrice, int count) {
        this.orderId = orderId;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getItemName() {
        return itemName;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public int getCount() {
        return count;
    }
}
