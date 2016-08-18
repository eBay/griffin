package com.ebay.oss.bark.service.modelcreator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ebay.oss.bark.domain.DqModel;
import com.ebay.oss.bark.service.DqModelCreator;
import com.ebay.oss.bark.vo.ModelInput;

@Component
public class DqModelCreatorChain implements DqModelCreator {
    
    private final List<DqModelCreator> list;

    public DqModelCreatorChain() {
        list = new ArrayList<>();
        list.add(new AccurcyModelCreator());
        list.add(new ValidityModelCreator());
        list.add(new AnomalyModelCreator());
        list.add(new PublishModelCreator());
    }
    
    public DqModelCreatorChain(List<DqModelCreator> list) {
        this.list = list;
    }

    @Override
    public DqModel newModel(ModelInput input) {
        for(DqModelCreator each : list) {
            if(each.isSupport(input)) {
                return each.newModel(input);
            }
        }
        throw new RuntimeException("Unsupported ModelInput" + input.getBasic().getType());
    }

    @Override
    public boolean isSupport(ModelInput input) {
        return true;
    }

}
