package com.littlepay.trips.util;

import java.time.Duration;

import com.littlepay.trips.dto.Trip;
import com.littlepay.trips.enums.TripStatus;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TripCalculator {

	private Trip trip;

	public Trip calculateOutcome() {
		trip.setDuration(calculateDuration());
		trip.setCharged(calculateCharge());
		trip.setStatus(calculateStatus());
		return trip;
	}

	private Long calculateDuration() {
		return trip.getFinished() == null ? null :
				Duration.between(trip.getStarted(), trip.getFinished()).getSeconds();
	}

	private Double calculateCharge() {
		return null;
	}

	private TripStatus calculateStatus() {
		return null;
	}
}
