package jargyle.common.net.http;

import static org.junit.Assert.*;

import org.junit.Test;

public class RequestLineTest {

	@Test
	public void testNewInstanceMethodStringString01() {
		RequestLine requestLine1 = RequestLine.newInstance(
				Method.CONNECT, "www.example.com:80", "HTTP/1.1");
		RequestLine requestLine2 = RequestLine.newInstance(
				requestLine1.toByteArray());
		assertEquals(requestLine1, requestLine2);
	}

	@Test
	public void testNewInstanceMethodStringString02() {
		RequestLine requestLine1 = RequestLine.newInstance(
				Method.GET, "/where?q=now", "HTTP/1.1");
		RequestLine requestLine2 = RequestLine.newInstance(
				requestLine1.toByteArray());
		assertEquals(requestLine1, requestLine2);
	}

	@Test
	public void testNewInstanceMethodStringString03() {
		RequestLine requestLine1 = RequestLine.newInstance(
				Method.OPTIONS, "*", "HTTP/1.1");
		RequestLine requestLine2 = RequestLine.newInstance(
				requestLine1.toByteArray());
		assertEquals(requestLine1, requestLine2);
	}

	@Test
	public void testNewInstanceMethodStringString04() {
		RequestLine requestLine1 = RequestLine.newInstance(
				Method.POST, 
				"http://www.example.org/pub/WWW/TheProject.html", 
				"HTTP/1.1");
		RequestLine requestLine2 = RequestLine.newInstance(
				requestLine1.toByteArray());
		assertEquals(requestLine1, requestLine2);
	}

}
