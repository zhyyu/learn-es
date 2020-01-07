package com.zhyyu.learn.es.springstarter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Component;

/**
 * @author juror
 * @datatime 2020/1/3 18:42
 */
//@Component
public class TestEsTemplate implements CommandLineRunner {

    @Autowired
    private ElasticsearchRestTemplate esTemplate;

    @Override
    public void run(String... args) throws Exception {
        boolean posts = esTemplate.indexExists("posts");
        System.out.println(posts);
    }

}
