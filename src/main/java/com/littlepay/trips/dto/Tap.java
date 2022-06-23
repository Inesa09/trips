package trips.dto;

import java.time.LocalDateTime;

import trips.enums.Stop;
import trips.enums.TapType;

public class Tap {

	private Integer id;
	private LocalDateTime datetime;
	private TapType tapType;
	private Stop stop;
	private String companyId;
	private String busId;
	private String pan;

	public Integer getId() {
		return id;
	}

	public Tap setId(Integer id) {
		this.id = id;
		return this;
	}

	public LocalDateTime getDatetime() {
		return datetime;
	}

	public Tap setDatetime(LocalDateTime datetime) {
		this.datetime = datetime;
		return this;
	}

	public TapType getTapType() {
		return tapType;
	}

	public Tap setTapType(TapType tapType) {
		this.tapType = tapType;
		return this;
	}

	public Stop getStop() {
		return stop;
	}

	public Tap setStop(Stop stop) {
		this.stop = stop;
		return this;
	}

	public String getCompanyId() {
		return companyId;
	}

	public Tap setCompanyId(String companyId) {
		this.companyId = companyId;
		return this;
	}

	public String getBusId() {
		return busId;
	}

	public Tap setBusId(String busId) {
		this.busId = busId;
		return this;
	}

	public String getPan() {
		return pan;
	}

	public Tap setPan(String pan) {
		this.pan = pan;
		return this;
	}

	public static Tap builder() {
		return new Tap();
	}
}
