package topq;

import lombok.SneakyThrows;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpClient {

    private String baseUrl;

    private OkHttpClient client;

    public HttpClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.client = new OkHttpClient();
    }

    @SneakyThrows
    public Response get(String url) {
        Request request = new Request.Builder()
                .url(baseUrl + url)
                .build();

        final Call call = client.newCall(request);
        return call.execute();
    }

}