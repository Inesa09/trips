package com.littlepay.trips.service;

import java.util.List;

import com.littlepay.trips.dto.Tap;
import com.littlepay.trips.dto.Trip;

public interface TripService {

	List<Trip> findTripsByTaps(List<Tap> taps);
}
