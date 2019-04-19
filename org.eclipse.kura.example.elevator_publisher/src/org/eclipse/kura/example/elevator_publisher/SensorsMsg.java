package org.eclipse.kura.example.elevator_publisher;

public class SensorsMsg {
	
	private float Temperature;
	
	private int Humidity;
	
	private String Timestamp;

	public float getTemperature() {
		return Temperature;
	}

	public void setTemperature(float temperature) {
		Temperature = temperature;
	}

	public int getHumidity() {
		return Humidity;
	}

	public void setHumidity(int humidity) {
		Humidity = humidity;
	}

	public String getTimestamp() {
		return Timestamp;
	}

	public void setTimestamp(String timestamp) {
		Timestamp = timestamp;
	}
}