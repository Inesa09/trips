package com.littlepay.trips.service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

import com.littlepay.trips.calculator.TripCalculatorFactory;
import com.littlepay.trips.dto.Tap;
import com.littlepay.trips.dto.Trip;
import com.littlepay.trips.calculator.TripCalculator;
import com.littlepay.trips.util.TripUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TripServiceImpl implements TripService {

	@Autowired
	TripCalculatorFactory tripCalculatorFactory;

	@Override
	public List<Trip> findTripsByTaps(List<Tap> taps) {
		// sort by dates
		List<Tap> tapsByDate = taps.stream()
				.sorted(Comparator.comparing(Tap::getDatetime))
				.collect(Collectors.toList());

		Deque<Tap> tapsStack = new ArrayDeque<>();
		List<Trip> trips = new ArrayList<>();

		/*
		 * if ON - put Tap in stack
		 * if OFF - remove Tap from stack and create Trip
		 */
		tapsByDate.forEach(tap -> {
			switch (tap.getTapType()) {
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

		// in the end there are only Incomplete trips in Stack
		tapsStack.stream()
				.map(this::createTrip)
				.forEach(trips::add);

		// calculate status and duration
		trips.forEach(trip -> {
			trip.setStatus(TripUtil.calculateStatus(trip));
			trip.setDuration(TripUtil.calculateDuration(trip));
		});

		// calculate charge and sort
		return trips.stream()
				.map(tripCalculatorFactory::getCalculator)
				.map(TripCalculator::getChargedTrip)
				.sorted(Comparator.comparing(Trip::getStarted))
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
		return createTrip(tapOn, Tap.builder().build());
	}
}
