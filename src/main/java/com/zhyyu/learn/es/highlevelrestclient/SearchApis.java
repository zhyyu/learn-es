package com.zhyyu.learn.es.highlevelrestclient;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;

/**
 * @author juror
 * @datatime 2020/1/3 16:35
 */
public class SearchApis {

    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        // search
//        SearchRequest searchRequest = new SearchRequest();
        SearchRequest searchRequest = new SearchRequest("posts");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
//        searchSourceBuilder.query(QueryBuilders.termQuery("user", "kimchy"));
        searchSourceBuilder.query(QueryBuilders.matchQuery("user", "kimch").fuzziness(Fuzziness.AUTO).prefixLength(3).maxExpansions(10));
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(5);
        searchSourceBuilder.sort(new FieldSortBuilder("_id").order(SortOrder.ASC));

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(searchResponse);

        // count
        CountRequest countRequest = new CountRequest("posts");
        SearchSourceBuilder searchSourceBuilder2 = new SearchSourceBuilder();
        searchSourceBuilder2.query(QueryBuilders.termQuery("user", "kimchy"));
        countRequest.source(searchSourceBuilder2);

        CountResponse countResponse = client.count(countRequest, RequestOptions.DEFAULT);
        System.out.println(countResponse);

        client.close();
    }

}
