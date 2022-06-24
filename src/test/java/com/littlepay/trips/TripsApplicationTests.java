package com.littlepay.trips;

import com.littlepay.trips.config.TripCostConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TripsApplicationTests {

	@Autowired
	private TripCostConfig jsonProperties;

	@Test
	public void whenPropertiesLoadedViaJsonPropertySource_thenLoadFlatValues() {
		System.out.println(jsonProperties.getCostsMap());
	}
}
