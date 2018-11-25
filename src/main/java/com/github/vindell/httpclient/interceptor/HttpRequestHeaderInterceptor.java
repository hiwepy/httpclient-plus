 package com.github.vindell.httpclient.interceptor;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.net.HttpHeaders;


public class HttpRequestHeaderInterceptor implements HttpRequestInterceptor {

	private static final Logger LOG = LoggerFactory.getLogger(HttpRequestHeaderInterceptor.class);
	
	//初始化配置文件
	protected final HttpRequestHeaderProperties headerProperties;
	
	public HttpRequestHeaderInterceptor(HttpRequestHeaderProperties headerProperties){	
		this.headerProperties = headerProperties;
	}
	
	@Override
	public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
		this.setHeader(request, HttpHeaders.ACCEPT, headerProperties.getAccept());
		this.setHeader(request, HttpHeaders.ACCEPT_CHARSET, headerProperties.getAcceptCharset());
		this.setHeader(request, HttpHeaders.ACCEPT_ENCODING, headerProperties.getAcceptEncoding());
		this.setHeader(request, HttpHeaders.ACCEPT_LANGUAGE, headerProperties.getAcceptLanguage());
		this.setHeader(request, HttpHeaders.ACCEPT_RANGES, headerProperties.getAcceptRanges());
		this.setHeader(request, HttpHeaders.AUTHORIZATION, headerProperties.getAuthorization());
		this.setHeader(request, HttpHeaders.CONNECTION, headerProperties.getConnection());
		this.setHeader(request, HttpHeaders.HOST, headerProperties.getHost());
		this.setHeader(request, HttpHeaders.ORIGIN, headerProperties.getOrigin());
		this.setHeader(request, HttpHeaders.PROXY_AUTHENTICATE, headerProperties.getProxyAuthenticate());
		this.setHeader(request, HttpHeaders.PROXY_AUTHORIZATION, headerProperties.getProxyAuthorization());
		this.setHeader(request, HttpHeaders.REFERER, headerProperties.getReferer());
		this.setHeader(request, HttpHeaders.USER_AGENT, headerProperties.getUserAgent());
	}

	protected void setHeader(HttpRequest request, String key, String value) {
		if(StringUtils.isNoneBlank(value)) {
			boolean match = Arrays.stream(request.getAllHeaders()).anyMatch(item -> StringUtils.equalsIgnoreCase(item.getName(), key));
			if(!match) {
				if(LOG.isDebugEnabled()){
					LOG.debug("Set HTTP HEADER: {}:{}.", key, value);
				}
				request.setHeader(key, value);
			}
		}
	}
	
}

 
