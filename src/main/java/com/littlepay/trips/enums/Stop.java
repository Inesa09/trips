package com.littlepay.trips.enums;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Stop {
	ONE("Stop1"),
	TWO("Stop2"),
	THREE("Stop3");

	@JsonValue
	String stopId;

	Stop(String stopId) {
		this.stopId = stopId;
	}

	public static Stop fromStopId(String stopId) {
		return Arrays.stream(Stop.values())
				.filter(stop -> stop.stopId.equals(stopId))
				.findFirst()
				.orElse(null);
	}
}
