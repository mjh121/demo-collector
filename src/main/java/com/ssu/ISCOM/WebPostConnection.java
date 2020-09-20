package com.ssu.ISCOM;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;

public class WebPostConnection {
	private HttpClient httpClient;
	private HttpPost httpPost;
	private ArrayList<NameValuePair> params;
	
	private String url = "";
	
	public WebPostConnection(String url) {
		this.url = url;
		init();
	}
	
	private void init() {
		httpClient = HttpClients.createDefault();
		httpPost = new HttpPost(url);
		params = new ArrayList<NameValuePair>();
	}
	
	public void setURL(String url) {
		try {
			httpPost.setURI(new URI(url));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addParam(String key, String value) {
		params.add(new BasicNameValuePair(key, value)); 
	}
	
	public String requestToURL() {
		String connectionInfo = Constants.WEB_NONE;

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			HttpResponse response = httpClient.execute(httpPost); 
			HttpEntity respEntity = response.getEntity();
			
			if(respEntity != null && !respEntity.equals(Constants.WEB_NONE)) {
				connectionInfo = EntityUtils.toString(respEntity); 
			} 
			params.clear();
			
			System.out.println("[WebConnection] ssucces request to URL");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			connectionInfo = Constants.WEB_NONE;
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			connectionInfo = Constants.WEB_NONE;
			e.printStackTrace();
		} catch (IOException e) {
			connectionInfo = Constants.WEB_NONE;
			e.printStackTrace();
		} 
		
		init();
		return connectionInfo;
	}
}
