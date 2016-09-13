package com.ebay.oss.bark.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import com.ebay.oss.bark.common.HDFSUtils;
import com.ebay.oss.bark.domain.*;
import com.ebay.oss.bark.repo.DqModelRepo;
import com.ebay.oss.bark.repo.DqScheduleRepo;
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

import com.ebay.oss.bark.vo.DqModelVo;
import com.ebay.oss.bark.vo.ModelBasicInputNew;
import com.ebay.oss.bark.vo.ModelExtraInputNew;
import com.ebay.oss.bark.vo.ModelInput;

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
//    private String[] newModelName = {"testAccu", "testVali", "testAnom", "testPub"};
//    private String testOwner = "lliu13";
//    private String testPlatform = "Apollo";
//
//    public void testNewModel(int type) {
//        DqModel ret = null;
//
//        if (type == ModelType.ACCURACY) {
//            ModelInput tempModel = new ModelInput();
//            ModelBasicInputNew basic = new ModelBasicInputNew();
//            ModelExtraInputNew extra = new ModelExtraInputNew();
//            basic.setDataaset("rheos_view_event");
//            basic.setDataasetId(16);
//            basic.setDesc("accu for rheos_view_event");
//            basic.setEmail(testOwner + "@ebay.com");
//            basic.setName(newModelName[0]);
//            basic.setOwner(testOwner);
//            basic.setScheduleType(ScheduleType.DAILY);
//            basic.setStatus(ModelStatus.DEPLOYED);
//            basic.setSystem(SystemType.KAFKA);
//            basic.setType(type);
//            extra.setVaType(ValidityType.TOTAL_COUNT);
//            extra.setColumn("u_id");
//            extra.setSrcDataSet("Sojourner.soj_view_event");
//            extra.setSrcDb(testPlatform);
//            extra.setTargetDataSet("Kafka.rheos_view_event");
//            extra.setTargetDb(testPlatform);
//            tempModel.setBasic(basic);
//            tempModel.setExtra(extra);
//            ret = dqModelService.newModel(tempModel);
//        } else if (type == ModelType.VALIDITY) {
//            ModelInput tempModel = new ModelInput();
//            ModelBasicInputNew basic = new ModelBasicInputNew();
//            ModelExtraInputNew extra = new ModelExtraInputNew();
//            basic.setDataaset("dmg");
//            basic.setDataasetId(6);
//            basic.setDesc("total count for dmg");
//            basic.setEmail(testOwner + "@ebay.com");
//            basic.setName(newModelName[1]);
//            basic.setOwner(testOwner);
//            basic.setScheduleType(ScheduleType.DAILY);
//            basic.setStatus(ModelStatus.DEPLOYED);
//            basic.setSystem(SystemType.BULLSEYE);
//            basic.setType(type);
//            extra.setVaType(ValidityType.TOTAL_COUNT);
//            extra.setColumn("age");
//            extra.setSrcDataSet("Bullseye.dmg");
//            extra.setSrcDb(testPlatform);
//            tempModel.setBasic(basic);
//            tempModel.setExtra(extra);
//            ret = dqModelService.newModel(tempModel);
//        } else if (type == ModelType.ANOMALY) {
//            ModelInput tempModel = new ModelInput();
//            ModelBasicInputNew basic = new ModelBasicInputNew();
//            ModelExtraInputNew extra = new ModelExtraInputNew();
//            basic.setDataaset("dmg");
//            basic.setDataasetId(6);
//            basic.setDesc("anomaly for dmg");
//            basic.setEmail(testOwner + "@ebay.com");
//            basic.setName(newModelName[2]);
//            basic.setOwner(testOwner);
//            basic.setScheduleType(ScheduleType.DAILY);
//            basic.setStatus(ModelStatus.DEPLOYED);
//            basic.setSystem(SystemType.BULLSEYE);
//            basic.setType(type);
//            extra.setAnType(AnomalyType.ANOMALY_DEFAULT);
//            extra.setColumn("age");
//            extra.setSrcDataSet("Bullseye.dmg");
//            extra.setSrcDb(testPlatform);
//            tempModel.setBasic(basic);
//            tempModel.setExtra(extra);
//            ret = dqModelService.newModel(tempModel);
//        } else if (type == ModelType.PUBLISH) {
//            ModelInput tempModel = new ModelInput();
//            ModelBasicInputNew basic = new ModelBasicInputNew();
//            ModelExtraInputNew extra = new ModelExtraInputNew();
//            basic.setDataaset("dmg");
//            basic.setDataasetId(6);
//            basic.setDesc("publish metrics for dmg");
//            basic.setEmail(testOwner + "@ebay.com");
//            basic.setName(newModelName[3]);
//            basic.setOwner(testOwner);
//            basic.setScheduleType(ScheduleType.DAILY);
//            basic.setStatus(ModelStatus.DEPLOYED);
//            basic.setSystem(SystemType.BULLSEYE);
//            basic.setType(type);
//            extra.setPublishUrl("http://www.test.com");
//            extra.setColumn("age");
//            extra.setSrcDataSet("Bullseye.dmg");
//            extra.setSrcDb(testPlatform);
//            tempModel.setBasic(basic);
//            tempModel.setExtra(extra);
//            ret = dqModelService.newModel(tempModel);
//        }
//        assertNotNull(ret);
//    }
//
//    private List<DqModelVo> testGetAllModels() {
//        return dqModelService.getAllModles();
//    }
//
//    private void testGetModels() {
//        try {
//            //find success
//            for (int i = 1; i < 4; i++) {
//                ModelInput mi = dqModelService.getModelByName(newModelName[i]);
//                if (i == 1 || i == 2) {
//                    assertEquals(mi.getBasic().getOwner(), testOwner);
//                    assertEquals(mi.getExtra().getSrcDb(), testPlatform);
//                    System.out.println("get model by name: " + mi.getBasic().getName()
//                            + " with owner: " + mi.getBasic().getOwner()
//                            + " and platform: " + mi.getExtra().getSrcDb());
//                } else {
//                    assertEquals(mi.getBasic().getOwner(), testOwner);
//                    assertEquals(mi.getExtra().getPublishUrl(), "http://www.test.com");
//                    System.out.println("get model by name: " + mi.getBasic().getName()
//                            + " with owner: " + mi.getBasic().getOwner()
//                            + " and publish url: " + mi.getExtra().getPublishUrl());
//                }
//            }
//            //find fail
//            ModelInput mi = dqModelService.getModelByName("findfailmodel");
//            assertNull(mi);
//        } catch (Exception e) {
//            System.out.println("fail to get model");
//            e.printStackTrace();
//        }
//    }
//
//    private void testDeleteModel(String name) {
//        try {
//            int del = dqModelService.deleteModel(name);
//            assertTrue(del <= 0);
//            System.out.println("delete model success");
//        } catch (Exception e) {
//            System.out.println("fail to delete model");
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testDQModelService(){
//
//        //get all models
//        List<DqModelVo> modelsList = testGetAllModels();
//        System.out.println("current models count: " + modelsList.size());
//
//        //add new models success
//        testNewModel(ModelType.ACCURACY);
//        testNewModel(ModelType.VALIDITY);
//        testNewModel(ModelType.ANOMALY);
//        testNewModel(ModelType.PUBLISH);
//
//        //get all models
//        List<DqModelVo> modelsList1 = testGetAllModels();
//        assertTrue(modelsList1.size() - modelsList.size() == 4);
//        System.out.println("current models count: " + modelsList1.size());
//
//        //add the same new model fail
//        testNewModel(ModelType.VALIDITY);
//
//        //find new add model
//        testGetModels();
//
//        //delete models
//        //TODO:: move to tearDown
//        for (int i = 0; i < newModelName.length; i++){
//            testDeleteModel(newModelName[i]);
//        }
//    }
//
//    @Test
//    public void testGtGeneralModeltAndEnableSchedule4Model() {
//        String name = "test_accuracy_1";
//        DqModel me = dqModelService.getGeneralModel(name);
//        assertNotNull(me);
//
//        dqModelService.enableSchedule4Model(me);
//    }
}
