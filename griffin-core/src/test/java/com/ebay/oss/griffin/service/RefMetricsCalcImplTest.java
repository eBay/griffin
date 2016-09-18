package com.ebay.oss.griffin.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.support.membermodification.MemberMatcher.method;

import java.util.*;

import com.ebay.oss.griffin.domain.*;
import com.ebay.oss.griffin.repo.DqMetricsRepo;
import com.ebay.oss.griffin.vo.AssetLevelMetricsDetail;
import com.ebay.oss.griffin.vo.BollingerBandsEntity;
import com.ebay.oss.griffin.vo.SystemLevelMetricsList;
import org.junit.Before;
import org.junit.Test;
import com.ebay.oss.griffin.repo.DqModelRepo;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;


@RunWith(PowerMockRunner.class)
@PrepareForTest({RefMetricsCalcImpl.class, SystemType.class})
public class RefMetricsCalcImplTest {

    private RefMetricsCalcImpl refMetricsCalcImpl;

    @Before
    public void setUp() {
        refMetricsCalcImpl = new RefMetricsCalcImpl();
    }

    @SuppressWarnings("serial")
    @Test
    public void test_getReferences() {
        refMetricsCalcImpl.modelRepo = mock(DqModelRepo.class);

        List<DqModel> models = new ArrayList<>();
        models.add(newModelWithRef("model1", "ref1"));
        models.add(newModelWithRef("model2", "ref2_0, ref2_1, ref2_2"));
        models.add(newModelWithRef("model2", null));
        models.add(newModelWithRef("model2", ""));
        when(refMetricsCalcImpl.modelRepo.getAll()).thenReturn(models);

        Map<String, List<String>> expect = new HashMap<String, List<String>>() {{
            this.put("model1", new ArrayList<String>() {{
                this.add("ref1");
            }});
            this.put("model2", new ArrayList<String>() {{
              this.add("ref2_0");
              this.add("ref2_1");
              this.add("ref2_2");
            }});
        }};

        Map<String, List<String>> actual = refMetricsCalcImpl.getReferences();

        assertEquals(expect, actual);

        verify(refMetricsCalcImpl.modelRepo).getAll();
    }

    private DqModel newModelWithRef(String name, String ref) {
        DqModel model = new DqModel();
        model.setModelName(name);
        model.setReferenceModel(ref);
        return model;
    }

    @Test
    public void testGetSystemType() {
        refMetricsCalcImpl.modelRepo = mock(DqModelRepo.class);

        List<DqModel> models = new ArrayList<DqModel>() {{
            this.add(newModelWithSystem("model1", 1));
            this.add(newModelWithSystem("model2", 2));
        }};
        when(refMetricsCalcImpl.modelRepo.getAll()).thenReturn(models);

        PowerMockito.mockStatic(SystemType.class);
        PowerMockito.when(SystemType.val(1)).thenReturn("system1");
        PowerMockito.when(SystemType.val(2)).thenReturn("system2");

        String sys1 = refMetricsCalcImpl.getSystemType("model1");
        assertEquals("system1", sys1);

        String sys2 = refMetricsCalcImpl.getSystemType("model2");
        assertEquals("system2", sys2);

        String sys3 = refMetricsCalcImpl.getSystemType("model3");
        assertNull(sys3);

        verify(refMetricsCalcImpl.modelRepo).getAll();
    }

    private DqModel newModelWithSystem(String name, int system) {
        DqModel model = new DqModel();
        model.setModelName(name);
        model.setSystem(system);
        return model;
    }

    @Test
    public void testCalc() throws Exception {
        RefMetricsCalcImpl spyRefMetricsCalcImpl = PowerMockito.spy(refMetricsCalcImpl);

        Map<String, List<String>> references = new HashMap<String, List<String>>() {{
            this.put("model1", new ArrayList<String>() {{
                this.add("ref1");
                this.add("ref2");
            }});
        }};
        doReturn(references).when(spyRefMetricsCalcImpl).getReferences();

        SystemLevelMetricsList mockSystemLevelMetricsList = mock(SystemLevelMetricsList.class);

        PowerMockito.doNothing().when(spyRefMetricsCalcImpl, "calc",
                anyString(), anyString(), any(SystemLevelMetricsList.class));

        spyRefMetricsCalcImpl.calc(mockSystemLevelMetricsList);

        PowerMockito.verifyPrivate(spyRefMetricsCalcImpl, times(2))
                .invoke("calc", anyString(), anyString(), any(SystemLevelMetricsList.class));
    }

