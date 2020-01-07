package com.zhyyu.learn.es.springstarter.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.regexpQuery;

/**
 * @author juror
 * @datatime 2020/1/6 14:59
 */
//@Component
public class RepositoryTest implements CommandLineRunner {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

    @Override
    public void run(String... args) throws Exception {
        // save
        Article article = new Article();
        article.setTitle("Spring Data Elasticsearch");

        article.setAuthors(Arrays.asList(new Author("John Smith"), new Author("John Doe")));
        articleRepository.save(article);

        // query
        String nameToFind = "John Smith";
        Page<Article> articleByAuthorName = articleRepository.findByAuthorsName(nameToFind, PageRequest.of(0, 10));
        System.out.println(articleByAuthorName);

        Page<Article> scnd = articleRepository.findByAuthorsNameUsingCustomQuery(nameToFind, PageRequest.of(0, 10));
        System.out.println(scnd);

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withFilter(regexpQuery("title", ".*data.*"))
                .build();
        List<Article> articles = elasticsearchTemplate.queryForList(searchQuery, Article.class);
        System.out.println(articles);
    }

}
