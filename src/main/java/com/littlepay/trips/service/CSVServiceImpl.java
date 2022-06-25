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

import com.littlepay.trips.dto.Tap;
import com.littlepay.trips.dto.Trip;
import com.littlepay.trips.enums.Stop;
import com.littlepay.trips.enums.TapType;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

@Service
public class CSVServiceImpl implements CSVService {

	@Override
	public List<Tap> parseTaps(InputStream is) {
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
		     CSVParser csvParser = new CSVParser(fileReader, CSVFormat.EXCEL.withFirstRecordAsHeader().withIgnoreSurroundingSpaces())) {
			return csvParser.getRecords().stream()
					.map(csvRecord -> Tap.builder()
							.id(Integer.parseInt(csvRecord.get("ID")))
							.datetime(LocalDateTime.parse(csvRecord.get("DateTimeUTC"),
									DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))
							.tapType(TapType.valueOf(csvRecord.get("TapType")))
							.stop(Stop.fromStopId(csvRecord.get("StopId")))
							.companyId(csvRecord.get("CompanyId"))
							.busId(csvRecord.get("BusID"))
							.pan(csvRecord.get("PAN"))
							.build())
					.collect(Collectors.toList());
		} catch (IOException e) {
			throw new IllegalStateException("Failed to parse CSV file: " + e.getMessage());
		}
	}

	public void writeTrips(Writer writer, List<Trip> trips) {
		try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
			for (Trip trip : trips) {
				csvPrinter.printRecord(trip.getStarted(), trip.getFinished(), trip.getDuration(), trip.getFromStop(),
						trip.getToStop(), trip.getCharged(), trip.getCompanyId(), trip.getBusId(), trip.getPan(), trip.getStatus());
			}
		} catch (IOException e) {
			throw new IllegalStateException("Error while writing CSV ", e);
		}
	}
}
