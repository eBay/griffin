package com.ebay.oss.bark.service;

import static org.junit.Assert.*;

import com.ebay.oss.bark.domain.DqModel;
import org.junit.Before;
import org.junit.Test;


public class DqModelConverterTest {

    private DqModelConverter converter;

    @Before
    public void setUp() {
        converter = new DqModelConverter();
    }
    
    @Test
    public void testVoOf() {
        assertNull(converter.voOf(null));
        assertNotNull(converter.voOf(new DqModel()));
    }
}
