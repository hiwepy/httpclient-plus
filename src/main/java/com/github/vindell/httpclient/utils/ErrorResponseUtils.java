 package com.github.vindell.httpclient.utils;

import org.apache.http.StatusLine;

public class ErrorResponseUtils {

	public static String getStatusErrorJSON(StatusLine statusLine, String errmsg){
		int statusCode = statusLine.getStatusCode();
		StringBuilder builder = new StringBuilder();
		//{"errcode":40013,"errmsg":"invalid appid"}
		builder.append("{");
		builder.append("\"errcode\":").append(statusCode).append(",");
		builder.append("\"reason\":\"").append(statusLine.getReasonPhrase()).append("\",");
		builder.append("\"errmsg\":\"").append(errmsg).append("\"");
		builder.append("}");
		return builder.toString();
	}
	
	
}

 
