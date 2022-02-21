package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.Orders;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void OrderItems() throws Exception{
        //given
        Member member = createMember("User1", new Address("Hong Kong", "1 Hillwood Road", "10021"));

        Item item = createItem("JPA", 130, 10);

        //when
        long orderId = orderService.order(member.getId(), item.getId(), 3);

        //then
        Orders getOrder = orderRepository.findOne(orderId);

        assertEquals("The status is order", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("the order count shud be same", 1, getOrder.getOrderItems().size());
        assertEquals("The total price shud be", 390, getOrder.getTotalPrice());
        assertEquals("THE total stock of item shud be reduced", 7, item.getStockQuantity());
    }




    @Test(expected = NotEnoughStockException.class)
    public void outOfStock() throws Exception{
        //given
        Member member = createMember("User1", new Address("Hong Kong", "1 Hillwood Road", "10021"));
        Item item = createItem("JPA", 130, 10);

        int orderCount = 11;
        //when
        orderService.order(member.getId(), item.getId(), orderCount);
        //then
        fail("out of stock exception error must be occurred");
    }

    @Test
    public void cacelOrder() throws Exception{
        //given
        Member member = createMember("User1", new Address("Hong Kong", "1 Hillwood Road", "10021"));
        Item item = createItem("JPA", 130, 10);

        int orderCount = 2;
        long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        //when
        orderService.cancelOrder(orderId);

        //then
        Orders getOrder = orderRepository.findOne(orderId);

        assertEquals("the order status shud be cencelled", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("the remain quantity shud be", 10, item.getStockQuantity());
    }



    private Item createItem(String name, int price, int stockQuantity) {
        Item item = new Book();
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);
        em.persist(item);
        return item;
    }

    private Member createMember(String username, Address address) {
        Member member = new Member();
        member.setName(username);
        member.setAddress(address);
        em.persist(member);
        return member;
    }

}