    @Test
    public void testCalcWithNames() throws Exception {
        refMetricsCalcImpl.dqModelService = mock(DqModelService.class);
        doReturn(null).doReturn(new DqModel())
                .doReturn(newModelWithTypeContent("model", ModelType.ACCURACY, ""))
                .doReturn(newModelWithTypeContent("model",
                        ModelType.ANOMALY, "1|2|" + AnomalyType.HISTORY_TREND))
                .doReturn(newModelWithTypeContent("model",
                        ModelType.ANOMALY, "1|2|" + AnomalyType.BOLLINGER_BANDS))
                .doReturn(newModelWithTypeContent("model",
                        ModelType.ANOMALY, "1|2|" + AnomalyType.MAD))
                .when(refMetricsCalcImpl.dqModelService).getGeneralModel("ref");
        doReturn(null).doReturn(new DqModel())
                .when(refMetricsCalcImpl.dqModelService).getGeneralModel("model");

        refMetricsCalcImpl.metricsRepo = mock(DqMetricsRepo.class);
        List<DqMetricsValue> metricsValues = new ArrayList<>();
        doReturn(metricsValues).when(refMetricsCalcImpl.metricsRepo).getByMetricsName(anyString());

        RefMetricsCalcImpl spyRefMetricsCalcImpl = PowerMockito.spy(refMetricsCalcImpl);

        PowerMockito.doNothing().when(spyRefMetricsCalcImpl, "calcHistoryRefModel",
                any(DqModel.class), any(DqModel.class),
                anyListOf(DqMetricsValue.class), any(SystemLevelMetricsList.class));
        PowerMockito.doNothing().when(spyRefMetricsCalcImpl, "calcBollingerRefModel",
                anyString(), anyString(), anyListOf(DqMetricsValue.class),
                any(SystemLevelMetricsList.class));
        PowerMockito.doNothing().when(spyRefMetricsCalcImpl, "calcMad",
                any(DqModel.class), any(DqModel.class), any(DqModel.class),
                anyListOf(DqMetricsValue.class), any(SystemLevelMetricsList.class));

        SystemLevelMetricsList slm = new SystemLevelMetricsList();

        int counter = 0;
        System.out.println("===== calc with names " + (++counter) + " =====");
        Whitebox.invokeMethod(spyRefMetricsCalcImpl, "calc", "model", "ref", slm);
        System.out.println("===== calc with names " + (++counter) + " =====");
        Whitebox.invokeMethod(spyRefMetricsCalcImpl, "calc", "model", "ref", slm);
        System.out.println("===== calc with names " + (++counter) + " =====");
        Whitebox.invokeMethod(spyRefMetricsCalcImpl, "calc", "model", "ref", slm);
        System.out.println("===== calc with names " + (++counter) + " =====");
        Whitebox.invokeMethod(spyRefMetricsCalcImpl, "calc", "model", "ref", slm);
        System.out.println("===== calc with names " + (++counter) + " =====");
        Whitebox.invokeMethod(spyRefMetricsCalcImpl, "calc", "model", "ref", slm);
        System.out.println("===== calc with names " + (++counter) + " =====");
        Whitebox.invokeMethod(spyRefMetricsCalcImpl, "calc", "model", "ref", slm);

        PowerMockito.verifyPrivate(spyRefMetricsCalcImpl)
                .invoke("calcHistoryRefModel", any(DqModel.class), any(DqModel.class),
                        anyListOf(DqMetricsValue.class), any(SystemLevelMetricsList.class));
        PowerMockito.verifyPrivate(spyRefMetricsCalcImpl)
                .invoke("calcBollingerRefModel", anyString(), anyString(),
                        anyListOf(DqMetricsValue.class), any(SystemLevelMetricsList.class));
        PowerMockito.verifyPrivate(spyRefMetricsCalcImpl)
                .invoke("calcMad", any(DqModel.class), any(DqModel.class), any(DqModel.class),
                        anyListOf(DqMetricsValue.class), any(SystemLevelMetricsList.class));
    }

