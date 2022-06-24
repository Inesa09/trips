package com.littlepay.trips.calculator;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public class CancelledTripCalculator extends TripCalculator {

	@Override
	protected Double calculateCharge() {
		return Double.NaN;
	}
}
