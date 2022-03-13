package jpabook.jpashop.api;


import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.OrderItems;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.Orders;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrdersDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/order")
    public List<ControllerOrdersDto> orderSearchV1() {
        List<Orders> order = orderRepository.findOrder();

        return order.stream().map(o -> new ControllerOrdersDto(o)).collect(Collectors.toList());
    }

    @Data
    static class ControllerOrdersDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public ControllerOrdersDto(Orders order ) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }

    @GetMapping("/api/v2/order")
    public List<OrdersDto> orderSearchV2() {
        return orderRepository.findOrderDto();
    }

    @GetMapping("/api/order/all")
    public List<AllOrderDto> orderSearchAll() {
        List<Orders> orderAll = orderRepository.findOrderAll();
        return orderAll.stream().map(o -> new AllOrderDto(o)).collect(toList());
    }

    @GetMapping("/api/order/page/all")
    public List<AllOrderDto> orderSearchAllPaging(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                                  @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<Orders> orderAll = orderRepository.findOrderAllPaging(offset, limit);
        return orderAll.stream().map(o -> new AllOrderDto(o)).collect(toList());
    }

    @Data
    static class AllOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;
        public AllOrderDto(Orders order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(toList());
        }
    }
    @Data
    static class OrderItemDto {
        private String itemName;
        private int orderPrice;
        private int count;
        public OrderItemDto(OrderItems orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
