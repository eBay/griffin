package com.ebay.oss.griffin.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.ebay.oss.griffin.common.Pair;
import com.ebay.oss.griffin.domain.*;
import com.ebay.oss.griffin.repo.DataAssetRepo;
import com.ebay.oss.griffin.repo.DqMetricsRepo;
import com.ebay.oss.griffin.repo.DqModelRepo;
import com.ebay.oss.griffin.repo.SampleFilePathRepo;
import com.ebay.oss.griffin.vo.*;
import com.mongodb.DBObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SystemType.class})
public class DQMetricsServiceImplTest {

    private DQMetricsServiceImpl dqMetricsServiceImpl;

    @Mock
    private DqMetricsRepo mockMetricsRepo;

    @Mock
    private DqModelRepo mockDqModelRepo;

    @Mock
    private RefMetrcsCalc mockRefMetrcsCalc;

    @Mock
    private DqModelService mockDqModelService;

    @Mock
    private SubscribeService mockSubscribeService;

    @Mock
    private DataAssetRepo mockDataAssetRepo;

    @Mock
    private SampleFilePathRepo mockSampleFilePathRepo;

    @Before
    public void setUp() throws IllegalAccessException {
        dqMetricsServiceImpl = new DQMetricsServiceImpl();
        MockitoAnnotations.initMocks(this);

        MemberModifier.field(DQMetricsServiceImpl.class, "metricsRepo")
                .set(dqMetricsServiceImpl, mockMetricsRepo);
        MemberModifier.field(DQMetricsServiceImpl.class, "modelRepo")
                .set(dqMetricsServiceImpl, mockDqModelRepo);
        MemberModifier.field(DQMetricsServiceImpl.class, "refMetricCalc")
                .set(dqMetricsServiceImpl, mockRefMetrcsCalc);
        MemberModifier.field(DQMetricsServiceImpl.class, "dqModelService")
                .set(dqMetricsServiceImpl, mockDqModelService);
        MemberModifier.field(DQMetricsServiceImpl.class, "subscribeService")
                .set(dqMetricsServiceImpl, mockSubscribeService);
        MemberModifier.field(DQMetricsServiceImpl.class, "dataAssetRepo")
                .set(dqMetricsServiceImpl, mockDataAssetRepo);
        MemberModifier.field(DQMetricsServiceImpl.class, "missedFileRepo")
                .set(dqMetricsServiceImpl, mockSampleFilePathRepo);

        Whitebox.setInternalState(DQMetricsServiceImpl.class,
                "totalSystemLevelMetricsList", (SystemLevelMetricsList)null);
    }

    @Test
    public void testInsertMetadata() {
        DqMetricsValue mockMetrics = mock(DqMetricsValue.class);
        when(mockMetrics.getMetricName()).thenReturn("name");
        when(mockMetrics.getTimestamp()).thenReturn(12345L);
        when(mockMetrics.getValue()).thenReturn(23.3f);

        when(mockMetricsRepo.getByCondition(anyListOf(Pair.class)))
                .thenReturn(mock(DBObject.class)).thenReturn(null);
        doNothing().when(mockMetricsRepo).update(any(DqMetricsValue.class), any(DBObject.class));

        InOrder inOrder = inOrder(mockMetricsRepo);

        dqMetricsServiceImpl.insertMetadata(mockMetrics);

        when(mockMetricsRepo.getNextId()).thenReturn(1234L);
        doNothing().when(mockMetricsRepo).save(mockMetrics);

        dqMetricsServiceImpl.insertMetadata(mockMetrics);

        inOrder.verify(mockMetricsRepo).update(any(DqMetricsValue.class), any(DBObject.class));
        inOrder.verify(mockMetricsRepo).save(mockMetrics);
    }

    @Test
    public void testGetLatestlMetricsbyId() {
        when(mockMetricsRepo.getLatestByAssetId(anyString()))
                .thenReturn(mock(DqMetricsValue.class)).thenReturn(null);

        DqMetricsValue metrics1 = dqMetricsServiceImpl.getLatestlMetricsbyId("id1");
        DqMetricsValue metrics2 = dqMetricsServiceImpl.getLatestlMetricsbyId("id2");
        assertNotNull(metrics1);
        assertNull(metrics2);

        verify(mockMetricsRepo, times(2)).getLatestByAssetId(anyString());
    }

    private DqModel newDqModel(String name, int system, float threshold) {
        DqModel model = new DqModel();
        model.setModelName(name);
        model.setSystem(system);
        model.setThreshold(threshold);
        return model;
    }

    private DqMetricsValue newDqMetricsValue(String name, float val) {
        DqMetricsValue value = new DqMetricsValue();
        value.setMetricName(name);
        value.setValue(val);
        return value;
    }

