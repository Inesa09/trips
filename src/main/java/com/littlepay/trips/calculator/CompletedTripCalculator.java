package com.littlepay.trips.calculator;

import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class CompletedTripCalculator extends TripCalculator {

	@Override
	protected Double calculateCharge() {
		return Optional.ofNullable(tripCostConfig.getCostsMap())
				.map(costsMap -> costsMap.get(trip.getFromStop()))
				.map(costsMapForStop -> costsMapForStop.get(trip.getToStop()))
				.orElse(0.0);
	}
}
