package org.apache.bark.scheduler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BarkSchedulerTest {
	@Test
	public void testSayHello(){
		BarkScheduler sch = new BarkScheduler();
		sch.sayHello();
		assertEquals("Hello", sch.sayHello());

	}

}
