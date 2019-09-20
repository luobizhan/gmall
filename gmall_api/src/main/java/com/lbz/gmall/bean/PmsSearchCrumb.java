package com.lbz.gmall.bean;

import java.io.Serializable;

/**
 * @author lbz
 * @create 2019-09-04 16:58
 */
public class PmsSearchCrumb  implements Serializable {
    private String valueName ;
    private String valueId ;
    private String urlParam;

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public String getValueId() {
        return valueId;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId;
    }

    public String getUrlParam() {
        return urlParam;
    }

    public void setUrlParam(String urlParam) {
        this.urlParam = urlParam;
    }
}
