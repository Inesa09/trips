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
		trip.setStatus(calculateStatus());
		trip.setCharged(calculateCharge());
		return trip;
	}

	private Long calculateDuration() {
		return trip.getFinished() == null ? null :
				Duration.between(trip.getStarted(), trip.getFinished()).getSeconds();
	}

	private TripStatus calculateStatus() {
		if (trip.getFinished() == null) {
			return TripStatus.INCOMPLETE;
		}
		if (trip.getFromStop() == trip.getToStop()) {
			return TripStatus.CANCELLED;
		}
		return TripStatus.COMPLETED;
	}

	private Double calculateCharge() {
		return null;
	}
}
