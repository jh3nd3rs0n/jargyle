package jargyle.common.net.http;

import static org.junit.Assert.*;

import org.junit.Test;

public class MessageHeaderTest {

	@Test
	public void testNewRequestInstance() {
		MessageHeader<RequestLine> messageHeader1 = 
				MessageHeader.newRequestInstance(
						Method.GET, "/hello.txt", "HTTP/1.1", 
						HeaderField.newInstance(
								"User-Agent", "curl/7.16.3 libcurl/7.16.3 OpenSSL/0.9.7l zlib/1.2.3"),
						HeaderField.newInstance("Host", "www.example.com"),
						HeaderField.newInstance("Accept-Language", "en, mi"));
		MessageHeader<RequestLine> messageHeader2 = MessageHeader.newInstance(
				messageHeader1.toByteArray(), RequestLine.class);
		assertEquals(messageHeader1, messageHeader2);
	}

	@Test
	public void testNewStatusInstance() {
		MessageHeader<StatusLine> messageHeader1 = 
				MessageHeader.newStatusInstance(
						"HTTP/1.1", 200, "OK", 
						HeaderField.newInstance(
								"Date", "Mon, 27 Jul 2009 12:28:53 GMT"),
						HeaderField.newInstance("Server", "Apache"),
						HeaderField.newInstance(
								"Last-Modified", "Wed, 22 Jul 2009 19:15:56 GMT"),
						HeaderField.newInstance("ETag", "\"34aa387-d-1568eb00\""),
						HeaderField.newInstance("Accept-Ranges", "bytes"),
						HeaderField.newInstance("Content-Length", "51"),
						HeaderField.newInstance("Vary", "Accept-Encoding"),
						HeaderField.newInstance("Content-Type", "text/plain"));
		MessageHeader<StatusLine> messageHeader2 = MessageHeader.newInstance(
				messageHeader1.toByteArray(), StatusLine.class);
		assertEquals(messageHeader1, messageHeader2);
	}

}