    private void mockData() {
        List<DqModel> models = new ArrayList<DqModel>() {{
            this.add(newDqModel("model1", 1, 10.0f));
            this.add(newDqModel("model2", 2, 10.0f));
            this.add(newDqModel("model3", 2, 50.0f));
        }};
        when(mockDqModelRepo.getAll()).thenReturn(models);

        List<DqMetricsValue> metrics = new ArrayList<DqMetricsValue>() {{
            this.add(newDqMetricsValue("model1", 11.1f));
            this.add(newDqMetricsValue("model2", 22.2f));
            this.add(newDqMetricsValue("model3", 33.3f));
        }};
        when(mockMetricsRepo.getAll()).thenReturn(metrics);

        doNothing().when(mockRefMetrcsCalc).calc(any(SystemLevelMetricsList.class));

        PowerMockito.mockStatic(SystemType.class);
        PowerMockito.when(SystemType.val(1)).thenReturn("system1");
        PowerMockito.when(SystemType.val(2)).thenReturn("system2");
    }

    @Test
    public void testUpdateLatestDQList() {
        mockData();

        dqMetricsServiceImpl.updateLatestDQList();

        verify(mockRefMetrcsCalc).calc(any(SystemLevelMetricsList.class));
    }

    @Test
    public void testBriefMetrics() {
        mockData();

        List<SystemLevelMetrics> sys1 = dqMetricsServiceImpl.briefMetrics("system1");
        assertNotNull(sys1);
        assertEquals(1, sys1.size());
        List<AssetLevelMetrics> metrics1 = sys1.get(0).getMetrics();
        assertNotNull(metrics1);
        assertEquals(1, metrics1.size());

        List<SystemLevelMetrics> sys2 = dqMetricsServiceImpl.briefMetrics("system2");
        assertNotNull(sys2);
        assertEquals(1, sys2.size());
        List<AssetLevelMetrics> metrics2 = sys2.get(0).getMetrics();
        assertNotNull(metrics2);
        assertEquals(2, metrics2.size());

        List<SystemLevelMetrics> sys3 = dqMetricsServiceImpl.briefMetrics("system3");
        assertNotNull(sys3);
        assertEquals(0, sys3.size());
    }

    @Test
    public void testHeatMap() {
        mockData();

        List<SystemLevelMetrics> metrics = dqMetricsServiceImpl.heatMap();
        assertNotNull(metrics);
        assertEquals(2, metrics.size());
    }

    @Test
    public void testAddAssetNames() {
        List<DqModelVo> modelVos = new ArrayList<DqModelVo>();
        DqModelVo modelVo = mock(DqModelVo.class);
        when(modelVo.getName()).thenReturn("model");
        when(modelVo.getAssetName()).thenReturn("asset");
        modelVos.add(modelVo);
        when(mockDqModelService.getAllModles()).thenReturn(modelVos);

        List<SystemLevelMetrics> sysList = new ArrayList<SystemLevelMetrics>();
        SystemLevelMetrics sys = new SystemLevelMetrics("sys");
        List<AssetLevelMetrics> alList = new ArrayList<AssetLevelMetrics>();
        AssetLevelMetrics metric = new AssetLevelMetrics("model");
        alList.add(metric);
        sys.setMetrics(alList);
        sysList.add(sys);

        List<SystemLevelMetrics> sysList1 = dqMetricsServiceImpl.addAssetNames(sysList);
        assertNotNull(sysList1);
        assertEquals(1, sysList1.size());

        List<AssetLevelMetrics> metrics = sysList1.get(0).getMetrics();
        assertEquals(1, metrics.size());
        assertEquals("asset", metrics.get(0).getAssetName());
    }

    @Test
    public void testGetAssetMap() {
        List<DqModelVo> modelVos = new ArrayList<DqModelVo>();
        DqModelVo modelVo = mock(DqModelVo.class);
        when(modelVo.getName()).thenReturn("model");
        when(modelVo.getAssetName()).thenReturn("asset");
        modelVos.add(modelVo);
        when(mockDqModelService.getAllModles()).thenReturn(modelVos);

        Map<String, String> services = dqMetricsServiceImpl.getAssetMap();
        assertNotNull(services);
        assertEquals(1, services.size());
        assertEquals("asset", services.get("model"));
    }

    @Test
    public void testDashboard() {
        mockData();

        DQMetricsServiceImpl spy = spy(dqMetricsServiceImpl);

        List<SystemLevelMetrics> sys1 = spy.dashboard("system1");
        assertNotNull(sys1);
        assertEquals(1, sys1.size());
        List<AssetLevelMetrics> metrics1 = sys1.get(0).getMetrics();
        assertNotNull(metrics1);
        assertEquals(1, metrics1.size());

        List<SystemLevelMetrics> sys2 = spy.dashboard("system2");
        assertNotNull(sys2);
        assertEquals(1, sys2.size());
        List<AssetLevelMetrics> metrics2 = sys2.get(0).getMetrics();
        assertNotNull(metrics2);
        assertEquals(2, metrics2.size());

        List<SystemLevelMetrics> sys3 = spy.dashboard("system3");
        assertNotNull(sys3);
        assertEquals(0, sys3.size());

        verify(spy, times(3)).addAssetNames(anyList());
    }

