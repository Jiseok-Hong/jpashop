package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.OrderItems;
import jpabook.jpashop.domain.Orders;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * Order Items
     */
    @Transactional
    public long order(Long memberId, Long itemId, int count) {
        //Call Entity
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //Set Delivery Information
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //Create order items
        OrderItems orderItems = OrderItems.createOrderItems(item, item.getPrice(), count);

        //Call order
        Orders order = Orders.createOrder(member, delivery, orderItems);

        //save the order
        orderRepository.save(order);
        return order.getId();
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Orders order = orderRepository.findOne(orderId);
        order.cancel();
    }

    public List<Orders> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }
}
