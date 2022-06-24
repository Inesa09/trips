package com.littlepay.trips.calculator;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public class IncompleteTripCalculator extends TripCalculator {

	@Override
	protected Double calculateCharge() {
		return tripCostConfig.getCostsMap().get(trip.getFromStop()).values()
				.stream()
				.max(Double::compareTo)
				.orElse(Double.NaN);
	}
}
