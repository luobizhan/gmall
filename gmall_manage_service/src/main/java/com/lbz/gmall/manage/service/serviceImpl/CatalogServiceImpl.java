package com.lbz.gmall.manage.service.serviceImpl;

import com.lbz.gmall.bean.PmsBaseCatalog1;
import com.lbz.gmall.bean.PmsBaseCatalog2;
import com.lbz.gmall.bean.PmsBaseCatalog3;
import com.lbz.gmall.manage.mapper.PmsBaseCatalog1Mapper;
import com.lbz.gmall.manage.mapper.PmsBaseCatalog2Mapper;
import com.lbz.gmall.manage.mapper.PmsBaseCatalog3Mapper;
import com.lbz.gmall.service.CatalogService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author lbz
 * @create 2019-08-17 15:16
 */

@Service
@org.springframework.stereotype.Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private PmsBaseCatalog1Mapper pmsBaseCatalog1Mapper ;
    @Autowired
    private PmsBaseCatalog2Mapper pmsBaseCatalog2Mapper ;
    @Autowired
    private PmsBaseCatalog3Mapper pmsBaseCatalog3Mapper ;


    @Override
    public List<PmsBaseCatalog1> getCatlog1() {
        return pmsBaseCatalog1Mapper.selectAll();
    }

    @Override
    public List<PmsBaseCatalog2> getCatlog2(String catalogId) {

        PmsBaseCatalog2 pmsBaseCatalog2 = new PmsBaseCatalog2() ;
        pmsBaseCatalog2.setCatalog1Id(catalogId) ;

        return pmsBaseCatalog2Mapper.select(pmsBaseCatalog2) ;
    }

    @Override
    public List<PmsBaseCatalog3> getCatlog3(String catalogId) {
        PmsBaseCatalog3 pmsBaseCatalog3 = new PmsBaseCatalog3();
        pmsBaseCatalog3.setCatalog2Id(catalogId) ;
        return pmsBaseCatalog3Mapper.select(pmsBaseCatalog3) ;
    }


}
