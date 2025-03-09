package topq.wiremock;


import com.github.tomakehurst.wiremock.stubbing.Scenario;
import lombok.SneakyThrows;
import org.testng.annotations.Test;
import retrofit2.Response;

import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static topq.ResourceUtils.resourceToString;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class StatefulTests extends AbstractMockedTest{

    @Test
    @SneakyThrows
    public void testState() {
        stubFor(get(urlEqualTo("/pet/12121212"))
                .inScenario("Pet Store")
                .whenScenarioStateIs(Scenario.STARTED) // Initial state
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(404)
                        .withBody("{ \"message\": \"Pet not found\" }")));

        stubFor(post(urlEqualTo("/pet"))
                .inScenario("Pet Store")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(resourceToString("mock/addPetResponse.json")))
                .willSetStateTo("Pet Added")); // Change state after adding the pet

        stubFor(get(urlEqualTo("/pet/12121212"))
                .inScenario("Pet Store")
                .whenScenarioStateIs("Pet Added") // Change response after state transition
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(resourceToString("mock/addPetResponse.json"))));

// Step 1: Initial GET should return 404
        Response<String> response = mockedPetStore.getPetById("12121212").execute();
        System.out.println(response.code()); // Expected: 404
        System.out.println(response.body()); // Expected: "Pet not found"

// Step 2: Add Pet
        Response<String> addResponse = mockedPetStore.addPet(resourceToString("real/addPetRequest.json")).execute();
        assertThat(addResponse.code()).isEqualTo(200);

// Step 3: GET after adding the pet should return pet details
        response = mockedPetStore.getPetById("12121212").execute();
        System.out.println(response.code()); // Expected: 200
        System.out.println(response.body()); // Expected: Pet details

    }

}
