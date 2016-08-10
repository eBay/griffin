package org.apache.bark.service;

import java.util.Date;

import org.apache.bark.domain.DqModel;
import org.apache.bark.vo.DqModelVo;
import org.springframework.stereotype.Component;

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
