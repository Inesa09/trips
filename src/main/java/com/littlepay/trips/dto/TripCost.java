package com.littlepay.trips.dto;

import com.littlepay.trips.enums.Stop;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripCost {

	private Stop from;
	private Stop to;
	private Double cost;
}
