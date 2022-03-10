package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;

        public void dbInit1() {
            Member member = new Member();
            member.setName("userA");
            member.setAddress(new Address("Seoul", "street1", "123123"));
            em.persist(member);

            Book book1 = new Book();
            book1.setName("JPA book");
            book1.setIsbn("1241");
            book1.setAuthor("John");
            book1.setStockQuantity(123);
            book1.setPrice(10000);
            em.persist(book1);

            Book book2 = new Book();
            book2.setName("JPA book1");
            book2.setIsbn("1232");
            book2.setAuthor("John1");
            book2.setStockQuantity(142);
            book2.setPrice(9120);
            em.persist(book2);

            OrderItems orderItems1 = OrderItems.createOrderItems(book1, 10000, 1);
            OrderItems orderItems2 = OrderItems.createOrderItems(book2, 9120, 2);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Orders order = Orders.createOrder(member, delivery, orderItems1, orderItems2);
            em.persist(order);
        }
        public void dbInit2() {
            Member member = new Member();
            member.setName("userB");
            member.setAddress(new Address("Hong Kong", "street2", "123123"));
            em.persist(member);

            Book book1 = new Book();
            book1.setName("Hemlet");
            book1.setIsbn("123");
            book1.setAuthor("Peter");
            book1.setStockQuantity(100);
            book1.setPrice(20000);
            em.persist(book1);

            Book book2 = new Book();
            book2.setName("Hemlet1");
            book2.setIsbn("12512");
            book2.setAuthor("Peter1");
            book2.setStockQuantity(112);
            book2.setPrice(30000);
            em.persist(book2);

            OrderItems orderItems1 = OrderItems.createOrderItems(book1, 20000, 3);
            OrderItems orderItems2 = OrderItems.createOrderItems(book2, 30000, 4);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Orders order = Orders.createOrder(member, delivery, orderItems1, orderItems2);
            em.persist(order);
        }
    }
}