    private DqModel newModelWithTypeContent(String name, int type, String content) {
        DqModel model = new DqModel();
        model.setModelName(name);
        model.setModelType(type);
        model.setModelContent(content);
        return model;
    }

    @Test
    public void testCalcHistoryRefModel() {
        DqModel srcModel = newModelWithScheduleThreshold("model", ScheduleType.DAILY, 0.1f);
        DqModel refModel = newModelWithScheduleThreshold("ref", ScheduleType.DAILY, 0.1f);

        long time = new Date().getTime();
        List<DqMetricsValue> metricsValues = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            metricsValues.add(new DqMetricsValue("model", time + i * 1000 * 3600 * 24, 11.0f - 0.1f * i));
        }

        SystemLevelMetricsList spySlm = spy(new SystemLevelMetricsList());

        RefMetricsCalcImpl spyRefMetricsCalcImpl = PowerMockito.spy(refMetricsCalcImpl);
        doReturn("system").when(spyRefMetricsCalcImpl).getSystemType(anyString());

        spyRefMetricsCalcImpl.calcHistoryRefModel(srcModel, refModel, metricsValues, spySlm);

        verify(spySlm, atLeastOnce()).upsertNewAssetExecute(anyString(), anyString(), anyLong(), anyFloat(), anyString(),
                anyInt(), anyBoolean(), any(AssetLevelMetricsDetail.class));
    }

    private DqModel newModelWithScheduleThreshold(String name, int schedule, float threshold) {
        DqModel model = new DqModel();
        model.setModelName(name);
        model.setSchedule(schedule);
        model.setThreshold(threshold);
        return model;
    }

    @Test
    public void testCalcMad() {
        String modelName = "model";
        String refName = "ref";
        DqModel refModel = new DqModel();

        long time = new Date().getTime();
        List<DqMetricsValue> metricsValues = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            metricsValues.add(new DqMetricsValue("model", time + i * 1000 * 3600 * 24, 11.0f - 0.1f * i));
        }

        SystemLevelMetricsList spySlm = spy(new SystemLevelMetricsList());

        RefMetricsCalcImpl spyRefMetricsCalcImpl = PowerMockito.spy(refMetricsCalcImpl);
        doReturn("system").when(spyRefMetricsCalcImpl).getSystemType(anyString());

        spyRefMetricsCalcImpl.calcMad(modelName, refName, refModel, metricsValues, spySlm);

        verify(spySlm, atLeastOnce()).upsertNewAssetExecute(anyString(), anyString(), anyLong(), anyFloat(), anyString(),
                anyInt(), anyBoolean(), any(AssetLevelMetricsDetail.class));
    }

    @Test
    public void testCalcBollingerRefModel() {
        String modelName = "model";
        String refName = "ref";

        long time = new Date().getTime();
        List<DqMetricsValue> metricsValues = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            metricsValues.add(new DqMetricsValue("model", time + i * 1000 * 3600 * 24, 11.0f - 0.1f * i));
        }

        SystemLevelMetricsList spySlm = spy(new SystemLevelMetricsList());

        RefMetricsCalcImpl spyRefMetricsCalcImpl = PowerMockito.spy(refMetricsCalcImpl);
        doReturn("system").when(spyRefMetricsCalcImpl).getSystemType(anyString());

        spyRefMetricsCalcImpl.calcBollingerRefModel(modelName, refName, metricsValues, spySlm);

        verify(spySlm, atLeastOnce()).upsertNewAssetExecute(anyString(), anyString(), anyLong(), anyFloat(), anyString(),
                anyInt(), anyBoolean(), any(AssetLevelMetricsDetail.class));
    }
}
