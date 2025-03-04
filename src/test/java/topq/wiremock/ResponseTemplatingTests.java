package topq.wiremock;

import lombok.SneakyThrows;
import org.json.JSONObject;
import org.testng.annotations.Test;
import retrofit2.Response;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static topq.ResourceUtils.resourceToString;

public class ResponseTemplatingTests extends AbstractMockedTest{

    @Test
    @SneakyThrows
    public void testEditingStubs() {
        JSONObject json = new JSONObject(resourceToString("mock/addPetResponse.json"));
        json.put("name", "Rex");
        UUID id = UUID.randomUUID();
        stubFor(get(urlEqualTo("/pet/1"))
                .withId(id)
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(json.toString())));
        Response<String> response = mockedPetStore.getPetById("1").execute();
        assertThat(new JSONObject(response.body()).get("name")).isEqualTo("Rex");

        json.put("name", "Max");
        // NOTE: We are using editStub instead of stubFor with the same id
        editStub(get(urlEqualTo("/pet/1"))
                .withId(id)
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(json.toString())));
        response = mockedPetStore.getPetById("1").execute();
        assertThat(new JSONObject(response.body()).get("name")).isEqualTo("Max");
    }

}
