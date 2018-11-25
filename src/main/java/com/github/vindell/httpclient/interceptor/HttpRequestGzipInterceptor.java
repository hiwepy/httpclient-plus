 package com.github.vindell.httpclient.interceptor;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

import com.google.common.net.HttpHeaders;

/**
 * 增加gzip压缩请求
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 */
public class HttpRequestGzipInterceptor implements HttpRequestInterceptor {
	
	@Override
	public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
		if (!request.containsHeader(HttpHeaders.CONTENT_ENCODING)) {  
			//设置相关的压缩文件标识，在请求头的信息中
			request.addHeader(HttpHeaders.CONTENT_ENCODING, "gzip");  
        }
	}

}

 
