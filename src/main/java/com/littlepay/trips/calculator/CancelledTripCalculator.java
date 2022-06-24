package com.littlepay.trips.calculator;

import org.springframework.stereotype.Component;

@Component
public class CancelledTripCalculator extends TripCalculator {

	@Override
	protected Double calculateCharge() {
		return 0.0;
	}
}
