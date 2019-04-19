package com.mackerelpike.iot.kura.simualtion.sensors;

import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SensorsPublisherOptions {
	private static final Logger LOGGER = LoggerFactory.getLogger(SensorsPublisherOptions.class);

	
	private Map<String, Object> properties;
	
	SensorsPublisherOptions(final Map<String, Object> properties) {
	        Objects.requireNonNull(properties);
	        this.properties = properties;
	    }

	public String getCloudPublisherPid() {

		LOGGER.info("ElevatorPublisherOptions Called!");
		return "org.eclipse.kura.cloud.CloudService";
	}

	String getCloudSubscriberPid() {

		return "";
	}

	public String getAppId() {
		String appId = "ELEVATOR_PUBLISHER";
		return appId;
	}
}
