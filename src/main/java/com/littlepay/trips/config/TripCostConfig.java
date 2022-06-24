package com.littlepay.trips.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.littlepay.trips.dto.TripCost;
import com.littlepay.trips.enums.Stop;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class TripCostConfig {

	@Value("classpath:tripCost.json")
	private Resource jsonConfig;

	private List<TripCost> costs;

	private Map<Stop, Map<Stop, Double>> costsMap;

	public List<TripCost> getCosts() {
		if (costs == null) {
			try {
				costs = Arrays.asList(new ObjectMapper().readValue(jsonConfig.getFile(), TripCost[].class));
			} catch (IOException e) {
				e.printStackTrace(); // TODO
			}
		}
		return costs;
	}

	public Map<Stop, Map<Stop, Double>> getCostsMap() {
		if (costsMap == null) {
			List<TripCost> costsList = getCosts();
			costsMap = new EnumMap<>(Stop.class);
			costsList.forEach(cost -> {
				costsMap.computeIfAbsent(cost.getFrom(), c -> new EnumMap<>(Stop.class))
						.put(cost.getTo(), cost.getCost());
				costsMap.computeIfAbsent(cost.getTo(), c -> new EnumMap<>(Stop.class))
						.put(cost.getFrom(), cost.getCost());
			});
		}
		return costsMap;
	}
}
