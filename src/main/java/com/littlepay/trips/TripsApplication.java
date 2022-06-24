package com.littlepay.trips;

import com.littlepay.trips.config.TripCostConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = { TripCostConfig.class})
public class TripsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TripsApplication.class, args);
	}

}
