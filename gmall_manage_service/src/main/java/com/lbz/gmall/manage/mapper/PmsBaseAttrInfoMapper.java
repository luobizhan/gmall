package com.lbz.gmall.manage.mapper;

import com.lbz.gmall.bean.PmsBaseAttrInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author lbz
 * @create 2019-08-17 18:18
 */
public interface PmsBaseAttrInfoMapper extends Mapper<PmsBaseAttrInfo> {
    List<PmsBaseAttrInfo> getAtrrInfoListByValueId(@Param("valueIds") String parm);

    List<PmsBaseAttrInfo> getAtrrNameList(@Param("valueIds")String parm);
}
