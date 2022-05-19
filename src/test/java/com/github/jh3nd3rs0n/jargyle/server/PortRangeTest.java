package com.github.jh3nd3rs0n.jargyle.server;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;

public class PortRangeTest {

	@Test
	public void testContains01() {
		assertTrue(PortRange.newInstance(Port.newInstance(Port.MIN_INT_VALUE), Port.newInstance(Port.MAX_INT_VALUE)).contains(Port.newInstance(Port.MIN_INT_VALUE)));
	}

	@Test
	public void testContains02() {
		assertTrue(PortRange.newInstance(Port.newInstance(Port.MIN_INT_VALUE), Port.newInstance(Port.MAX_INT_VALUE)).contains(Port.newInstance(Port.MAX_INT_VALUE)));
	}

	@Test
	public void testContains03() {
		assertTrue(PortRange.newInstance(Port.newInstance(Port.MIN_INT_VALUE), Port.newInstance(Port.MAX_INT_VALUE)).contains(Port.newInstance(1080)));
	}

	@Test
	public void testContains04() {
		assertTrue(PortRange.newInstance(Port.newInstance(Port.MIN_INT_VALUE)).contains(Port.newInstance(Port.MIN_INT_VALUE)));
	}

	@Test
	public void testContains05() {
		assertTrue(PortRange.newInstance(Port.newInstance(Port.MAX_INT_VALUE)).contains(Port.newInstance(Port.MAX_INT_VALUE)));
	}

	@Test
	public void testContains06() {
		assertTrue(PortRange.newInstance(Port.newInstance(1080)).contains(Port.newInstance(1080)));
	}
	
	@Test
	public void testContains07() {
		assertTrue(PortRange.newInstance(Port.newInstance(1000), Port.newInstance(2000)).contains(Port.newInstance(1000)));		
	}
	
	@Test
	public void testContains08() {
		assertTrue(PortRange.newInstance(Port.newInstance(1000), Port.newInstance(2000)).contains(Port.newInstance(2000)));		
	}
	
	@Test
	public void testContains09() {
		assertTrue(PortRange.newInstance(Port.newInstance(1000), Port.newInstance(2000)).contains(Port.newInstance(1080)));		
	}

}
