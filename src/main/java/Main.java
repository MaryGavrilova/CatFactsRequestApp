import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static final String REMOTE_SERVICE_URI = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";

    public static void main(String[] args) {
        try {
            CloseableHttpClient httpClient = createHttpClient();
            HttpGet request = new HttpGet(REMOTE_SERVICE_URI);
            CloseableHttpResponse response = httpClient.execute(request);

            ObjectMapper mapper = new ObjectMapper();
            List<FactAboutCats> facts = mapper.readValue(
                    response.getEntity().getContent(), new TypeReference<List<FactAboutCats>>() {
                    });
            System.out.println("All facts:");
            facts.forEach(System.out::println);

            List<FactAboutCats> popularFacts = facts.stream()
                    .filter(fact -> fact.getUpvotes() != null)
                    .collect(Collectors.toList());
            System.out.println("Popular facts:");
            popularFacts.forEach(System.out::println);

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static CloseableHttpClient createHttpClient() {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
        return httpClient;
    }
}
