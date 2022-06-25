package com.littlepay.trips.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import javax.servlet.http.HttpServletResponse;

import com.littlepay.trips.util.FilesHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/trip")
public class TripController {

	public static final String CSV_TYPE = "text/csv";
	public static final String CONTENT_DISPOSITION_HEADER = "Content-Disposition";
	public static final String CONTENT_DISPOSITION_VALUE = "attachment; filename=\"%s\"";

	@Value("${taps.filename}")
	private String tapsFilename;

	@Value("${trips.filename}")
	private String tripsFilename;

	@Value("${resource.dir}")
	private String resourceDir;

	final FilesHandler filesHandler;

	public TripController(FilesHandler filesHandler) {
		this.filesHandler = filesHandler;
	}

	@GetMapping
	public ResponseEntity<String> get() {
		try (InputStream in = new FileInputStream(ResourceUtils.getFile(resourceDir + "/" + tapsFilename))) {
			File outputFile = new File(ResourceUtils.getFile(resourceDir) + "/" + tripsFilename);
			outputFile.createNewFile();
			Writer writer = new FileWriter(outputFile);

			filesHandler.convertTapsIntoTrips(in, writer);
		} catch (IOException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("Success", HttpStatus.OK);
	}

	@PostMapping
	public void uploadFile(@RequestParam("taps") MultipartFile file, HttpServletResponse httpServletResponse) throws IOException {
		if (!CSV_TYPE.equals(file.getContentType())) {
			httpServletResponse.sendError(HttpStatus.BAD_REQUEST.value(), "Please upload a csv file!");
		}

		try {
			httpServletResponse.setContentType(CSV_TYPE);
			httpServletResponse.setHeader(CONTENT_DISPOSITION_HEADER, String.format(CONTENT_DISPOSITION_VALUE, tripsFilename));
			httpServletResponse.setStatus(HttpStatus.OK.value());

			filesHandler.convertTapsIntoTrips(file.getInputStream(), httpServletResponse.getWriter());
		} catch (Exception e) {
			httpServletResponse.sendError(HttpStatus.BAD_REQUEST.value(), "Could calculate trips: " + e.getMessage());
		}
	}
}
