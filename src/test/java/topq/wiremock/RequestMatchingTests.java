package topq.wiremock;

import lombok.SneakyThrows;
import org.json.JSONObject;
import org.testng.annotations.Test;
import retrofit2.Response;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static topq.ResourceUtils.resourceToString;

public class RequestMatchingTests extends AbstractMockedTest{


    @Test
    @SneakyThrows
    public void testRequestMatching() {
        stubFor(any(urlPathEqualTo("/everything"))
                .withHeader("Accept", containing("xml"))
                .withCookie("session", matching(".*12345.*"))
                .withQueryParam("search_term", equalTo("WireMock"))
                .withBasicAuth("jeff@example.com", "jeffteenjefftyjeff")
                .withRequestBody(equalToXml("<search-results />"))
                .withRequestBody(matchingXPath("//search-results"))
                .withMultipartRequestBody(
                        aMultipart()
                                .withName("info")
                                .withHeader("Content-Type", containing("charset"))
                                .withBody(equalToJson("{}"))
                )
                .willReturn(aResponse()));
    }

}
