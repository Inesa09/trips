package com.littlepay.trips.calculator;

import com.littlepay.trips.dto.Trip;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TripCalculatorFactory {

	public static TripCalculator getCalculator(Trip trip) {
		switch (trip.getStatus()) {
			case COMPLETED:
				return CompletedTripCalculator.builder().trip(trip).build();
			case INCOMPLETE:
				return IncompleteTripCalculator.builder().trip(trip).build();
			case CANCELLED:
				return CancelledTripCalculator.builder().trip(trip).build();
			default:
				throw new IllegalStateException();
		}
	}
}
