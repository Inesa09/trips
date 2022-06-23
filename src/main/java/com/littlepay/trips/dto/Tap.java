package com.littlepay.trips.dto;

import java.time.LocalDateTime;

import com.littlepay.trips.enums.Stop;
import com.littlepay.trips.enums.TapType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class Tap {

	private Integer id;
	private LocalDateTime datetime;
	private TapType tapType;
	private Stop stop;
	private String companyId;
	private String busId;
	private String pan;
}
