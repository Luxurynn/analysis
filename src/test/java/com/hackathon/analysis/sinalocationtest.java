package com.hackathon.analysis;

import com.hackathon.analysis.repository.sinaRepository;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
public class sinalocationtest {

    @Autowired
    private sinaRepository sinaRepository;
    //blur
    @Test
    public void findLocation() {

        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("province", "北京");
        sinaRepository.search(matchQuery)
                .forEach(e -> System.out.println("city：{}" + e.getNick_name()+ ":"
                        + e.getGender() + ":"
                        + e.get_id()+ ":" + e.getTweets_num()));

    }
    //exact
//    @Test
//    public void findBook() {
//
//        TermQueryBuilder termQuery = QueryBuilders.termQuery("id", 2);
//        sinaRepository.search(termQuery)
//                .forEach(e -> log.info("作品信息：{}", e));
//    }
//
//    //range
//    @Test
//    public void findBook() {
//        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("words").gt(0).lt(300000);
//        sinaRepository.search(rangeQuery);
  //  }
}
