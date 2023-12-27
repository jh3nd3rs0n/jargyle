package com.github.jh3nd3rs0n.jargyle.common.net;

import org.junit.Assert;
import org.junit.Test;

public class PortRangeTest {

	@Test
	public void testContains01() {
		Assert.assertTrue(PortRange.newInstance(Port.newInstance(Port.MIN_INT_VALUE), Port.newInstance(Port.MAX_INT_VALUE)).contains(Port.newInstance(Port.MIN_INT_VALUE)));
	}

	@Test
	public void testContains02() {
		Assert.assertTrue(PortRange.newInstance(Port.newInstance(Port.MIN_INT_VALUE), Port.newInstance(Port.MAX_INT_VALUE)).contains(Port.newInstance(Port.MAX_INT_VALUE)));
	}

	@Test
	public void testContains03() {
		Assert.assertTrue(PortRange.newInstance(Port.newInstance(Port.MIN_INT_VALUE), Port.newInstance(Port.MAX_INT_VALUE)).contains(Port.newInstance(1080)));
	}

	@Test
	public void testContains04() {
		Assert.assertTrue(PortRange.newInstance(Port.newInstance(Port.MIN_INT_VALUE)).contains(Port.newInstance(Port.MIN_INT_VALUE)));
	}

	@Test
	public void testContains05() {
		Assert.assertTrue(PortRange.newInstance(Port.newInstance(Port.MAX_INT_VALUE)).contains(Port.newInstance(Port.MAX_INT_VALUE)));
	}

	@Test
	public void testContains06() {
		Assert.assertTrue(PortRange.newInstance(Port.newInstance(1080)).contains(Port.newInstance(1080)));
	}
	
	@Test
	public void testContains07() {
		Assert.assertTrue(PortRange.newInstance(Port.newInstance(1000), Port.newInstance(2000)).contains(Port.newInstance(1000)));
	}
	
	@Test
	public void testContains08() {
		Assert.assertTrue(PortRange.newInstance(Port.newInstance(1000), Port.newInstance(2000)).contains(Port.newInstance(2000)));
	}
	
	@Test
	public void testContains09() {
		Assert.assertTrue(PortRange.newInstance(Port.newInstance(1000), Port.newInstance(2000)).contains(Port.newInstance(1080)));
	}

}
