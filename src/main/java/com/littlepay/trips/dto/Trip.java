package com.littlepay.trips.dto;

import java.time.LocalDateTime;

import com.littlepay.trips.enums.Stop;
import com.littlepay.trips.enums.TripStatus;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class Trip {

	private LocalDateTime started;
	private LocalDateTime finished;
	private Long duration;
	private Stop fromStop;
	private Stop toStop;
	private Double charged;
	private String companyId;
	private String busId;
	private String pan;
	private TripStatus status;
}
