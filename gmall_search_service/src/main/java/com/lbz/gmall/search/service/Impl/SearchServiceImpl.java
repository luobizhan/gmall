package com.lbz.gmall.search.service.Impl;

import com.lbz.gmall.bean.PmsSearchParam;
import com.lbz.gmall.bean.PmsSearchSkuInfo;
import com.lbz.gmall.search.mapper.SearchMapper;
import com.lbz.gmall.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lbz
 * @create 2019-08-30 22:21
 */

@Service
@org.apache.dubbo.config.annotation.Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchMapper searchMapper ;

    @Override
    public List<PmsSearchSkuInfo> getSearchSkuInfo(PmsSearchParam pmsSearchParam) {

      List<PmsSearchSkuInfo> pmsSearchSkuInfos = searchMapper.getSearchSkuInfo(pmsSearchParam);

        return pmsSearchSkuInfos;
    }
}
