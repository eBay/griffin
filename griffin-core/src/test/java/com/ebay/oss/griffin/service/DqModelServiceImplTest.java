package com.ebay.oss.griffin.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import com.ebay.oss.griffin.common.HDFSUtils;
import com.ebay.oss.griffin.domain.*;
import com.ebay.oss.griffin.repo.DqModelRepo;
import com.ebay.oss.griffin.repo.DqScheduleRepo;
import com.mongodb.DBObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ebay.oss.griffin.vo.DqModelVo;
import com.ebay.oss.griffin.vo.ModelBasicInputNew;
import com.ebay.oss.griffin.vo.ModelExtraInputNew;
import com.ebay.oss.griffin.vo.ModelInput;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HDFSUtils.class})
public class DqModelServiceImplTest {

    private DqModelServiceImpl dqModelServiceImpl;

    @Mock
    private DqModelRepo mockDqModelRepo;

    @Mock
    private Converter<DqModel, DqModelVo> mockVoConverter;

    @Mock
    private DqScheduleRepo mockDqScheduleRepo;

    @Mock
    private Converter<DqModel, ModelInput> mockModelInputConverter;

    @Mock
    private DqModelCreator mockDqModelCreator;

    @Before
    public void setUp() throws IllegalAccessException {
        dqModelServiceImpl = new DqModelServiceImpl();
        MockitoAnnotations.initMocks(this);

        MemberModifier.field(DqModelServiceImpl.class, "dqModelRepo")
                .set(dqModelServiceImpl, mockDqModelRepo);
        MemberModifier.field(DqModelServiceImpl.class, "converter")
                .set(dqModelServiceImpl, mockVoConverter);
        MemberModifier.field(DqModelServiceImpl.class, "scheduleRepo")
                .set(dqModelServiceImpl, mockDqScheduleRepo);
        MemberModifier.field(DqModelServiceImpl.class, "modelInputConverter")
                .set(dqModelServiceImpl, mockModelInputConverter);
        MemberModifier.field(DqModelServiceImpl.class, "modelCreator")
                .set(dqModelServiceImpl, mockDqModelCreator);
    }

    @Test
    public void testGetAllModles() {
        List<DqModel> models = new ArrayList<DqModel>() {{
            this.add(new DqModel());
            this.add(new DqModel());
            this.add(new DqModel());
        }};
        when(mockDqModelRepo.getAll()).thenReturn(models);
        when(mockVoConverter.voOf(any(DqModel.class))).thenReturn(new DqModelVo());

        List<DqModelVo> vos = dqModelServiceImpl.getAllModles();

        assertNotNull(vos);
        assertEquals(3, vos.size());
    }

    @Test
    public void testDeleteModel() {
        DqModel model1 = new DqModel();
        model1.setModelType(ModelType.ACCURACY);
        DqModel model2 = new DqModel();
        model2.setModelType(ModelType.VALIDITY);
        model2.setAssetId(123L);
        DqModel model3 = new DqModel();
        model3.setModelType(ModelType.VALIDITY);
        model3.setAssetId(234L);

        when(mockDqModelRepo.findByName(anyString()))
                .thenReturn(model1).thenReturn(model2).thenReturn(model3);
        doNothing().when(mockDqModelRepo).delete(anyLong());

        doNothing().when(mockDqScheduleRepo).deleteByModelList(anyString());

        DBObject dbo1 = mock(DBObject.class);
        when(dbo1.get("modelList")).thenReturn("model");
        when(dbo1.put(anyString(), anyString())).thenReturn(null);
        doReturn(dbo1).when(mockDqScheduleRepo).getValiditySchedule(123L);

        DBObject dbo2 = mock(DBObject.class);
        when(dbo2.get("modelList")).thenReturn("123-=-321");
        when(dbo2.put(anyString(), anyString())).thenReturn(null);
        doReturn(dbo2).when(mockDqScheduleRepo).getValiditySchedule(234L);

        doNothing().when(mockDqScheduleRepo).updateModelType(any(DBObject.class), anyInt());
        doNothing().when(mockDqScheduleRepo).deleteByModelList(anyString());

        int del1 = dqModelServiceImpl.deleteModel("model");
        assertEquals(0, del1);
        verify(mockDqScheduleRepo).deleteByModelList(anyString());

        int del2 = dqModelServiceImpl.deleteModel("model");
        assertEquals(0, del2);
        verify(mockDqScheduleRepo, times(2)).deleteByModelList(anyString());

        int del3 = dqModelServiceImpl.deleteModel("model");
        assertEquals(0, del3);
        verify(mockDqScheduleRepo).updateModelType(any(DBObject.class), anyInt());
    }

    @Test
    public void testGetGeneralModel() {
        when(mockDqModelRepo.findByName(anyString())).thenReturn(new DqModel());
        DqModel model = dqModelServiceImpl.getGeneralModel("");
        assertNotNull(model);
        verify(mockDqModelRepo).findByName(anyString());
    }

    @Test
    public void testGetModelByName() {
        when(mockDqModelRepo.findByName(anyString())).thenReturn(new DqModel());
        when(mockModelInputConverter.voOf(any(DqModel.class))).thenReturn(new ModelInput());
        ModelInput input = dqModelServiceImpl.getModelByName("");
        assertNotNull(input);
        verify(mockDqModelRepo).findByName(anyString());
        verify(mockModelInputConverter).voOf(any(DqModel.class));
    }

    @Test
    public void testEnableSchedule4Model() {
        DqModel model1 = newModelWithType("model1", ModelType.ACCURACY);
        DqModel model2 = newModelWithType("model2", ModelType.VALIDITY);
        DqModel model3 = newModelWithType("model3", ModelType.ANOMALY);
        DqModel model4 = newModelWithType("model4", ModelType.PUBLISH);

        doReturn(null).when(mockDqModelRepo).update(any(DqModel.class));
        when(mockDqScheduleRepo.getNextId()).thenReturn(1234L);

        DBObject dbo = mock(DBObject.class);
        when(dbo.get("modelList")).thenReturn("model");
        when(mockDqScheduleRepo.getValiditySchedule(anyLong())).thenReturn(dbo);

        doNothing().when(mockDqScheduleRepo).updateByModelType(any(DqSchedule.class), anyInt());

        dqModelServiceImpl.enableSchedule4Model(model1);
        dqModelServiceImpl.enableSchedule4Model(model2);
        dqModelServiceImpl.enableSchedule4Model(model3);
        dqModelServiceImpl.enableSchedule4Model(model4);

        verify(mockDqScheduleRepo, times(2)).updateByModelType(any(DqSchedule.class), anyInt());
    }

    private DqModel newModelWithType(String name, int type) {
        DqModel model = new DqModel();
        model.setModelName(name);
        model.setModelType(type);
        return model;
    }

    @Test
    public void testNewModel() {
        when(mockDqModelCreator.newModel(any(ModelInput.class))).thenReturn(new DqModel());
        DqModel model = dqModelServiceImpl.newModel(new ModelInput());
        assertNotNull(model);
        verify(mockDqModelCreator).newModel(any(ModelInput.class));
    }
}
