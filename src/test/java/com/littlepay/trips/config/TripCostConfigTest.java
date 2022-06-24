package com.littlepay.trips.config;

import com.littlepay.trips.enums.Stop;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TripCostConfigTest {

	@Autowired
	private TripCostConfig tripCostConfig;

	@Test
	void testConfigParsing() {
		assertThat(tripCostConfig.getCostsMap()).isNotEmpty();
		assertThat(tripCostConfig.getCostsMap().get(Stop.ONE)).isNotEmpty();
		assertThat(tripCostConfig.getCostsMap().get(Stop.TWO)).isNotEmpty();
		assertThat(tripCostConfig.getCostsMap().get(Stop.THREE)).isNotEmpty();
	}
}
