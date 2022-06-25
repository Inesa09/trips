package com.littlepay.trips.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.littlepay.trips.config.TapInfo;
import com.littlepay.trips.config.TripInfo;
import com.littlepay.trips.dto.Trip;
import com.littlepay.trips.enums.Stop;
import com.littlepay.trips.enums.TapType;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

@Service
public class CSVServiceImpl implements CSVService {

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	private static final String DELIMITER = ", ";
	private static final String RECORD_SEPARATOR = "\n";
	private static final String CURRENCY_SIGN = "$";

	@Override
	public List<com.littlepay.trips.dto.Tap> parseTaps(InputStream is) {
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
		     CSVParser csvParser = new CSVParser(fileReader, CSVFormat.Builder.create().setIgnoreSurroundingSpaces(true).setSkipHeaderRecord(true)
				     .setHeader(TapInfo.ID, TapInfo.DATE_TIME, TapInfo.TAP_TYPE, TapInfo.STOP, TapInfo.COMPANY, TapInfo.BUS, TapInfo.PAN).build());
		) {
			return csvParser.getRecords().stream()
					.map(csvRecord -> com.littlepay.trips.dto.Tap.builder()
							.id(Integer.parseInt(csvRecord.get(TapInfo.ID)))
							.datetime(LocalDateTime.parse(csvRecord.get(TapInfo.DATE_TIME), DATE_TIME_FORMATTER))
							.tapType(TapType.valueOf(csvRecord.get(TapInfo.TAP_TYPE)))
							.stop(Stop.fromStopId(csvRecord.get(TapInfo.STOP)))
							.companyId(csvRecord.get(TapInfo.COMPANY))
							.busId(csvRecord.get(TapInfo.BUS))
							.pan(csvRecord.get(TapInfo.PAN))
							.build())
					.collect(Collectors.toList());
		} catch (IOException e) {
			throw new IllegalStateException("Failed to parse CSV file: " + e.getMessage());
		}
	}

	public void writeTrips(Writer writer, List<Trip> trips) {
		try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.Builder.create().setDelimiter(DELIMITER)
				.setRecordSeparator(RECORD_SEPARATOR)
				.setHeader(TripInfo.STARTED, TripInfo.FINISHED, TripInfo.DURATION, TripInfo.FROM, TripInfo.TO,
						TripInfo.CHARGED, TripInfo.COMPANY, TripInfo.BUS, TripInfo.PAN, TripInfo.STATUS).build());
		) {
			for (Trip trip : trips) {
				csvPrinter.printRecord(formatDateTime(trip.getStarted()), formatDateTime(trip.getFinished()),
						trip.getDuration(), trip.getFromStop().toString(), trip.getToStop(), formatCharge(trip.getCharged()),
						trip.getCompanyId(), trip.getBusId(), trip.getPan(), trip.getStatus());
			}
		} catch (IOException e) {
			throw new IllegalStateException("Error while writing CSV ", e);
		}
	}

	private String formatDateTime(LocalDateTime dateTime) {
		return DATE_TIME_FORMATTER.format(dateTime);
	}

	private String formatCharge(Double charge) {
		return CURRENCY_SIGN + charge;
	}
}
