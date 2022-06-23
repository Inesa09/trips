package trips.enums;

public enum Stop {
	ONE("Stop1"),
	TWO("Stop2"),
	THREE("Stop3");

	String stopId;

	Stop(String stopId) {
		this.stopId = stopId;
	}
}
