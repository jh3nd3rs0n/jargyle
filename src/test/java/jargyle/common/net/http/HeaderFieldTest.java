package jargyle.common.net.http;

import static org.junit.Assert.*;

import org.junit.Test;

public class HeaderFieldTest {

	@Test
	public void testNewInstanceStringString01() {
		HeaderField headerField1 = HeaderField.newInstance(
				"Host", "www.example.org");
		HeaderField headerField2 = HeaderField.newInstance(
				headerField1.toByteArray());
		assertEquals(headerField1, headerField2);
	}

	@Test
	public void testNewInstanceStringString02() {
		HeaderField headerField1 = HeaderField.newInstance(
				"Upgrade", "HTTP/2.0, SHTTP/1.3, IRC/6.9, RTA/x11");
		HeaderField headerField2 = HeaderField.newInstance(
				headerField1.toByteArray());
		assertEquals(headerField1, headerField2);		
	}

	@Test
	public void testNewInstanceStringString03() {
		HeaderField headerField1 = HeaderField.newInstance(
				"Via", " 1.0 ricky, 1.1 ethel, 1.1 fred, 1.0 lucy");
		HeaderField headerField2 = HeaderField.newInstance(
				headerField1.toByteArray());
		assertEquals(headerField1, headerField2);		
	}

	@Test
	public void testNewInstanceStringString04() {
		HeaderField headerField1 = HeaderField.newInstance(
				"User-Agent", " curl/7.16.3 libcurl/7.16.3 OpenSSL/0.9.7l zlib/1.2.3");
		HeaderField headerField2 = HeaderField.newInstance(
				headerField1.toByteArray());
		assertEquals(headerField1, headerField2);		
	}

	@Test
	public void testNewInstanceStringString05() {
		HeaderField headerField1 = HeaderField.newInstance(
				"Date", "Mon, 27 Jul 2009 12:28:53 GMT");
		HeaderField headerField2 = HeaderField.newInstance(
				headerField1.toByteArray());
		assertEquals(headerField1, headerField2);		
	}

}
