package com.mackerelpike.kapua.proxy;

//{"type":"storableListResult","limitExceeded":false,"size":1,"items":[{"type":"jsonDatastoreMessage","capturedOn":"2019-04-09T07:50:39.982Z","channel":{"semanticParts":["ELEVATOR_PUBLISHER","data","metrics"]},
//"clientId":"test1","deviceId":"RUZnmnxQUZ0","payload":{"metrics":[{"valueType":"string","value":"1554796239982","name":"rate"}]},"receivedOn":"2019-04-09T07:50:40.647Z","scopeId":"apcnMD_Bayg","sentOn":"2019-04-09T07:50:39.982Z","datastoreId":"8a2ce504-cacd-473f-a932-73298f72025b","timestamp":"2019-04-09T07:50:39.982Z"}]}

public class DeviceMessages
{
	private String	type;

	private boolean	limitExceeded;

	private int		size;

	private Item[]	items;

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

	public Item[] getItems()
	{
		return items;
	}

	public void setItems(Item[] items)
	{
		this.items = items;
	}

	static class Item
	{
		private String	type;

		private String	capturedOn;

		private Channel	channel;

		private String	clientId;

		private String	deviceId;

		private Payload	payload;

		public String getType()
		{
			return type;
		}

		public void setType(String type)
		{
			this.type = type;
		}

		public String getCapturedOn()
		{
			return capturedOn;
		}

		public void setCapturedOn(String capturedOn)
		{
			this.capturedOn = capturedOn;
		}

		public Channel getChannel()
		{
			return channel;
		}

		public void setChannel(Channel channel)
		{
			this.channel = channel;
		}

		public String getClientId()
		{
			return clientId;
		}

		public void setClientId(String clientId)
		{
			this.clientId = clientId;
		}

		public String getDeviceId()
		{
			return deviceId;
		}

		public void setDeviceId(String deviceId)
		{
			this.deviceId = deviceId;
		}

		public Payload getPayload()
		{
			return payload;
		}

		public void setPayload(Payload payload)
		{
			this.payload = payload;
		}

	}

	static class Channel
	{
		private String[] semanticParts;

		public String[] getSemanticParts()
		{
			return semanticParts;
		}

		public void setSemanticParts(String[] semanticParts)
		{
			this.semanticParts = semanticParts;
		}

	}

	static class Payload
	{
		private Metric[]	metrics;

		private String		receivedOn;

		private String		scopeId;

		private String		sentOn;

		private String		datastoreId;

		private String		timestamp;

		public Metric[] getMetrics()
		{
			return metrics;
		}

		public void setMetrics(Metric[] metrics)
		{
			this.metrics = metrics;
		}

		public String getReceivedOn()
		{
			return receivedOn;
		}

		public void setReceivedOn(String receivedOn)
		{
			this.receivedOn = receivedOn;
		}

		public String getScopeId()
		{
			return scopeId;
		}

		public void setScopeId(String scopeId)
		{
			this.scopeId = scopeId;
		}

		public String getSentOn()
		{
			return sentOn;
		}

		public void setSentOn(String sentOn)
		{
			this.sentOn = sentOn;
		}

		public String getDatastoreId()
		{
			return datastoreId;
		}

		public void setDatastoreId(String datastoreId)
		{
			this.datastoreId = datastoreId;
		}

		public String getTimestamp()
		{
			return timestamp;
		}

		public void setTimestamp(String timestamp)
		{
			this.timestamp = timestamp;
		}

	}

	static class Metric
	{
		private String	valueType;

		private String	value;

		private String	name;

		public String getValueType()
		{
			return valueType;
		}

		public void setValueType(String valueType)
		{
			this.valueType = valueType;
		}

		public String getValue()
		{
			return value;
		}

		public void setValue(String value)
		{
			this.value = value;
		}

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}

	}

}