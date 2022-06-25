package com.littlepay.trips.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TripInfo {

	public static final String STARTED = "Started";
	public static final String FINISHED = "Finished";
	public static final String DURATION = "DurationSecs";
	public static final String FROM = "FromStopId";
	public static final String TO = "ToStopId";
	public static final String CHARGED = "ChargeAmount";
	public static final String COMPANY = "CompanyId";
	public static final String BUS = "BusID";
	public static final String PAN = "PAN";
	public static final String STATUS = "Status";
}
