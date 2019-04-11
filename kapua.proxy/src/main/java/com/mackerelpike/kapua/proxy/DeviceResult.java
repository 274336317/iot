package com.mackerelpike.kapua.proxy;

public class DeviceResult
{

	private String			type;

	private boolean			limitExceeded;

	private int				size;

	private DeviceInfo[]	items;

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public boolean isLimitExceeded()
	{
		return limitExceeded;
	}

	public void setLimitExceeded(boolean limitExceeded)
	{
		this.limitExceeded = limitExceeded;
	}

	public int getSize()
	{
		return size;
	}

	public void setSize(int size)
	{
		this.size = size;
	}

	public DeviceInfo[] getItems()
	{
		return items;
	}

	public void setItems(DeviceInfo[] items)
	{
		this.items = items;
	}
}
