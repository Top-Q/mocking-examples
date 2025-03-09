package topq.wiremock;

import lombok.SneakyThrows;
import org.testng.annotations.Test;
import retrofit2.Response;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static topq.ResourceUtils.resourceToString;

public class SimulatingFaultsTests extends AbstractMockedTest{

    @Test
    @SneakyThrows
    public void testFixedDelay() {
        stubFor(post(urlEqualTo("/pet"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(resourceToString("mock/addPetResponse.json"))
                        .withFixedDelay(2000)));


        long start = System.currentTimeMillis();
        mockedPetStore.addPet(resourceToString("real/addPetRequest.json")).execute();
        System.out.println(System.currentTimeMillis() - start);
    }

    /**
     *
     * A lognormal distribution is a pretty good approximation of long tailed latencies centered on the 50th percentile.
     * It takes two parameters:
     *  median - The 50th percentile of latencies.
     *  sigma - Standard deviation. The larger the value, the longer the tail.
     * <a href="https://wiremock.org/docs/simulating-faults/#per-stub-random-delays">...</a>
     */
    @Test
    @SneakyThrows
    public void testLogNormalDelay() {
        stubFor(post(urlEqualTo("/pet"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(resourceToString("mock/addPetResponse.json"))
                        .withLogNormalRandomDelay(1000, 0.2)));


        long start = System.currentTimeMillis();
        mockedPetStore.addPet(resourceToString("real/addPetRequest.json")).execute();
        System.out.println(System.currentTimeMillis() - start);
    }
}
