package com.littlepay.trips.calculator;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public class CompletedTripCalculator extends TripCalculator {

	@Override
	protected Double calculateCharge() {
		return tripCostConfig.getCostsMap().get(trip.getFromStop()).get(trip.getToStop());
	}
}
