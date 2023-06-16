package Exceptions;

public class TooMuchDisallowPedestrianLaneException extends Exception {

	private static final long serialVersionUID = 496397954484680265L;

	public TooMuchDisallowPedestrianLaneException() {
		super("Only one disallow pedestrian lane in an edge.");
	}

	public TooMuchDisallowPedestrianLaneException(String message) {
		super(message);
	}
}