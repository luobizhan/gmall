package com.lbz.gmall.search;

import com.lbz.gmall.bean.PmsSearchSkuInfo;
import com.lbz.gmall.bean.PmsSkuInfo;
import com.lbz.gmall.service.SkuService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallSearchServiceApplicationTests {

    @Reference
    private SkuService skuService ;
    @Autowired
    private JestClient jestClient ;

    @Test
    public void contextLoads() throws InvocationTargetException, IllegalAccessException, IOException {

        final List<PmsSkuInfo> pmsSkuInfoList = skuService.getAll(null);
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList<>();
        //转换成对应elasticsearch的bean

        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfoList) {
            PmsSearchSkuInfo pmsSearchSkuInfo = new PmsSearchSkuInfo();
            BeanUtils.copyProperties(pmsSearchSkuInfo,pmsSkuInfo);
            pmsSearchSkuInfos.add(pmsSearchSkuInfo) ;
        }

        for (PmsSearchSkuInfo searchSkuInfo : pmsSearchSkuInfos) {
            Index put = new Index.Builder(searchSkuInfo).index("gmall").type("PmsSkuInfo").id(searchSkuInfo.getId()).build();
            jestClient.execute(put);
        }
    }

}
