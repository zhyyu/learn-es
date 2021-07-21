package com.zhyyu.learn.es.lowlevelrestclient;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("lowLevelTest")
public class LowLevelTestController {

    private RestClient restClient;

    @PostConstruct
    private void init() {
//        restClient = RestClient.builder(new HttpHost("localhost", 9200, "http")).build();

        // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/_timeouts.html
        // ...
        restClient = RestClient.builder(new HttpHost("localhost", 9200, "http")).setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
            @Override
            public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder builder) {
                // default connectionTimeOut: 1000, default socketTimeOut: 30000
                return builder.setConnectTimeout(5000)
                        .setSocketTimeout(60000);
            }
        }).build();
    }

    @RequestMapping("firstRequest")
    public String firstRequest() throws IOException {
        Request request = new Request("GET", "/");
        Response response = restClient.performRequest(request);

        String result = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
        System.out.println(result);

        System.out.println(response);
        return response.toString();
    }

    @RequestMapping("requestWithJson")
    public String requestWithJson() throws IOException {
        Request request = new Request("GET", "/sw_log/_search");
        request.setJsonEntity("{\n" +
                "    \"query\":{\n" +
                "        \"term\":{\n" +
                "            \"trace_id\":\"7580be649912412b909d479d576351c7.47.16238221219160003\"\n" +
                "        }\n" +
                "    }\n" +
                "}");
        Response response = restClient.performRequest(request);

        String result = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
        System.out.println(result);

        // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-low-usage-responses.html
        System.out.println(EntityUtils.toString(response.getEntity()));

        System.out.println(response);
        return response.toString();
    }

}
