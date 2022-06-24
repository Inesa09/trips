package com.littlepay.trips.calculator;

import com.littlepay.trips.dto.Trip;
import com.littlepay.trips.enums.TripStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TripCalculatorFactory {

	@Autowired
	CancelledTripCalculator cancelledTripCalculator;

	@Autowired
	CompletedTripCalculator completedTripCalculator;

	@Autowired
	IncompleteTripCalculator incompleteTripCalculator;

	public TripCalculator getCalculator(Trip trip) {
		TripCalculator tripCalculator = getCalculatorByStatus(trip.getStatus());
		tripCalculator.setTrip(trip);
		return tripCalculator;
	}

	private TripCalculator getCalculatorByStatus(TripStatus tripStatus) {
		switch (tripStatus) {
			case COMPLETED:
				return completedTripCalculator;
			case INCOMPLETE:
				return incompleteTripCalculator;
			case CANCELLED:
				return cancelledTripCalculator;
			default:
				throw new IllegalStateException("There is an incorrect trip status!");
		}
	}
}
