package com.stock;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpUtils {

	public static String sendGet(String url, String param) throws ClientProtocolException, IOException {
		String result = "";
		HttpClient httpClient = HttpClients.createDefault();
		String urlNameString = url;
		if(param!=null){
			urlNameString += "?" + param;
		}
		//System.out.println("urlNameString:" + urlNameString);
		HttpGet httpget = new HttpGet(urlNameString); 
		httpget.setHeader("Charset", "UTF-8");
		httpget.setHeader("referer", "https://finance.sina.com.cn/");
		
		HttpResponse response;
		response = httpClient.execute(httpget);
		
		String responserHeader = response.getHeaders("Content-Type")[0].getValue();
		if (responserHeader.contains("image"))
			result = null;
		else if (responserHeader.contains("application/octet-stream"))
			result = null;
		else{
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity); 
		}
		
		return result;
	}
	
}
