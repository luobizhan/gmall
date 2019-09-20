package com.lbz.gmall.search.mapper;

import com.lbz.gmall.bean.PmsSearchParam;
import com.lbz.gmall.bean.PmsSearchSkuInfo;
import com.lbz.gmall.bean.PmsSkuAttrValue;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lbz
 * @create 2019-08-30 22:25
 */
@Component
public class SearchMapper {

    @Autowired
    private JestClient jestClient;

    public List<PmsSearchSkuInfo> getSearchSkuInfo(PmsSearchParam pmsSearchParam) {

        //组装查询条件
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        String[] skuAttrValueId = pmsSearchParam.getValueId();

        //filter

        if (!StringUtil.isBlank(pmsSearchParam.getCatalog3Id())) {
            TermQueryBuilder termQueryBuilder = new TermQueryBuilder("catalog3Id", pmsSearchParam.getCatalog3Id());
            boolQueryBuilder.filter(termQueryBuilder);
        }

        if (skuAttrValueId != null) {
            for (String valueId : skuAttrValueId) {
                TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId",valueId);
                boolQueryBuilder.filter(termQueryBuilder);
            }
        }

        //must

        if (!StringUtil.isBlank(pmsSearchParam.getKeyword())) {

            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName", pmsSearchParam.getKeyword());
            boolQueryBuilder.must(matchQueryBuilder);
        }

        //query

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(100);

        //highlighter
        //keyword为空时不能加highlight 否则会空指针异常
        if(!StringUtil.isBlank(pmsSearchParam.getKeyword())) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.preTags("<span style=\"color: red\">");
            highlightBuilder.field("skuName");
            highlightBuilder.postTags("</span>");
            searchSourceBuilder.highlighter(highlightBuilder);
        }
        //获取dsl
        String dsl = searchSourceBuilder.toString();
        Search build = new Search.Builder(dsl).addIndex("gmall").addType("PmsSkuInfo").build();

        //使用JestClient操作es

        //使用pmsSearchSkuInfos封装结果集
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList<>();
        try {
            SearchResult execute = jestClient.execute(build);

            List<SearchResult.Hit<PmsSearchSkuInfo, Void>> hits = execute.getHits(PmsSearchSkuInfo.class);
            for (SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits) {
                PmsSearchSkuInfo source = hit.source;
                //这里只用高亮skuName字段，如果要高亮多个字段，最好创建一个pojo来接收
                if(!StringUtil.isBlank(pmsSearchParam.getKeyword())){
                    List<String> skuName = hit.highlight.get("skuName");
                    source.setSkuName(skuName.get(0));
                }
                pmsSearchSkuInfos.add(source);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //商品筛选urlParm封装


        return pmsSearchSkuInfos;
    }

}
