package com.littlepay.trips.util;

import java.io.InputStream;
import java.io.Writer;
import java.util.List;

import com.littlepay.trips.dto.Tap;
import com.littlepay.trips.dto.Trip;
import com.littlepay.trips.service.CSVService;
import com.littlepay.trips.service.TripService;
import org.springframework.stereotype.Component;

@Component
public class FilesHandler {

	final TripService tripService;
	final CSVService csvService;

	public FilesHandler(TripService tripService, CSVService csvService) {
		this.tripService = tripService;
		this.csvService = csvService;
	}

	public void convertTapsIntoTrips(InputStream inputStream, Writer outputWriter) {
		List<Tap> taps = csvService.parseTaps(inputStream);
		List<Trip> trips = tripService.findTripsByTaps(taps);
		csvService.writeTrips(outputWriter, trips);
	}
}
