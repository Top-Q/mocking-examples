package topq.mockito.exe0;

import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository; // Mock the dependency

    @InjectMocks
    private OrderService orderService; // Inject the mock into the service

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    public void testGetOrderItem() {
        // Arrange: Stub the findOrderById method
        Order mockOrder = new Order(1, "Laptop", 2);
        when(orderRepository.findOrderById(1)).thenReturn(mockOrder);

        // Act: Call the service method
        String result = orderService.getOrderItem(1);

        // Assert: Verify behavior
        assertEquals(result, "Laptop"); // Validate the return value
        verify(orderRepository).findOrderById(1); // Ensure method was called
    }
}
