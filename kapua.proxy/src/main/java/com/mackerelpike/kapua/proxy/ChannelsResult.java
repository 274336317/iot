package com.mackerelpike.kapua.proxy;

public class ChannelsResult 
{

	private String type;
	
	private boolean limitExceeded;
	
	private int size;
	
	private ChannelInfo [] items;
	
	private int totalCount;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isLimitExceeded() {
		return limitExceeded;
	}

	public void setLimitExceeded(boolean limitExceeded) {
		this.limitExceeded = limitExceeded;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public ChannelInfo[] getItems() {
		return items;
	}

	public void setItems(ChannelInfo[] items) {
		this.items = items;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	
}
