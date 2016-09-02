package com.ebay.oss.bark.service;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.ebay.oss.bark.error.BarkDbOperationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ebay.oss.bark.domain.DataAsset;
import com.ebay.oss.bark.vo.DataAssetInput;
import com.ebay.oss.bark.vo.PlatformMetadata;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:context.xml"})
public class DataAssetServiceImplTest {
    @Autowired
    private DataAssetServiceImpl dataAssetService;

    @Test
    public void testGetSourceTree() {
        System.out.println("===== Get platfrom metadatas =====");
        List<PlatformMetadata> platformMetadatas = dataAssetService.getSourceTree();
        assertNotNull(platformMetadatas);
        System.out.println();
    }

    @Test
    public void testGetAllDataAssets() {
        System.out.println("===== Get all data assets =====");
        List<DataAsset> dataAssets = dataAssetService.getAllDataAssets();
        assertNotNull(dataAssets);
        System.out.println();
    }

    private DataAsset getDataAssetByName(String name) {
        List<DataAsset> dataAssets = dataAssetService.getAllDataAssets();
        for (DataAsset asset : dataAssets) {
            if (name.equals(asset.getAssetName())) {
                return asset;
            }
        }
        return null;
    }

    @Test
    public void testDataAssetLifeCycle() throws BarkDbOperationException {
        System.out.println("===== Create data asset =====");
        DataAssetInput newData = new DataAssetInput();
        try {
            newData.setAssetName("testAsset");
            newData.setAssetType("testType");
            newData.setSystem("testSystem");
            newData.setAssetHDFSPath("/testPath");
            newData.setPlatform("testPlatform");
            newData.setOwner("testOwner");
            int success = dataAssetService.createDataAsset(newData);
            assertEquals(0, success);
        } catch (BarkDbOperationException e) {
            System.out.println("***** Fail: Create data asset *****");
            throw e;
        }
        System.out.println();

        DataAsset asset = getDataAssetByName("testAsset");
        assertNotNull(asset);
        System.out.println("===== Get data asset id: " + asset.get_id() + " =====");
        System.out.println();

        System.out.println("===== Get data asset by id: " + asset.get_id() + " =====");
        try {
            DataAsset existAsset = dataAssetService.getDataAssetById(asset.get_id());
            assertNotNull(existAsset);
            assertEquals("testSystem", existAsset.getSystem());
            assertEquals("testOwner", existAsset.getOwner());
            DataAsset nonExistAsset = dataAssetService.getDataAssetById(863510L);
            assertNull(nonExistAsset);
        } catch (BarkDbOperationException e) {
            System.out.println("***** Fail: Get data asset by id *****");
            throw e;
        }
        System.out.println();

        System.out.println("===== Update data asset =====");
        try {
            newData.setOwner("newOwner");
            dataAssetService.updateDataAsset(newData);
            DataAsset findAsset = dataAssetService.getDataAssetById(asset.get_id());
            assertNotNull(findAsset);
            assertEquals("newOwner", findAsset.getOwner());
        } catch (BarkDbOperationException e) {
            System.out.println("***** Fail: Update data asset *****");
            throw e;
        }
        System.out.println();

        System.out.println("===== Remove data asset =====");
        try {
            dataAssetService.removeAssetById(asset.get_id());
            DataAsset nonExistAsset = dataAssetService.getDataAssetById(asset.get_id());
            assertNull(nonExistAsset);
        } catch (BarkDbOperationException e) {
            System.out.println("***** Fail: Remove data asset *****");
            throw e;
        }
        System.out.println();
    }
}

