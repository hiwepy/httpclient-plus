package com.github.hiwepy.httpclient.interceptor;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

/**
 * @author 		： <a href="https://github.com/hiwepy">hiwepy</a>
 */
public class HttpRequestSummaryInterceptor implements HttpRequestInterceptor {
	
	public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
		// AtomicInteger是个线程安全的整型类
		//AtomicInteger count = (AtomicInteger) context.getAttribute("count");
		//request.addHeader("Count", Integer.toString(count.getAndIncrement()));
	}
	
}
