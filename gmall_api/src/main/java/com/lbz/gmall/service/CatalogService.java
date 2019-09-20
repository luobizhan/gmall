package com.lbz.gmall.service;

import com.lbz.gmall.bean.PmsBaseCatalog1;
import com.lbz.gmall.bean.PmsBaseCatalog2;
import com.lbz.gmall.bean.PmsBaseCatalog3;

import java.util.List;

/**
 * @author lbz
 * @create 2019-08-17 15:13
 */
public interface CatalogService {

    List<PmsBaseCatalog1> getCatlog1();

    List<PmsBaseCatalog2> getCatlog2(String catalogId);

    List<PmsBaseCatalog3> getCatlog3(String catalogId);
}
