package com.mackerelpike.iot.kura.simualtion.sensors;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class SensorsMsgRecvClient 
{
	private static final Logger LOGGER = LoggerFactory.getLogger(SensorsMsgRecvClient.class);

	public static final String HOST = "tcp://192.168.0.109:1883";
	// 定义一个主题
	public static final String TOPIC = "sensors/temperature_humidity";
	// 定义MQTT的ID，可以在MQTT服务配置中指定
	private static final String clientid = "kura_sensors_client";

	private MqttClient client;
	private String userName = "mqtt";
	private String passWord = "mqtt"; 


	private MqttConnectOptions options;
	
	private SensorsPublisher publisher;
	
	public SensorsMsgRecvClient(SensorsPublisher publisher) 
	{
		this.publisher = publisher;
	}

	public boolean init() {
		
		LOGGER.info("Called init() Method!");
		
		boolean result = true;
		
		try 
		{
			client = new MqttClient(HOST, clientid, new MemoryPersistence());

			options = new MqttConnectOptions();

			options.setCleanSession(false);

			options.setUserName(userName);

			options.setPassword(passWord.toCharArray());

			options.setConnectionTimeout(10);

			options.setKeepAliveInterval(20);

			client.setCallback(new MqttCallback() {
				public void connectionLost(Throwable cause) {
					LOGGER.error("Connection Lost!", cause);
				}

				public void deliveryComplete(IMqttDeliveryToken token) {
					LOGGER.info("deliveryComplete---------" + token.isComplete());
				}

				public void messageArrived(String topic, MqttMessage message) throws Exception {
					LOGGER.info("RECV MSG:{}", message.toString());
					
					SensorsMsg msg = null;
					
					try
					{
						Gson gson = new Gson();
						
						msg = gson.fromJson(message.toString(), SensorsMsg.class);
						
						publisher.doPublish(msg);
					}
					catch(Exception ex)
					{
						LOGGER.error("", ex);
					}
				}
			});
			
			client.connect(options);
			client.subscribe(TOPIC);

		} catch (Exception ex) 
		{
			LOGGER.error("Exception Happened!", ex);
			
			result = false;
		}

		return result;
	}

	public void destory() {

		if(client != null)
		{
			try 
			{
				client.close();
			} catch (MqttException ex) 
			{
				LOGGER.error("Exception Happened!", ex);
			}
			client = null;
		}
	}
}