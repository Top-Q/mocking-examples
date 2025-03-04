package topq.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import topq.PetStoreService;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public abstract class AbstractMockedTest {

    private static final int mockedPort = 8090;
    private static final String REAL_BASE_URL = "https://petstore.swagger.io/v2/";
    protected static final String MOCK_BASE_URL = "http://localhost:" + mockedPort + "/";
    protected PetStoreService realPetStore;
    protected PetStoreService mockedPetStore;
    protected WireMockServer wireMockServer;


    @BeforeClass
    @SneakyThrows
    public void setup(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(REAL_BASE_URL)  // Ensure BASE_URL ends with '/'
                .client(new OkHttpClient())
                .addConverterFactory(ScalarsConverterFactory.create()) // Optional if using JSON
                .build();

        realPetStore = retrofit.create(PetStoreService.class);
        retrofit = new Retrofit.Builder()
                .baseUrl(MOCK_BASE_URL)  // Ensure BASE_URL ends with '/'
                .client(new OkHttpClient())
                .addConverterFactory(ScalarsConverterFactory.create()) // Optional if using JSON
                .build();
        mockedPetStore = retrofit.create(PetStoreService.class);
        wireMockServer = new WireMockServer(options().port(mockedPort));

        wireMockServer.start();
        configureFor("localhost", mockedPort);
    }

    @AfterClass
    public void tearDown() {
        wireMockServer.stop();
    }

}
