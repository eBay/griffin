package com.ebay.oss.bark.service.modelcreator;

import com.ebay.oss.bark.domain.DqModel;
import com.ebay.oss.bark.domain.ModelStatus;
import com.ebay.oss.bark.domain.ModelType;
import com.ebay.oss.bark.vo.MappingItemInput;
import com.ebay.oss.bark.vo.ModelInput;

public class AccurcyModelCreator extends BaseModelCreator {

    @Override
    protected void enhance(DqModel entity, ModelInput input) {
        entity.setStatus(ModelStatus.TESTING);

        newSampleJob4Model(entity);
    }

    @Override
    public boolean isSupport(ModelInput input) {
        return input.getBasic() != null && input.getBasic().getType() == ModelType.ACCURACY;
    }

    protected String contentOf( ModelInput input) {
	    String content = input.getExtra().getSrcDb() + "|"
	                    + input.getExtra().getSrcDataSet() + "|"
	                    + input.getExtra().getTargetDb() + "|"
	                    + input.getExtra().getTargetDataSet() + "|";

	    String delimeter = "";
	    for(MappingItemInput itm : input.getMappings()) {
	        content += delimeter 
	                        + itm.getSrc() + ","
	                        + itm.getTarget() + ","
	                        + itm.isIsPk() + ","
	                        + itm.getMatchMethod();
	        delimeter = ";";
	    }

	    return content;

    }
}
