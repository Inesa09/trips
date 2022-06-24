package com.littlepay.trips.enums;

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
}
