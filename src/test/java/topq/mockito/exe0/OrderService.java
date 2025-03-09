package topq.mockito.exe0;

public class OrderService {
    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public String getOrderItem(int orderId) {
        Order order = orderRepository.findOrderById(orderId);
        return (order != null) ? order.item(): "Order Not Found";
    }
}