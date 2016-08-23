package com.ebay.oss.bark.service.modelcreator;

import org.springframework.stereotype.Component;

import com.ebay.oss.bark.domain.DqModel;
import com.ebay.oss.bark.domain.ModelStatus;
import com.ebay.oss.bark.domain.ModelType;
import com.ebay.oss.bark.vo.ModelInput;

@Component("anomalyModelCreator")
public class AnomalyModelCreator extends BaseModelCreator {

    @Override
    public boolean isSupport(ModelInput input) {
		return input.getBasic() != null && input.getBasic().getType() == ModelType.ANOMALY;

    }

    @Override
    protected void enhance(DqModel entity, ModelInput input) {
        entity.setStatus(ModelStatus.DEPLOYED);

        DqModel countModel = createCountModel(input);
        dqModelRepo.addReference(countModel, input.getBasic().getName());
    }

    protected String contentOf(ModelInput input) {
        return input.getExtra().getSrcDb() + "|"
                        + input.getExtra().getSrcDataSet() + "|"
                        + input.getExtra().getAnType();
    }
}
