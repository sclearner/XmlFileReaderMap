package Exceptions;

public class NoDisallowPedestrianLaneException extends Exception {

	private static final long serialVersionUID = 496397954484680267L;

	public NoDisallowPedestrianLaneException() {
		super("Only one disallow pedestrian lane in an edge.");
	}

	public NoDisallowPedestrianLaneException(String message) {
		super(message);
	}
}