    @Test
    public void testMydashboard() {
        mockData();

        DQMetricsServiceImpl spy = spy(dqMetricsServiceImpl);

        when(mockSubscribeService.getSubscribe(anyString())).thenReturn(null);
        when(mockDqModelService.getAllModles()).thenReturn(new ArrayList<DqModelVo>());

        List<SystemLevelMetrics> metrics = spy.mydashboard("user");
        assertNotNull(metrics);
        assertEquals(2, metrics.size());

        verify(spy).addAssetNames(anyList());
    }

    @Test
    public void testOneDataCompleteDashboard() {
        mockData();

        AssetLevelMetrics nullMetrics = dqMetricsServiceImpl.oneDataCompleteDashboard("");
        assertNull(nullMetrics);

        AssetLevelMetrics metrics1 = dqMetricsServiceImpl.oneDataCompleteDashboard("model1");
        assertNotNull(metrics1);
        assertEquals(11.1f, metrics1.getDq(), 0.0f);

        AssetLevelMetrics metrics2 = dqMetricsServiceImpl.oneDataCompleteDashboard("model2");
        assertNotNull(metrics2);
        assertEquals(22.2f, metrics2.getDq(), 0.0f);

        AssetLevelMetrics metrics3 = dqMetricsServiceImpl.oneDataCompleteDashboard("model3");
        assertNotNull(metrics3);
        assertEquals(33.3f, metrics3.getDq(), 0.0f);
    }

    @Test
    public void testOneDataBriefDashboard() {
        mockData();

        AssetLevelMetrics nullMetrics = dqMetricsServiceImpl.oneDataBriefDashboard("");
        assertNull(nullMetrics);

        AssetLevelMetrics metrics1 = dqMetricsServiceImpl.oneDataBriefDashboard("model1");
        assertNotNull(metrics1);
        assertEquals(11.1f, metrics1.getDq(), 0.0f);

        AssetLevelMetrics metrics2 = dqMetricsServiceImpl.oneDataBriefDashboard("model2");
        assertNotNull(metrics2);
        assertEquals(22.2f, metrics2.getDq(), 0.0f);

        AssetLevelMetrics metrics3 = dqMetricsServiceImpl.oneDataBriefDashboard("model3");
        assertNotNull(metrics3);
        assertEquals(33.3f, metrics3.getDq(), 0.0f);
    }

    @Test
    public void testGetOverViewStats() {
        mockData();

        when(mockDataAssetRepo.getAll()).thenReturn(new ArrayList<DataAsset>());

        OverViewStatistics overViewStatistics = dqMetricsServiceImpl.getOverViewStats();

        assertNotNull(overViewStatistics);
        assertEquals(0, overViewStatistics.getAssets());
        assertEquals(3, overViewStatistics.getMetrics());

        DQHealthStats health = overViewStatistics.getStatus();
        assertNotNull(health);
        assertEquals(2, health.getHealth());
        assertEquals(1, health.getInvalid());
    }

    @Test
    public void testMetricsForReport() {
        mockData();

        AssetLevelMetrics nullMetrics = dqMetricsServiceImpl.metricsForReport("");
        assertNull(nullMetrics);

        AssetLevelMetrics metrics1 = dqMetricsServiceImpl.metricsForReport("model1");
        assertNotNull(metrics1);
        assertEquals(11.1f, metrics1.getDq(), 0.0f);

        AssetLevelMetrics metrics2 = dqMetricsServiceImpl.metricsForReport("model2");
        assertNotNull(metrics2);
        assertEquals(22.2f, metrics2.getDq(), 0.0f);

        AssetLevelMetrics metrics3 = dqMetricsServiceImpl.metricsForReport("model3");
        assertNotNull(metrics3);
        assertEquals(33.3f, metrics3.getDq(), 0.0f);
    }

    @Test
    public void testListSampleFile() {
        DBObject dbo = mock(DBObject.class);
        when(dbo.get("timestamp")).thenReturn("1234");
        when(dbo.get("hdfsPath")).thenReturn("path");
        List<DBObject> dbObjects = new ArrayList<DBObject>();
        dbObjects.add(dbo);

        when(mockSampleFilePathRepo.findByModelName(anyString())).thenReturn(dbObjects);

        List<SampleOut> samples = dqMetricsServiceImpl.listSampleFile("");
        assertNotNull(samples);
        assertEquals(1, samples.size());
        assertEquals(1234L, samples.get(0).getDate());
        assertEquals("path", samples.get(0).getPath());
    }

    @Test
    public void testInsertSampleFilePath() {
        SampleFilePathLKP lkp = new SampleFilePathLKP();
        lkp.setModelName("name");
        lkp.setHdfsPath("path");
        lkp.setTimestamp(12345L);

        when(mockSampleFilePathRepo.getNextId()).thenReturn(1234L);
        doNothing().when(mockSampleFilePathRepo).save(any(SampleFilePathLKP.class));

        dqMetricsServiceImpl.insertSampleFilePath(lkp);

        ArgumentCaptor<SampleFilePathLKP> clkp = ArgumentCaptor.forClass(SampleFilePathLKP.class);

        verify(mockSampleFilePathRepo).save(clkp.capture());
        assertNotSame(clkp, lkp);
    }
}
