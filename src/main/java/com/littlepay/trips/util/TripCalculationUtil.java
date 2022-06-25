package com.littlepay.trips.util;

import java.time.Duration;

import com.littlepay.trips.dto.Trip;
import com.littlepay.trips.enums.TripStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TripCalculationUtil {

	public static Long calculateDuration(Trip trip) {
		return trip.getFinished() == null ? null :
				Duration.between(trip.getStarted(), trip.getFinished()).getSeconds();
	}

	public static TripStatus calculateStatus(Trip trip) {
		if (trip.getFinished() == null) {
			return TripStatus.INCOMPLETE;
		}
		if (trip.getFromStop() == trip.getToStop()) {
			return TripStatus.CANCELLED;
		}
		return TripStatus.COMPLETED;
	}
}
