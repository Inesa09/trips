package com.littlepay.trips.dto;

import java.time.LocalDateTime;

import com.littlepay.trips.enums.Stop;
import com.littlepay.trips.enums.TripStatus;

public class Trip {

	private LocalDateTime started;
	private LocalDateTime finished;
	private Integer duration;
	private Stop fromStop;
	private Stop toStop;
	private Double charged;
	private String companyId;
	private String busId;
	private String pan;
	private TripStatus status;

	public LocalDateTime getStarted() {
		return started;
	}

	public Trip setStarted(LocalDateTime started) {
		this.started = started;
		return this;
	}

	public LocalDateTime getFinished() {
		return finished;
	}

	public Trip setFinished(LocalDateTime finished) {
		this.finished = finished;
		return this;
	}

	public Integer getDuration() {
		return duration;
	}

	public Trip setDuration(Integer duration) {
		this.duration = duration;
		return this;
	}

	public Stop getFromStop() {
		return fromStop;
	}

	public Trip setFromStop(Stop fromStop) {
		this.fromStop = fromStop;
		return this;
	}

	public Stop getToStop() {
		return toStop;
	}

	public Trip setToStop(Stop toStop) {
		this.toStop = toStop;
		return this;
	}

	public Double getCharged() {
		return charged;
	}

	public Trip setCharged(Double charged) {
		this.charged = charged;
		return this;
	}

	public String getCompanyId() {
		return companyId;
	}

	public Trip setCompanyId(String companyId) {
		this.companyId = companyId;
		return this;
	}

	public String getBusId() {
		return busId;
	}

	public Trip setBusId(String busId) {
		this.busId = busId;
		return this;
	}

	public String getPan() {
		return pan;
	}

	public Trip setPan(String pan) {
		this.pan = pan;
		return this;
	}

	public TripStatus getStatus() {
		return status;
	}

	public Trip setStatus(TripStatus status) {
		this.status = status;
		return this;
	}

	public static Trip builder() {
		return new Trip();
	}
}
