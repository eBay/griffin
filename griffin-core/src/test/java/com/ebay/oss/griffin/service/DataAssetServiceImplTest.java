package com.ebay.oss.griffin.service;

import static org.junit.Assert.*;

import java.util.*;

import com.ebay.oss.griffin.common.HDFSUtils;
import com.ebay.oss.griffin.common.Pair;
import com.ebay.oss.griffin.domain.DqModel;
import com.ebay.oss.griffin.error.BarkDbOperationException;
import com.ebay.oss.griffin.repo.DataAssetRepo;
import com.ebay.oss.griffin.repo.DqModelRepo;
import com.ebay.oss.griffin.vo.ModelInput;
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
import org.springframework.core.env.Environment;

import com.ebay.oss.griffin.domain.DataAsset;
import com.ebay.oss.griffin.vo.DataAssetInput;
import com.ebay.oss.griffin.vo.PlatformMetadata;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HDFSUtils.class})
public class DataAssetServiceImplTest {

    private DataAssetServiceImpl dataAssetServiceImpl;

    @Mock
    private DataAssetRepo mockDataAssetRepo;

    @Mock
    private DqModelService mockDqModelService;

    @Mock
    private DqModelRepo mockDqModelRepo;

    @Mock
    private Environment mockEnv;

    @Before
    public void setUp() throws IllegalAccessException {
        dataAssetServiceImpl = new DataAssetServiceImpl();
        MockitoAnnotations.initMocks(this);

        MemberModifier.field(DataAssetServiceImpl.class, "dataAssetRepo")
                .set(dataAssetServiceImpl, mockDataAssetRepo);
        MemberModifier.field(DataAssetServiceImpl.class, "dqModelService")
                .set(dataAssetServiceImpl, mockDqModelService);
        MemberModifier.field(DataAssetServiceImpl.class, "env")
                .set(dataAssetServiceImpl, mockEnv);
        MemberModifier.field(DataAssetServiceImpl.class, "dqModelRepo")
                .set(dataAssetServiceImpl, mockDqModelRepo);
    }

    @Test
    public void testGetAllDataAssets() {
        List<DataAsset> dataAssets = new LinkedList<DataAsset>();
        DataAsset mockDataAsset = mock(DataAsset.class);
        dataAssets.add(mockDataAsset);
        when(mockDataAssetRepo.getAll()).thenReturn(dataAssets);

        List<DataAsset> allDataAssets = dataAssetServiceImpl.getAllDataAssets();

        verify(mockDataAssetRepo).getAll();
        assertNotNull(allDataAssets);
        assertEquals(1, allDataAssets.size());
    }

    @Test
    public void testGetSourceTree() {
        List<DataAsset> dataAssets = new LinkedList<DataAsset>();
        DataAsset mockDataAsset = mock(DataAsset.class);
        when(mockDataAsset.getPlatform()).thenReturn("platform");
        when(mockDataAsset.getSystem()).thenReturn("system");
        dataAssets.add(mockDataAsset);
        when(mockDataAssetRepo.getAll()).thenReturn(dataAssets);

        List<PlatformMetadata> platformMetadatas = dataAssetServiceImpl.getSourceTree();

        verify(mockDataAssetRepo).getAll();
        assertNotNull(platformMetadatas);
        assertEquals(1, platformMetadatas.size());
    }

