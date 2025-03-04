package topq.mockito;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import topq.MyService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class TestMyService {


    @Mock
    private MyService mockService;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessData() {
        when(mockService.processData(anyString())).thenReturn("processed data from Top-Q service");
        System.out.println(mockService.processData("data"));
        // Output: processed data from Top-Q service
    }

    @Test
    public void testSpy() {
        List<String> list = new ArrayList<>();
        List<String> spyList = spy(list);
        doReturn(100).when(spyList).size();
        spyList.add("test");

        System.out.println(list.size()); // Prints 0
//
//        System.out.println(spyList.size()); // Prints 100
//
//        // Verify the behavior
//        System.out.println(spyList.get(0)); // Calls real method: "test"
//        System.out.println(spyList.size()); // Calls stubbed method: 100
//
//        // Verify interaction
//        verify(spyList).add("test");
//
//        // Stub a method
//        doReturn("mocked").when(spyList).get(1);
//        System.out.println(spyList.get(1)); // Calls stubbed method: "mocked"
    }


    @Test
    public void testGetData() {
        MyService mockService = mock(MyService.class);
        when(mockService.getData()).thenReturn("data from Top-Q service");
        System.out.println(mockService.getData());
        verify(mockService).getData();
        verify(mockService, atLeast(1)).getData();
    }

    @Test
    public void testNoMockingDefined() {
        MyService mockService = mock(MyService.class);
        System.out.println(mockService.getData());
        // Output: null
    }

}
