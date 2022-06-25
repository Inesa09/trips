package com.littlepay.trips.service;

import java.io.InputStream;
import java.io.Writer;
import java.util.List;

import com.littlepay.trips.dto.Tap;
import com.littlepay.trips.dto.Trip;

public interface CSVService {

	List<Tap> parseTaps(InputStream is);

	void writeTrips(Writer writer, List<Trip> trips);
}
