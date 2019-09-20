package com.lbz.gmall.service;

import com.lbz.gmall.bean.PmsSearchParam;
import com.lbz.gmall.bean.PmsSearchSkuInfo;

import java.util.List;

/**
 * @author lbz
 * @create 2019-08-30 22:11
 */
public interface SearchService {

    /**
     * 从es中搜索相关的数据
     * @param pmsSearchParam (从前端返回的搜索关键字数据)
     * @return
     */
    List<PmsSearchSkuInfo> getSearchSkuInfo(PmsSearchParam pmsSearchParam);
}
