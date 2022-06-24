package com.littlepay.trips.calculator;

import com.littlepay.trips.config.TripCostConfig;
import com.littlepay.trips.dto.Trip;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.factory.annotation.Autowired;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class TripCalculator {

	@Autowired
	protected TripCostConfig tripCostConfig;

	protected Trip trip;

	protected abstract Double calculateCharge();

	public Trip getChargedTrip() {
		trip.setCharged(calculateCharge());
		return trip;
	}
}
