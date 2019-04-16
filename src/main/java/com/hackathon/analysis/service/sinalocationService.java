package com.hackathon.analysis.service;

import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import com.hackathon.analysis.repository.sinaRepository;
import com.hackathon.analysis.model.sinalocation;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.functionScoreQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;

@Service
public class sinalocationService{


    private final static Integer PAGE_SIZE = 12;          // 每页数量
    private final static Integer DEFAULT_PAGE_NUMBER = 0; // 默认当前页码

    /* 搜索模式 */
    private final static String SCORE_MODE_SUM = "sum"; // 权重分求和模式
    private final static Float  MIN_SCORE = 10.0F;      // 由于无相关性的分值默认为 1 ，设置权重分最小值为 10

    @Autowired
    sinaRepository sinaRepository;

//    @Autowired
//    JestElasticsearchTemplate jestElasticsearchTemplate;
//
//    @Autowired
//    HighLightJestSearchResultMapper jestSearchResultMapper;

    public Page<sinalocation> searchSina(Integer pNum, Integer pSize, String keywords) {
        // 校验分页参数
        if (pSize == null || pSize <= 0) {
            pSize = PAGE_SIZE;
        }

        if (pNum == null || pNum < DEFAULT_PAGE_NUMBER) {
            pNum = DEFAULT_PAGE_NUMBER;
        }

        System.out.println("\n searchCity: searchContent [" + keywords + "] \n ");
        // 构建搜索查询
        SearchQuery searchQuery = getCitySearchQuery(pNum,pSize,keywords);
        System.out.println("\n searchCity: searchContent [" + keywords + "] \n DSL  = \n " + searchQuery.getQuery().toString());
        Page<sinalocation> cityPage = sinaRepository.search(searchQuery);
//        Page<Song> cityPage = jestElasticsearchTemplate.queryForPage(searchQuery,Song.class,jestSearchResultMapper);
        System.out.println(cityPage.getTotalPages() + ""+ cityPage.getContent().size());
        cityPage.hasPrevious();
        return cityPage;
    }

    /**
     * 根据搜索词构造搜索查询语句
     *
     * 代码流程：
     *      - 权重分查询
     *      - 短语匹配
     *      - 设置权重分最小值
     *      - 设置分页参数
     *
     * @param pNum 当前页码
     * @param pSize 每页大小
     * @param searchContent 搜索内容
     * @return
     */
    private SearchQuery getCitySearchQuery(Integer pNum, Integer pSize,String searchContent) {

        /* elasticsearch 2.4.6 版本写法
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery()
                .add(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("lyric", searchContent)),
                        ScoreFunctionBuilders.weightFactorFunction(1000))
                .scoreMode(SCORE_MODE_SUM).setMinScore(MIN_SCORE);
        */


        FunctionScoreQueryBuilder.FilterFunctionBuilder[] functions = {
                new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                        matchPhraseQuery("province", searchContent),
                        ScoreFunctionBuilders.weightFactorFunction(10))
        };
        FunctionScoreQueryBuilder functionScoreQueryBuilder =
                functionScoreQuery(functions).scoreMode(FunctionScoreQuery.ScoreMode.SUM).setMinScore(MIN_SCORE);

        // 分页参数
//        Pageable pageable = new PageRequest(pNum, pSize);
        Pageable pageable = PageRequest.of(pNum, pSize);

        //高亮提示
//        HighlightBuilder.Field highlightField =  new HighlightBuilder.Field("lyric")
//                .preTags(new String[]{"<font color='red'>", "<b>", "<em>"})
//                .postTags(new String[]{"</font>", "</b>", "</em>"})
//                .fragmentSize(15)
//                .numOfFragments(5)
//
//                //highlightQuery必须单独设置，否则在使用FunctionScoreQuery时，highlight配置不生效，返回结果无highlight元素
//                //官方解释：Highlight matches for a query other than the search query. This is especially useful if you use a rescore query because those are not taken into account by highlighting by default.
//                .highlightQuery(matchPhraseQuery("lyric", searchContent));

        return new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withSourceFilter(new FetchSourceFilter(new String[]{"city","gender","nick_name"},new String[]{}))
                //.withHighlightFields(highlightField)
                .withQuery(functionScoreQueryBuilder).build();
    }

}
//
//    private String city;
//    private String gender;
//    private String nick_name;
//    private String province;
//    private int fans_num;
//    private int follow_num;
//    private int tweets_num;