package com.lbz.gmall.search.controller;

import com.lbz.gmall.annotation.LoginRequire;
import com.lbz.gmall.bean.*;
import com.lbz.gmall.service.PmsBaseAttrService;
import com.lbz.gmall.service.SearchService;
import org.apache.dubbo.config.annotation.Reference;
import org.jsoup.helper.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * @author lbz
 * @create 2019-08-30 20:23
 */
@Controller
public class SearchController {

    @Reference
    private SearchService searchService ;
    @Reference
    private PmsBaseAttrService pmsBaseAttrService ;

    @LoginRequire(MustLogin = false)
    @RequestMapping("/index")
    public String index(HttpServletRequest request,Model model) {

        String nickname = (String) request.getAttribute("nickname");
        if(nickname==null){
            nickname = "请登录";
        }
        model.addAttribute("nickname",nickname);
        return "index";
    }

    @RequestMapping("/list.html")
    public String list(PmsSearchParam pmsSearchParam,Model model){

        //返回urlParam
        String urlParam = getUrlParam(pmsSearchParam,null) ;
        model.addAttribute("urlParam",urlParam);

        //根据pmsSearchParam属性在es中搜索到结果
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = searchService.getSearchSkuInfo(pmsSearchParam);

        //返回给页面
        String keyword = pmsSearchParam.getKeyword();
        model.addAttribute("keyword",keyword);
        model.addAttribute("skuLsInfoList",pmsSearchSkuInfos);

        //平台属性筛选条件的数据
        //从pmsSearchSkuInfos找出valueId（不重复）
        Set<String> valueIds = new HashSet<>();
        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfos) {
            List<PmsSkuAttrValue> skuAttrValueList = pmsSearchSkuInfo.getSkuAttrValueList();
            for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
                valueIds.add(pmsSkuAttrValue.getValueId());
            }
        }
        //找到valueId对应的平台属性
        String parm = StringUtil.join(valueIds,",");
        List<PmsBaseAttrInfo> atrrInfoListByValueId = pmsBaseAttrService.getAtrrInfoListByValueId(parm);


        //获取对应的attrname
        List<PmsBaseAttrInfo> attrNameList = pmsBaseAttrService.getAtrrNameList(parm);
        //由于attrname重复，进行再封装
        List<PmsBaseAttrInfo> reAtrrInfoListByValueId = new ArrayList<>();
        for (PmsBaseAttrInfo pmsBaseAttrInfo : attrNameList) {
            PmsBaseAttrInfo pms = new PmsBaseAttrInfo();
            List<PmsBaseAttrValue> add = new ArrayList<>();
            pms.setAttrName(pmsBaseAttrInfo.getAttrName());
            for (PmsBaseAttrInfo baseAttrInfo : atrrInfoListByValueId) {
                if(pmsBaseAttrInfo.getAttrName().equals(baseAttrInfo.getAttrName())){
                   add.add(baseAttrInfo.getAttrValueList().get(0));
                }
            }
            pms.setAttrValueList(add);
            reAtrrInfoListByValueId.add(pms) ;
        }

        //用于封装pmsSearchCrumb
        List<PmsSearchCrumb> list = new ArrayList<>();

        //删除选中的平台属性列和面包屑功能
        if(pmsSearchParam.getValueId()!=null){
            for (String s : pmsSearchParam.getValueId()) {
                Iterator<PmsBaseAttrInfo> iterator = reAtrrInfoListByValueId.iterator();

                //forCrumb
                PmsSearchCrumb pmsSearchCrumb = new PmsSearchCrumb();
                String urlParamForCrumb = getUrlParam(pmsSearchParam,s);
                String valueName = "";
                while (iterator.hasNext()){
                    PmsBaseAttrInfo next = iterator.next();
                    List<PmsBaseAttrValue> attrValueList = next.getAttrValueList();
                    for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                        if(pmsBaseAttrValue.getId().equals(s)){
                            //forCrumb
                            valueName = pmsBaseAttrValue.getValueName();
                            pmsSearchCrumb.setUrlParam(urlParamForCrumb);
                            pmsSearchCrumb.setValueName(valueName);
                            pmsSearchCrumb.setValueId(pmsBaseAttrValue.getId());
                            list.add(pmsSearchCrumb);
                            //forDelete
                            iterator.remove();
                        }
                    }
                }
            }
        }

        model.addAttribute("attrList",reAtrrInfoListByValueId);
        model.addAttribute("attrValueSelectedList",list);
        return "list";
    }


    private String getUrlParam(PmsSearchParam skuLsParam,String id) {
        String urlParam = "";
        String keyword = skuLsParam.getKeyword();
        String catalog3Id = skuLsParam.getCatalog3Id();
        String[] valueId = skuLsParam.getValueId();

        if (!StringUtil.isBlank(keyword)) {
            if (StringUtil.isBlank(urlParam)) {
                urlParam = "keyword=" + keyword;
            } else {
                urlParam = urlParam + "&keyword=" + keyword;
            }
        }
        if (!StringUtil.isBlank(catalog3Id)) {
            if (StringUtil.isBlank(urlParam)) {
                urlParam = "catalog3Id=" + catalog3Id;
            } else {
                urlParam = urlParam + "&catalog3Id=" + catalog3Id;
            }
        }
        if (valueId != null && valueId.length > 0) {
            for (String s : valueId) {
                if (!s.equals(id)) {
                    urlParam = urlParam + "&valueId=" + s;
                }
            }
        }
        return urlParam;
    }
}

