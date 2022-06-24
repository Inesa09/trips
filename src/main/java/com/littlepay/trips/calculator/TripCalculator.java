package com.littlepay.trips.calculator;

import com.littlepay.trips.config.TripCostConfig;
import com.littlepay.trips.dto.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class TripCalculator {

	@Autowired
	protected TripCostConfig tripCostConfig;

	protected Trip trip;

	protected abstract Double calculateCharge();

	public Trip getChargedTrip() {
		trip.setCharged(calculateCharge());
		return trip;
	}

	public void setTrip(Trip trip) {
		this.trip = trip;
	}
}
