package com.lbz.gmall.manage.controller;

import com.lbz.gmall.bean.PmsBaseCatalog1;
import com.lbz.gmall.bean.PmsBaseCatalog2;
import com.lbz.gmall.bean.PmsBaseCatalog3;
import com.lbz.gmall.service.CatalogService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lbz
 * @create 2019-08-17 14:55
 */
//开启跨域请求通信
@CrossOrigin
@Controller
public class CatalogController {

    @Reference
    private CatalogService catalogService ;

    @ResponseBody
    @RequestMapping(value = "/getCatalog1",method = RequestMethod.POST)
    public List<PmsBaseCatalog1> getCatalog1(){
        return catalogService.getCatlog1();
    }

    @ResponseBody
    @RequestMapping(value = "/getCatalog2",method = RequestMethod.POST)
    public List<PmsBaseCatalog2> getCatalog2(String catalog1Id){
        return catalogService.getCatlog2(catalog1Id);
    }

    @ResponseBody
    @RequestMapping(value = "/getCatalog3",method = RequestMethod.POST)
    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id){

        return catalogService.getCatlog3(catalog2Id);
    }


}
