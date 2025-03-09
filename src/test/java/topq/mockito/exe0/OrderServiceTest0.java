package topq.mockito.exe0;

import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class OrderServiceTest0 {

    @Test
    public void testMockService() {
        Order order = new Order(1, "Laptop", 2);
        OrderRepository orderRepository = mock(OrderRepository.class);
        when(orderRepository.findOrderById(1)).thenReturn(order);
        OrderService orderService = new OrderService(orderRepository);
        String result = orderService.getOrderItem(1);
        assert(result.equals("Laptop"));
        verify(orderRepository).findOrderById(1);

    }


}
