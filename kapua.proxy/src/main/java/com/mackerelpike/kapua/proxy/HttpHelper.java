package com.mackerelpike.kapua.proxy;

import java.io.IOException;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpHelper
{
	private static Logger LOG = LoggerFactory.getLogger(HttpHelper.class);

	public static String SendHttpPost(String url, String jsonParam)
	{
		return SendHttpPost(url, jsonParam, false);
	}

	public static String SendHttpPost(String url, String jsonParam, boolean noNeedResponse)
	{
		String jsonResult = null;

		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build())
		{
			HttpPost method = new HttpPost(url);

			LOG.debug("Post Request To {}", url);

			if (null != jsonParam)
			{
				StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/json");
				method.setEntity(entity);
			}

			try (CloseableHttpResponse response = httpClient.execute(method);)
			{
				HttpEntity responseEntity = response.getEntity();
				LOG.info("The Returened StatusCode:{}", response.getStatusLine().getStatusCode());
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
				{
					if (noNeedResponse)
					{
						return null;
					}
					jsonResult = EntityUtils.toString(responseEntity);

					if (LOG.isDebugEnabled())
					{
						LOG.debug("The Returened Body\n:{}", jsonResult);
					}
				}
			}
		} catch (IOException e)
		{
			LOG.error("Post Request To:[{}] Failed!", url, e);
		}

		return jsonResult;
	}

	public static String SendHttpGet(String url, String jwtToken)
	{
		String jsonResult = null;
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build())
		{
			// 发送get请求
			HttpGet request = new HttpGet(url);

			LOG.debug("Post Request To {}", url);

			request.setHeader("Authorization", "Bearer " + jwtToken);
			request.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"));
			request.setHeader(new BasicHeader("Accept", "application/json;charset=utf-8"));

			try (CloseableHttpResponse response = httpClient.execute(request);)
			{
				HttpEntity responseEntity = response.getEntity();

				LOG.info("The Returened StatusCode:{}", response.getStatusLine().getStatusCode());

				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
				{

					jsonResult = EntityUtils.toString(responseEntity);

					if (LOG.isDebugEnabled())
					{
						LOG.debug("The Returened Body\n:{}", jsonResult);
					}
				}
			}
		} catch (IOException e)
		{
			LOG.error("Post Request To:[{}] Failed!", url, e);
		}
		return jsonResult;
	}
}
