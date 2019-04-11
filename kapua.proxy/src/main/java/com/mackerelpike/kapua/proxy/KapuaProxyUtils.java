package com.mackerelpike.kapua.proxy;

import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class KapuaProxyUtils
{
	private static Logger LOG = LoggerFactory.getLogger(KapuaProxyUtils.class);
	
	public static UserInfo Login(KapuaProxy proxy)
	{
		UserInfo	user	= null;

		String		param	= "{ \"username\": \"" + proxy.getUserName() + "\", \"password\": \"" + proxy.getPassword()
				+ "\" }";

		String		result	= HttpHelper.SendHttpPost(ConfigHelper.GetLoginUrl(), param);
		if (result != null)
		{
			Gson g = new Gson();
			user = g.fromJson(result, UserInfo.class);
		}

		return user;
	}

	/**
	 * 获取指定用户下的所有设备
	 * 
	 * @param user
	 * @return
	 */
	public static DeviceResult GetDevices(UserInfo user)
	{
		DeviceResult	dr		= null;
		String			result	= HttpHelper.SendHttpGet(ConfigHelper.GetDeviceUlr(user), user.getTokenId());
		Gson			g		= new Gson();
		if (result != null)
		{
			dr = g.fromJson(result, DeviceResult.class);
		}

		return dr;
	}

	/**
	 * 获取指定用户下的所有设备通信通道
	 * 
	 * @param user
	 * @return
	 */
	public static ChannelsResult GetChannels(UserInfo user)
	{
		ChannelsResult	cr		= null;
		String			result	= HttpHelper.SendHttpGet(ConfigHelper.GetChannelUrl(user), user.getTokenId());

		if (result != null)
		{
			Gson g = new Gson();
			cr = g.fromJson(result, ChannelsResult.class);
		}

		return cr;
	}

	/**
	 * 获取指定通道下的消息
	 * @param user
	 * @param ci
	 * @param offset
	 * @param limit
	 * @return
	 */
	public static DeviceMessages GetMessages(UserInfo user, ChannelInfo ci, int offset, int limit)
	{
		DeviceMessages msgs = null;
		try
		{
			String url = ConfigHelper.GetMessagesUrl(user);
			url += "scopeId=" + URLEncoder.encode(user.getScopeId(), "UTF-8");
			url += "&clientId=" + URLEncoder.encode(ci.getClientId(), "UTF-8");
			url += "&channel=" + URLEncoder.encode(ci.getName(), "UTF-8");
			url += "&strictChannel=true";
			url += "&offset=" + offset;
			url += "&limit=" + limit;

			String result = HttpHelper.SendHttpGet(url, user.getTokenId());
			if(result != null)
			{
				Gson g = new Gson();
				msgs = g.fromJson(result, DeviceMessages.class);
			}
		} catch (Exception ex)
		{
			LOG.error("", ex);
		}

		return msgs;
	}
	
	/**
	 * 获取指定通道下的消息
	 * @param user
	 * @param ci
	 * @param offset
	 * @param limit
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static DeviceMessages GetMessages(UserInfo user, ChannelInfo ci, int offset, int limit, String startDate, String endDate)
	{
		DeviceMessages msgs = null;
		try
		{
			String url = ConfigHelper.GetMessagesUrl(user);
			url += "scopeId=" + URLEncoder.encode(user.getScopeId(), "UTF-8");
			url += "&clientId=" + URLEncoder.encode(ci.getClientId(), "UTF-8");
			url += "&channel=" + URLEncoder.encode(ci.getName(), "UTF-8");
			url += "&strictChannel=true";
			url += "&startDate=" + URLEncoder.encode(startDate,"UTF-8");
			url += "&endDate=" + URLEncoder.encode(endDate,"UTF-8");
			url += "&offset=" + offset;
			url += "&limit=1" + limit;

			String result = HttpHelper.SendHttpGet(url, user.getTokenId());
			if(result != null)
			{
				Gson g = new Gson();
				msgs = g.fromJson(result, DeviceMessages.class);
			}
		} catch (Exception ex)
		{
			LOG.error("", ex);
		}

		return msgs;
	}

}