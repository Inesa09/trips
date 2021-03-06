package com.littlepay.trips.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
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
class CSVServiceTest {

	private static final LocalDateTime START_DATE = LocalDateTime.of(2018, 1, 22, 13, 0, 0);
	private static final LocalDateTime END_DATE = LocalDateTime.of(2018, 1, 22, 13, 5, 0);
	private static final String COMPANY = "Company1";
	private static final String BUS = "Bus37";
	private static final String PAN = "5500005555555559";
	@Autowired
	CSVService csvService;

	@Test
	void testParseTaps() {
		InputStream inputStream = new ByteArrayInputStream(
				("ID, DateTimeUTC, TapType, StopId, CompanyId, BusID, PAN\n" +
						"1, 22-01-2018 13:00:00, ON, Stop1, Company1, Bus37, 5500005555555559\n" +
						"2, 22-01-2018 13:05:00, OFF, Stop2, Company1, Bus37, 5500005555555559").getBytes());
		List<Tap> taps = csvService.parseTaps(inputStream);

		assertThat(taps.size()).isEqualTo(2);
		Tap tap = taps.get(0);
		assertThat(tap.getId()).isEqualTo(1);
		assertThat(tap.getDatetime()).isEqualTo(START_DATE);
		assertThat(tap.getTapType()).isEqualTo(TapType.ON);
		assertThat(tap.getStop()).isEqualTo(Stop.ONE);
		assertThat(tap.getCompanyId()).isEqualTo(COMPANY);
		assertThat(tap.getBusId()).isEqualTo(BUS);
		assertThat(tap.getPan()).isEqualTo(PAN);
	}

	@Test
	void testWriteTrips() {
		Trip trip1 = Trip.builder()
				.started(START_DATE)
				.finished(END_DATE)
				.duration(300L)
				.fromStop(Stop.ONE)
				.toStop(Stop.TWO)
				.charged(3.25)
				.companyId(COMPANY)
				.busId(BUS)
				.pan(PAN)
				.status(TripStatus.COMPLETED)
				.build();

		Trip trip2 = Trip.builder()
				.started(START_DATE)
				.fromStop(Stop.ONE)
				.charged(3.25)
				.companyId(COMPANY)
				.busId(BUS)
				.pan(PAN)
				.status(TripStatus.INCOMPLETE)
				.build();

		Writer stringWriter = new StringWriter();
		csvService.writeTrips(stringWriter, Arrays.asList(trip1, trip2));

		assertThat(stringWriter).hasToString(
				"Started, Finished, DurationSecs, FromStopId, ToStopId, ChargeAmount, CompanyId, BusID, PAN, Status\n" +
						"22-01-2018 13:00:00, 22-01-2018 13:05:00, 300, Stop1, Stop2, $3.25, Company1, Bus37, 5500005555555559, COMPLETED\n" +
						"22-01-2018 13:00:00, , , Stop1, , $3.25, Company1, Bus37, 5500005555555559, INCOMPLETE\n");
	}
}
