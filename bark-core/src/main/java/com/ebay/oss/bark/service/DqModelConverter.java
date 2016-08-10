package com.ebay.oss.bark.service;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.ebay.oss.bark.domain.DqModel;
import com.ebay.oss.bark.vo.DqModelVo;

@Component
public class DqModelConverter implements Converter<DqModel, DqModelVo>{

    @Override
	public DqModelVo voOf(DqModel o) {
	    DqModelVo vo = new DqModelVo();
	    vo.setName(o.getModelName());
	    vo.setSystem(o.getSystem());
	    vo.setDescription(o.getModelDesc());
	    vo.setType(o.getModelType());
        vo.setCreateDate(new Date(o.getTimestamp()));
        vo.setStatus("" + o.getStatus());
        vo.setAssetName(o.getAssetName());
        vo.setOwner(o.getOwner());
        return vo;
    }

    @Override
    public DqModel entityOf(DqModelVo vo) {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented yet...");
    }


}