    @Test
    public void testCreateDataAsset() {
        DataAssetInput mockDataAssetInput = mock(DataAssetInput.class);
        when(mockDataAssetInput.getAssetHDFSPath()).thenReturn("path");

        InOrder inOrder = inOrder(mockDataAssetRepo, mockDqModelService);

        when(mockDataAssetRepo.getByCondition(anyListOf(Pair.class)))
                .thenReturn(mock(DBObject.class)).thenReturn(null);

        try {
            int success = dataAssetServiceImpl.createDataAsset(mockDataAssetInput);
            fail();
        } catch (BarkDbOperationException e) {
            assertEquals("Record already existing", e.getMessage());
        }

        when(mockEnv.getProperty("env")).thenReturn("prod");
        PowerMockito.mockStatic(HDFSUtils.class);
        PowerMockito.when(HDFSUtils.checkHDFSFolder("path"))
                .thenReturn(false).thenReturn(true);

        try {
            int success = dataAssetServiceImpl.createDataAsset(mockDataAssetInput);
            fail();
        } catch (BarkDbOperationException e) {
            assertEquals("Hdfs Path is invalid, please input valid hdfs path!", e.getMessage());
        }

        when(mockDataAssetRepo.getNextId()).thenReturn(1234L);
        doNothing().when(mockDataAssetRepo).save(any(DataAsset.class));

        when(mockDqModelService.newModel(any(ModelInput.class)))
                .thenThrow(new BarkDbOperationException()).thenReturn(null);

        try {
            int success = dataAssetServiceImpl.createDataAsset(mockDataAssetInput);
            fail();
        } catch (BarkDbOperationException e) {
            assertEquals("Failed to create a new data asset", e.getMessage());
        }

        try {
            int success = dataAssetServiceImpl.createDataAsset(mockDataAssetInput);
            assertEquals(0, success);
        } catch (BarkDbOperationException e) {
            e.printStackTrace();
        }

        inOrder.verify(mockDataAssetRepo).save(any(DataAsset.class));
        inOrder.verify(mockDqModelService).newModel(any(ModelInput.class));
        inOrder.verify(mockDataAssetRepo).save(any(DataAsset.class));
        inOrder.verify(mockDqModelService).newModel(any(ModelInput.class));
    }

    @Test
    public void testUpdateDataAsset() {
        DataAssetInput mockDataAssetInput = mock(DataAssetInput.class);

        DBObject mockDBObject = mock(DBObject.class);
        when(mockDBObject.get("_id")).thenReturn("1234");

        when(mockDataAssetRepo.getByCondition(anyListOf(Pair.class)))
                .thenReturn(null).thenReturn(mockDBObject);

        try {
            dataAssetServiceImpl.updateDataAsset(mockDataAssetInput);
            fail();
        } catch (BarkDbOperationException e) {
            assertEquals("Failed to update data asset", e.getMessage());
        }

        doNothing().when(mockDataAssetRepo).update(any(DataAsset.class), any(DBObject.class));

        try {
            dataAssetServiceImpl.updateDataAsset(mockDataAssetInput);
        } catch (BarkDbOperationException e) {
            e.printStackTrace();
        }

        ArgumentCaptor<DataAsset> da = ArgumentCaptor.forClass(DataAsset.class);

        verify(mockDataAssetRepo).update(da.capture(), any(DBObject.class));
        assertEquals(new Long(1234L), da.getValue().get_id());
    }

    @Test
    public void testGetDataAssetById() {
        when(mockDataAssetRepo.getById(anyLong()))
                .thenReturn(mock(DataAsset.class)).thenReturn(null);

        DataAsset dataAsset1 = dataAssetServiceImpl.getDataAssetById(1234L);
        DataAsset dataAsset2 = dataAssetServiceImpl.getDataAssetById(4321L);
        assertNotNull(dataAsset1);
        assertNull(dataAsset2);

        verify(mockDataAssetRepo, times(2)).getById(anyLong());
    }

    @Test
    public void testRemoveAssetById() {
        DqModel mockModel = mock(DqModel.class);
        List<DqModel> models = new ArrayList<DqModel>();
        models.add(mockModel);
        when(mockDqModelRepo.getByDataAsset(any(DataAsset.class))).thenReturn(models);
        when(mockModel.getModelName()).thenReturn("name");

        when(mockDqModelService.deleteModel("name")).thenReturn(0);

        when(mockDataAssetRepo.getById(anyLong()))
                .thenReturn(null).thenReturn(mock(DataAsset.class));
        doThrow(Exception.class).doNothing().when(mockDataAssetRepo).delete(anyLong());

        try {
            dataAssetServiceImpl.removeAssetById(1234L);
            fail();
        } catch (BarkDbOperationException e) {
            assertEquals("Failed to delete data asset with id of '" + 1234L + "'", e.getMessage());
        }

        try {
            dataAssetServiceImpl.removeAssetById(1234L);
        } catch (BarkDbOperationException e) {
            e.printStackTrace();
        }

        verify(mockDataAssetRepo, times(2)).delete(anyLong());
    }
}

