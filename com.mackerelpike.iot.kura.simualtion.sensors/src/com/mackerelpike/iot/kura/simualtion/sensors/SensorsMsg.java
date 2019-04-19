package com.mackerelpike.iot.kura.simualtion.sensors;

public class SensorsMsg {
	
	private String Temperature;
	
	private String Humidity;
	
	private String Timestamp;

	public String getTemperature() {
		return Temperature;
	}

	public void setTemperature(String temperature) {
		Temperature = temperature;
	}

	public String getHumidity() {
		return Humidity;
	}

	public void setHumidity(String humidity) {
		Humidity = humidity;
	}

	public String getTimestamp() {
		return Timestamp;
	}

	public void setTimestamp(String timestamp) {
		Timestamp = timestamp;
	}
}