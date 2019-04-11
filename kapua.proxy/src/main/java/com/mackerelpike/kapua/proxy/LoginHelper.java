package com.mackerelpike.kapua.proxy;

import com.google.gson.Gson;

public class LoginHelper
{
	public static UserInfo login(String loginUrl, String userName, String password)
	{
		UserInfo	user	= null;

		String		param	= "{ \"username\": \"" + userName + "\", \"password\": \"" + password + "\" }";

		String		result	= HttpHelper.httpPost(loginUrl, param);
		if (result != null)
		{
			Gson g = new Gson();
			user = g.fromJson(result, UserInfo.class);
		}

		return user;
	}

}
