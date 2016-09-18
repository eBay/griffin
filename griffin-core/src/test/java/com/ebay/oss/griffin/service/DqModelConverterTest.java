package com.ebay.oss.griffin.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.ebay.oss.griffin.domain.DqModel;


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
