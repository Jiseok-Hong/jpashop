package jpabook.jpashop.repository;


import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.OrderItems;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.Orders;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Orders order) {
        em.persist(order);
    }

    public Orders findOne(Long id) {
        return em.find(Orders.class, id);
    }

    public List<Orders> findAllByString(OrderSearch orderSearch) {
        //language=JPAQL
        String jpql = "select o From Orders o join o.member m";
        boolean isFirstCondition = true;
        //Order Status
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }
        //UserName
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }
        TypedQuery<Orders> query = em.createQuery(jpql, Orders.class)
                .setMaxResults(1000); //최대 1000건
        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }

    public List<OrdersDto> findOrderDto() {
        return em.createQuery("select new jpabook.jpashop.repository.OrdersDto(o.id, m.name, o.orderDate, o.status, d.address) from Orders o" +
                                " join o.member m" +
                                " join o.delivery d", OrdersDto.class).getResultList();
    }

    public List<Orders> findOrder() {
        return em.createQuery("select o from Orders o" +
                                " join fetch o.member m" +
                                " join fetch o.delivery d", Orders.class).getResultList();
    }

    public List<Orders> findOrderAll() {
        return em.createQuery("select distinct o from Orders o " +
                                "join fetch o.member m " +
                                "join fetch o.delivery d " +
                                "join fetch o.orderItems oi " +
                                "join fetch oi.item i", Orders.class).getResultList();
    }


    public List<Orders> findOrderAllPaging(int offset, int limit) {
        return em.createQuery("select o from Orders o" +
                " join fetch o.member m" +
                " join fetch o.delivery d", Orders.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}
