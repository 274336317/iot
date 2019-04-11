package com.mackerelpike.kapua.proxy;

import java.io.UnsupportedEncodingException;

public class Main
{
	public static void main(String[] args) throws UnsupportedEncodingException
	{
		String		userName	= "User123";

		String		password	= "User@12345678";

		KapuaProxy	kp			= new KapuaProxy();
		kp.setPassword(password);
		kp.setUserName(userName);

		UserInfo user = KapuaProxyUtils.Login(kp);

		if (user != null)
		{

			DeviceResult	dr	= KapuaProxyUtils.GetDevices(user);

			ChannelsResult	cr	= KapuaProxyUtils.GetChannels(user);

			ChannelInfo		ci	= cr.getItems()[1];

			DeviceMessages	dms	= KapuaProxyUtils.GetMessages(user, ci, 0, 100);
			System.out.println(dms);
		} else
		{
			System.out.println("Empty Result!");
		}

	}
}
