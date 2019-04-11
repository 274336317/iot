package com.mackerelpike.kapua.proxy;

import com.google.gson.Gson;

public class KapuaProxyUtils
{

	public static String LOGIN_URL = "http://192.168.0.111:8081/v1/authentication/user";

	public static UserInfo Login(KapuaProxy proxy)
	{
		UserInfo	user	= null;

		String		param	= "{ \"username\": \"" + proxy.getUserName() + "\", \"password\": \"" + proxy.getPassword()
				+ "\" }";

		String		result	= HttpHelper.httpPost(LOGIN_URL, param);
		if (result != null)
		{
			Gson g = new Gson();
			user = g.fromJson(result, UserInfo.class);
		}

		return user;
	}

}