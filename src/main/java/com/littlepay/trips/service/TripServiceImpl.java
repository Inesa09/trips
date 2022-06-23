package com.littlepay.trips.service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

import com.littlepay.trips.dto.Tap;
import com.littlepay.trips.dto.Trip;
import com.littlepay.trips.util.TripCalculator;

public class TripServiceImpl implements TripService {
	
	/**
	 * 	sort by dates
	 * 	for each TAP:
	 * 		if ON - put Trip in stack
	 * 		if OFF - remove Trip from stack
	 *
	 * 		if OFF - check BusStop, if is not changed -> cancelled
	 *
	 * 	in the end there are only Incomplete trips in Stack
	 */
	@Override
	public List<Trip> findTripsByTaps(List<Tap> taps) {
		List<Tap> tapsByDate = taps.stream().sorted(Comparator.comparing(Tap::getDatetime))
				.collect(Collectors.toList());
		
		Deque<Tap> tapsStack = new ArrayDeque<>();
		List<Trip> trips = new ArrayList<>();

		tapsByDate.forEach(tap -> {
			switch(tap.getTapType()) {
				case ON:
					tapsStack.push(tap);
					break;
				case OFF:
					Tap tapOn = tapsStack.pop();
					trips.add(createTrip(tapOn, tap));
					break;
				default:
					break;
			}
		});

		tapsStack.stream()
				.map(this::createTrip)
				.forEach(trips::add);

		return trips.stream()
				.map(TripCalculator::new)
				.map(TripCalculator::calculateOutcome)
				.collect(Collectors.toList());
	}

	private Trip createTrip(Tap tapOn, Tap tapOff) {
		return Trip.builder()
				.started(tapOn.getDatetime())
				.finished(tapOff.getDatetime())
				.fromStop(tapOn.getStop())
				.toStop(tapOff.getStop())
				.companyId(tapOn.getCompanyId())
				.busId(tapOn.getBusId())
				.pan(tapOn.getPan())
				.build();
	}

	private Trip createTrip(Tap tapOn) {
		return createTrip(tapOn, new Tap());
	}
}
