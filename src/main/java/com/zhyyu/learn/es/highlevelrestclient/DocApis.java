package com.zhyyu.learn.es.highlevelrestclient;

import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.TermVectorsRequest;
import org.elasticsearch.client.core.TermVectorsResponse;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;

/**
 * @author juror
 * @datatime 2020/1/3 11:41
 */
public class DocApis {

    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        // index
        IndexRequest indexRequest = new IndexRequest("posts");
        indexRequest.id("1");
        String jsonString = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";
        indexRequest.source(jsonString, XContentType.JSON);
//        indexRequest.opType(DocWriteRequest.OpType.CREATE);

        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println(indexResponse);

        // get
        GetRequest getRequest = new GetRequest(
                "posts",
                "1");
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(getResponse);

        // update
        UpdateRequest updateRequest = new UpdateRequest(
                "posts",
                "1");
        String updateJsonString = "{" +
                "\"updated\":\"2017-01-01\"," +
                "\"reason\":\"daily update\"" +
                "}";
        updateRequest.doc(updateJsonString, XContentType.JSON);
        UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(updateResponse);

        // termVector
        TermVectorsRequest termVectorsRequest = new TermVectorsRequest("posts", "1");
        termVectorsRequest.setFields("user");
        TermVectorsResponse termVectorsResponse = client.termvectors(termVectorsRequest, RequestOptions.DEFAULT);
        System.out.println(termVectorsResponse);

        client.close();
    }

}
