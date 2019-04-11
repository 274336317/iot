package com.mackerelpike.kapua.proxy;

public class ConfigHelper
{
	public static String GetLoginUrl()
	{
		return "http://192.168.0.111:8081/v1/authentication/user";
	}
	
	public static String GetDeviceUlr(UserInfo user)
	{
		return "http://192.168.0.111:8081/v1/" + user.getScopeId() + "/devices?offset=0&limit=1000000";
	}
	
	public static String GetChannelUrl(UserInfo user)
	{
		return "http://192.168.0.111:8081/v1/" + user.getScopeId() + "/data/channels";
	}
	
	public static String GetMessagesUrl(UserInfo user)
	{
		return "http://192.168.0.111:8081/v1/" + user.getScopeId() + "/data/messages?";
	}
}
