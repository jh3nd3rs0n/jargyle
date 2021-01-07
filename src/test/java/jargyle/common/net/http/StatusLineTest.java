package jargyle.common.net.http;

import static org.junit.Assert.*;

import org.junit.Test;

public class StatusLineTest {

	@Test
	public void testNewInstanceStringIntString01() {
		StatusLine statusLine1 = StatusLine.newInstance(
				"HTTP/1.1", 200, "OK");
		StatusLine statusLine2 = StatusLine.newInstance(
				statusLine1.toByteArray());
		assertEquals(statusLine1, statusLine2);
	}

	@Test
	public void testNewInstanceStringIntString02() {
		StatusLine statusLine1 = StatusLine.newInstance(
				"HTTP/1.1", 404, "Not Found");
		StatusLine statusLine2 = StatusLine.newInstance(
				statusLine1.toByteArray());
		assertEquals(statusLine1, statusLine2);
	}

	@Test
	public void testNewInstanceStringIntString03() {
		StatusLine statusLine1 = StatusLine.newInstance(
				"HTTP/1.1", 500, "Internal Server Error");
		StatusLine statusLine2 = StatusLine.newInstance(
				statusLine1.toByteArray());
		assertEquals(statusLine1, statusLine2);
	}

	@Test
	public void testNewInstanceStringIntString04() {
		StatusLine statusLine1 = StatusLine.newInstance(
				"HTTP/1.1", 999, null);
		StatusLine statusLine2 = StatusLine.newInstance(
				statusLine1.toByteArray());
		assertEquals(statusLine1, statusLine2);
	}

}
