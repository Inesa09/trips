package com.littlepay.trips.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.littlepay.trips.dto.Tap;
import com.littlepay.trips.dto.Trip;
import com.littlepay.trips.enums.Stop;
import com.littlepay.trips.enums.TapType;
import com.littlepay.trips.enums.TripStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TripServiceTest {

	@Autowired
	TripService tripService;

	@Test
	void testCompleted() {
		List<Tap> taps = new ArrayList<>();
		LocalDateTime started = LocalDateTime.of(2022, 6, 23, 10, 0, 0);
		LocalDateTime finished = LocalDateTime.of(2022, 6, 23, 10, 10, 0);
		addTapOn(taps, started, Stop.ONE);
		addTapOff(taps, finished, Stop.TWO);

		List<Trip> trips = tripService.findTripsByTaps(taps);

		assertThat(trips.size()).isEqualTo(1);
		assertCompletedTrip(trips.get(0), started, finished, 600L, Stop.ONE, Stop.TWO, 3.25);
	}

	@Test
	void testIncomplete() {
		List<Tap> taps = new ArrayList<>();
		LocalDateTime started = LocalDateTime.of(2022, 6, 23, 10, 0, 0);
		addTapOn(taps, started, Stop.ONE);

		List<Trip> trips = tripService.findTripsByTaps(taps);

		assertThat(trips.size()).isEqualTo(1);
		assertIncompleteTrip(trips.get(0), started, Stop.ONE, 7.3);
	}

	@Test
	void testCancelled() {
		List<Tap> taps = new ArrayList<>();
		LocalDateTime started = LocalDateTime.of(2022, 6, 23, 10, 0, 0);
		LocalDateTime finished = LocalDateTime.of(2022, 6, 23, 10, 1, 0);
		addTapOn(taps, started, Stop.ONE);
		addTapOff(taps, finished, Stop.ONE);

		List<Trip> trips = tripService.findTripsByTaps(taps);

		assertThat(trips.size()).isEqualTo(1);
		assertCancelledTrip(trips.get(0), started, finished, 60L, Stop.ONE);
	}

	@Test
	void testCompletedMultiple() {
		List<Tap> taps = new ArrayList<>();
		LocalDateTime started1 = LocalDateTime.of(2022, 6, 23, 10, 0, 0);
		LocalDateTime finished2 = LocalDateTime.of(2022, 6, 23, 10, 10, 0);
		addTapOn(taps, started1, Stop.ONE);
		addTapOff(taps, finished2, Stop.TWO);

		LocalDateTime started2 = LocalDateTime.of(2022, 6, 23, 10, 20, 0);
		LocalDateTime finished3 = LocalDateTime.of(2022, 6, 23, 10, 30, 0);
		addTapOn(taps, started2, Stop.TWO);
		addTapOff(taps, finished3, Stop.THREE);

		// testing reverse stops order as well
		LocalDateTime started3 = LocalDateTime.of(2022, 6, 23, 10, 40, 0);
		LocalDateTime finished1 = LocalDateTime.of(2022, 6, 23, 10, 50, 0);
		addTapOn(taps, started3, Stop.THREE);
		addTapOff(taps, finished1, Stop.ONE);

		List<Trip> trips = tripService.findTripsByTaps(taps);

		assertThat(trips.size()).isEqualTo(3);
		assertCompletedTrip(trips.get(0), started1, finished2, 600L, Stop.ONE, Stop.TWO, 3.25);
		assertCompletedTrip(trips.get(1), started2, finished3, 600L, Stop.TWO, Stop.THREE, 5.5);
		assertCompletedTrip(trips.get(2), started3, finished1, 600L, Stop.THREE, Stop.ONE, 7.3);
	}

	/**
	 * Assuming the tap ons/offs are not sorted by time by default
	 */
	@Test
	void testCompletedMultipleWithShuffledOrder() {
		List<Tap> taps = new ArrayList<>();
		LocalDateTime started1 = LocalDateTime.of(2022, 6, 23, 10, 0, 0);
		LocalDateTime finished2 = LocalDateTime.of(2022, 6, 23, 10, 10, 0);
		LocalDateTime started2 = LocalDateTime.of(2022, 6, 23, 10, 20, 0);
		LocalDateTime finished3 = LocalDateTime.of(2022, 6, 23, 10, 30, 0);
		LocalDateTime started3 = LocalDateTime.of(2022, 6, 23, 10, 40, 0);
		LocalDateTime finished1 = LocalDateTime.of(2022, 6, 23, 10, 50, 0);

		addTapOn(taps, started1, Stop.ONE);
		addTapOn(taps, started2, Stop.TWO);
		addTapOff(taps, finished3, Stop.THREE);
		addTapOff(taps, finished2, Stop.TWO);
		addTapOn(taps, started3, Stop.THREE);
		addTapOff(taps, finished1, Stop.ONE);

		List<Trip> trips = tripService.findTripsByTaps(taps);

		assertThat(trips.size()).isEqualTo(3);
		assertCompletedTrip(trips.get(0), started1, finished2, 600L, Stop.ONE, Stop.TWO, 3.25);
		assertCompletedTrip(trips.get(1), started2, finished3, 600L, Stop.TWO, Stop.THREE, 5.5);
		assertCompletedTrip(trips.get(2), started3, finished1, 600L, Stop.THREE, Stop.ONE, 7.3);
	}

	@Test
	void testCompletedAndIncomplete() {
		List<Tap> taps = new ArrayList<>();
		LocalDateTime started1 = LocalDateTime.of(2022, 6, 23, 10, 0, 0);
		addTapOn(taps, started1, Stop.ONE);

		LocalDateTime started2 = LocalDateTime.of(2022, 6, 23, 10, 20, 0);
		LocalDateTime finished3 = LocalDateTime.of(2022, 6, 23, 10, 30, 0);
		addTapOn(taps, started2, Stop.TWO);
		addTapOff(taps, finished3, Stop.THREE);

		LocalDateTime started3 = LocalDateTime.of(2022, 6, 23, 10, 40, 0);
		addTapOn(taps, started3, Stop.THREE);

		List<Trip> trips = tripService.findTripsByTaps(taps);

		assertThat(trips.size()).isEqualTo(3);
		assertIncompleteTrip(trips.get(0), started1, Stop.ONE, 7.3);
		assertCompletedTrip(trips.get(1), started2, finished3, 600L, Stop.TWO, Stop.THREE, 5.5);
		assertIncompleteTrip(trips.get(2), started3, Stop.THREE, 7.3);
	}

	@Test
	void testCompletedAndIncompleteWithShuffledOrder() {
		List<Tap> taps = new ArrayList<>();
		LocalDateTime started1 = LocalDateTime.of(2022, 6, 23, 10, 0, 0);
		LocalDateTime started2 = LocalDateTime.of(2022, 6, 23, 10, 20, 0);
		LocalDateTime finished3 = LocalDateTime.of(2022, 6, 23, 10, 30, 0);
		LocalDateTime started3 = LocalDateTime.of(2022, 6, 23, 10, 40, 0);

		addTapOn(taps, started1, Stop.ONE);
		addTapOn(taps, started2, Stop.TWO);
		// note: these two should NOT be treated as CANCELLED trip
		addTapOn(taps, started3, Stop.THREE);
		addTapOff(taps, finished3, Stop.THREE);

		List<Trip> trips = tripService.findTripsByTaps(taps);

		assertThat(trips.size()).isEqualTo(3);
		assertIncompleteTrip(trips.get(0), started1, Stop.ONE, 7.3);
		assertCompletedTrip(trips.get(1), started2, finished3, 600L, Stop.TWO, Stop.THREE, 5.5);
		assertIncompleteTrip(trips.get(2), started3, Stop.THREE, 7.3);
	}


	@Test
	void testCompletedAndCancelled() {
		List<Tap> taps = new ArrayList<>();
		LocalDateTime started1 = LocalDateTime.of(2022, 6, 23, 10, 0, 0);
		LocalDateTime finished1 = LocalDateTime.of(2022, 6, 23, 10, 1, 0);
		addTapOn(taps, started1, Stop.ONE);
		addTapOff(taps, finished1, Stop.ONE);

		LocalDateTime started2 = LocalDateTime.of(2022, 6, 23, 10, 20, 0);
		LocalDateTime finished3 = LocalDateTime.of(2022, 6, 23, 10, 30, 0);
		addTapOn(taps, started2, Stop.TWO);
		addTapOff(taps, finished3, Stop.THREE);

		List<Trip> trips = tripService.findTripsByTaps(taps);

		assertThat(trips.size()).isEqualTo(2);
		assertCancelledTrip(trips.get(0), started1, finished1, 60L, Stop.ONE);
		assertCompletedTrip(trips.get(1), started2, finished3, 600L, Stop.TWO, Stop.THREE, 5.5);
	}

	@Test
	void testCompletedAndCancelledWithShuffledOrder() {
		List<Tap> taps = new ArrayList<>();
		LocalDateTime started1 = LocalDateTime.of(2022, 6, 23, 10, 0, 0);
		LocalDateTime finished1 = LocalDateTime.of(2022, 6, 23, 10, 1, 0);
		LocalDateTime started2 = LocalDateTime.of(2022, 6, 23, 10, 20, 0);
		LocalDateTime finished3 = LocalDateTime.of(2022, 6, 23, 10, 30, 0);

		addTapOn(taps, started1, Stop.ONE);
		addTapOff(taps, finished3, Stop.THREE);
		addTapOn(taps, started2, Stop.TWO);
		addTapOff(taps, finished1, Stop.ONE);

		List<Trip> trips = tripService.findTripsByTaps(taps);

		assertThat(trips.size()).isEqualTo(2);
		assertCancelledTrip(trips.get(0), started1, finished1, 60L, Stop.ONE);
		assertCompletedTrip(trips.get(1), started2, finished3, 600L, Stop.TWO, Stop.THREE, 5.5);
	}

	@Test
	void testIncompleteAndCancelled() {
		List<Tap> taps = new ArrayList<>();
		LocalDateTime started1 = LocalDateTime.of(2022, 6, 23, 10, 0, 0);
		LocalDateTime finished1 = LocalDateTime.of(2022, 6, 23, 10, 1, 0);
		addTapOn(taps, started1, Stop.ONE);
		addTapOff(taps, finished1, Stop.ONE);

		LocalDateTime started2 = LocalDateTime.of(2022, 6, 23, 10, 20, 0);
		addTapOn(taps, started2, Stop.TWO);

		List<Trip> trips = tripService.findTripsByTaps(taps);

		assertThat(trips.size()).isEqualTo(2);
		assertCancelledTrip(trips.get(0), started1, finished1, 60L, Stop.ONE);
		assertIncompleteTrip(trips.get(1), started2, Stop.TWO, 5.5);
	}

	@Test
	void testIncompleteAndCancelledWithShuffledOrder() {
		List<Tap> taps = new ArrayList<>();
		LocalDateTime started1 = LocalDateTime.of(2022, 6, 23, 10, 0, 0);
		LocalDateTime finished1 = LocalDateTime.of(2022, 6, 23, 10, 1, 0);
		LocalDateTime started2 = LocalDateTime.of(2022, 6, 23, 10, 20, 0);

		addTapOn(taps, started1, Stop.ONE);
		addTapOn(taps, started2, Stop.TWO);
		addTapOff(taps, finished1, Stop.ONE);

		List<Trip> trips = tripService.findTripsByTaps(taps);

		assertThat(trips.size()).isEqualTo(2);
		assertCancelledTrip(trips.get(0), started1, finished1, 60L, Stop.ONE);
		assertIncompleteTrip(trips.get(1), started2, Stop.TWO, 5.5);
	}

	private void addTapOn(List<Tap> taps, LocalDateTime dateTime, Stop stop) {
		addTap(taps, dateTime, stop, TapType.ON);
	}

	private void addTapOff(List<Tap> taps, LocalDateTime dateTime, Stop stop) {
		addTap(taps, dateTime, stop, TapType.OFF);
	}

	private void addTap(List<Tap> taps, LocalDateTime dateTime, Stop stop, TapType tapType) {
		taps.add(Tap.builder()
				.datetime(dateTime)
				.stop(stop)
				.tapType(tapType)
				.build());
	}

	private void assertCompletedTrip(Trip trip, LocalDateTime started, LocalDateTime finished, Long duration, Stop from,
	                                 Stop to, Double charged) {
		assertThat(trip.getStarted()).isEqualTo(started);
		assertThat(trip.getFinished()).isEqualTo(finished);
		assertThat(trip.getDuration()).isEqualTo(duration);
		assertThat(trip.getFromStop()).isEqualTo(from);
		assertThat(trip.getToStop()).isEqualTo(to);
		assertThat(trip.getStatus()).isEqualTo(TripStatus.COMPLETED);
		assertThat(trip.getCharged()).isEqualTo(charged);
	}

	private void assertIncompleteTrip(Trip trip, LocalDateTime started, Stop from, Double charged) {
		assertThat(trip.getStarted()).isEqualTo(started);
		assertThat(trip.getFinished()).isNull();
		assertThat(trip.getDuration()).isNull();
		assertThat(trip.getFromStop()).isEqualTo(from);
		assertThat(trip.getToStop()).isNull();
		assertThat(trip.getStatus()).isEqualTo(TripStatus.INCOMPLETE);
		assertThat(trip.getCharged()).isEqualTo(charged);
	}

	private void assertCancelledTrip(Trip trip, LocalDateTime started, LocalDateTime finished, Long duration, Stop stop) {
		assertThat(trip.getStarted()).isEqualTo(started);
		assertThat(trip.getFinished()).isEqualTo(finished);
		assertThat(trip.getDuration()).isEqualTo(duration);
		assertThat(trip.getFromStop()).isEqualTo(stop);
		assertThat(trip.getToStop()).isEqualTo(stop);
		assertThat(trip.getStatus()).isEqualTo(TripStatus.CANCELLED);
		assertThat(trip.getCharged()).isZero();
	}
}
