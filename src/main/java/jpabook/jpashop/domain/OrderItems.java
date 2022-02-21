package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.criterion.Order;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class OrderItems {

    @Id
    @GeneratedValue
    @Column(name = "order_items_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Orders order;

    private int orderPrice;

    private int count;

    //constructor
    public static OrderItems createOrderItems(Item item, int orderPrice, int count) {
        OrderItems orderItems = new OrderItems();
        orderItems.setOrderPrice(orderPrice);
        orderItems.setItem(item);
        orderItems.setCount(count);

        item.removeStock(count);
        return orderItems;
    }

    public void cancel() {
        getItem().addStock(count);
    }

    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
