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
    public void testResponseTemplate() {
        String expectedDogName = "Rex";
        JSONObject json = new JSONObject(resourceToString("mock/addPetResponseTemplate.json"));
        json.put("name", expectedDogName);

        stubFor(post(urlEqualTo("/pet"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(json.toString())
                        .withTransformers("response-template")));


        Response<String> response = mockedPetStore.addPet(resourceToString("real/addPetRequest.json")).execute();
        System.out.println(response.body());
        assertThat(response.code()).isEqualTo(200);
        String actualDogName = new JSONObject(response.body()).get("name").toString();
        System.out.println(actualDogName);
        assertThat(actualDogName).isEqualTo(expectedDogName);
    }

}
