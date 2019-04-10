package com.mackerelpike.kapua.proxy;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLDecoder;

public class HttpHelper 
{
	private static Logger logger = LoggerFactory.getLogger(HttpHelper.class); 
	
	public static String httpPost(String url, String jsonParam) 
	{
		return httpPost(url, jsonParam, false);
	}

	public static String httpPost(String url, String jsonParam, boolean noNeedResponse) {
		// post请求返回结果
		DefaultHttpClient httpClient = new DefaultHttpClient();
		String jsonResult = null;
		HttpPost method = new HttpPost(url);
		try 
		{
			if (null != jsonParam) {
				StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/json");
				method.setEntity(entity);
			}
			HttpResponse result = httpClient.execute(method);
			url = URLDecoder.decode(url, "UTF-8");
			/** 请求发送成功，并得到响应 **/
			if (result.getStatusLine().getStatusCode() == 200) 
			{
				try 
				{

					if (noNeedResponse) 
					{
						return null;
					}
					/** 读取服务器返回过来的json字符串数据 **/
					jsonResult = EntityUtils.toString(result.getEntity());
				} catch (Exception e) 
				{
					logger.error("post请求提交失败:" + url, e);
				}
			}
			
		} catch (IOException e) {
			logger.error("post请求提交失败:" + url, e);
		}

		return jsonResult;
	}

	/**
	 * 发送get请求
	 * 
	 * @param url 路径
	 * @return
	 */
	public static String httpGet(String url, String jwtToken) {
		// get请求返回结果
		String jsonResult = null;
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			// 发送get请求
			HttpGet request = new HttpGet(url);
			
			request.setHeader("Authorization", "Bearer " + jwtToken );
			//request.setHeader("Content-type", "application/json");
			request.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"));
			request.setHeader(new BasicHeader("Accept", "application/json;charset=utf-8"));
			
			HttpResponse response = client.execute(request);
			

			/** 请求发送成功，并得到响应 **/
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				jsonResult = EntityUtils.toString(response.getEntity());

				url = URLDecoder.decode(url, "UTF-8");
			} else {
				
				System.out.println(response.toString());
				
				logger.error("get请求提交失败:" + url);
			}
		} catch (IOException e) {
			logger.error("get请求提交失败:" + url, e);
		}
		return jsonResult;
	}
}
