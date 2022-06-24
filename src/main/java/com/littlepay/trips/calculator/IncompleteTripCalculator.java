package com.littlepay.trips.calculator;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

@Component
public class IncompleteTripCalculator extends TripCalculator {

	@Override
	protected Double calculateCharge() {
		return Optional.ofNullable(tripCostConfig.getCostsMap())
				.map(costsMap -> costsMap.get(trip.getFromStop()))
				.map(Map::values)
				.map(Collection::stream)
				.orElse(Stream.empty())
				.max(Double::compareTo)
				.orElse(0.0);
	}
}
