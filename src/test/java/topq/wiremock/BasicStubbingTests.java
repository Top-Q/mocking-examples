package topq.wiremock;

import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.testng.annotations.Test;
import retrofit2.Response;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static topq.ResourceUtils.resourceToString;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class BasicStubbingTests extends AbstractMockedTest {


    @Test
    @SneakyThrows
    public void testRealCalls() {
        Response<String> response = realPetStore.addPet(resourceToString("real/addPetRequest.json")).execute();
        assertThat(response.code()).isEqualTo(200);
        System.out.println(response.body());
        JSONObject json = new JSONObject(response.body());
        String id = String.valueOf(json.get("id"));

        response = realPetStore.getPetById(id).execute();
        assertThat(response.code()).isEqualTo(200);
        System.out.println(response.body());

    }

    @Test
    @SneakyThrows
    public void testBasicStubbing() {
        stubFor(post(urlEqualTo("/pet"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(resourceToString("mock/addPetResponse.json"))));

        stubFor(get(urlEqualTo("/pet/12121212"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(resourceToString("mock/addPetResponse.json"))));


        Response<String> response = mockedPetStore.addPet(resourceToString("real/addPetRequest.json")).execute();
        System.out.println(response.body());
        assertThat(response.code()).isEqualTo(200);
        JSONObject json = new JSONObject(response.body());
        String id = String.valueOf(json.get("id"));

        response = mockedPetStore.getPetById(id).execute();

        assertThat(response.code()).isEqualTo(200);
        System.out.println(response.body());
    }

    /**
     * <a href="https://wiremock.org/docs/stubbing/#stub-priority">...</a>
     */
    @Test
    @SneakyThrows
    public void testStubPriority() {
        //Catch-all case
        stubFor(get(urlMatching("/store/.*")).atPriority(5)
                .willReturn(aResponse().withStatus(401)));

        //Specific case
        stubFor(get(urlEqualTo("/store/inventory")).atPriority(1) //1 is highest
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(resourceToString("mock/getInventoryResponse.json"))));

        Response<String> response = mockedPetStore.getStoreOrder().execute();
        assertThat(response.code()).isEqualTo(401);

        response = mockedPetStore.getStoreInventory().execute();
        assertThat(response.code()).isEqualTo(200);
        System.out.println(response.body());
    }

    /**
     * <a href="https://wiremock.org/docs/stubbing/#default-response-for-unmapped-requests">...</a>
     */
    @Test
    @SneakyThrows
    public void testUnmappedRequests() {
        stubFor(any(anyUrl())
                .atPriority(10)
                .willReturn(aResponse()
                        .withStatus(404)
                        .withBody("{\"status\":\"Error\",\"message\":\"Endpoint not found\"}")));

        Response<String> response = mockedPetStore.getStoreOrder().execute();
        System.out.println(response.body());
        assertThat(response.code()).isEqualTo(404);
        assertThat(response.body()).contains("Endpoint not found");
    }



    /**
     * <a href="https://wiremock.org/docs/stubbing/#editing-stubs">...</a>
     */
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

    /**
     * <a href="https://wiremock.org/docs/stubbing/#file-serving">...</a>
     */
    @Test
    @SneakyThrows
    public void testFileServing() {
        System.out.println("Files should be in " + wireMockServer.getOptions().filesRoot().getPath() + "__files");
        System.out.println("Open browser and hit " + MOCK_BASE_URL+ "file.txt");
        Thread.sleep(20_000);
    }

    /**
     * <a href="https://wiremock.org/docs/stubbing/#getting-all-currently-registered-stub-mappings">...</a>
     */
    @Test
    public void testGetAllRegisteredStubs() {
        stubFor(post(urlEqualTo("/pet"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(resourceToString("mock/addPetResponse.json"))));

        stubFor(get(urlEqualTo("/pet/12121212"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(resourceToString("mock/addPetResponse.json"))));

        System.out.println(WireMock.listAllStubMappings().getMappings());
    }


    /**
     * <a href="https://wiremock.org/docs/stubbing/#saving-stubs">...</a>
     */
    @Test
    @SneakyThrows
    public void testSavingStubs(){
        stubFor(post(urlEqualTo("/pet"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(resourceToString("mock/addPetResponse.json"))));

        stubFor(get(urlEqualTo("/pet/12121212"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(resourceToString("mock/addPetResponse.json"))));

        // The stubs are saved in the mappings folder
        Thread.sleep(20_000);
        WireMock.saveAllMappings();
    }



}
