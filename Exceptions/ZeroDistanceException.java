package Exceptions;

public class ZeroDistanceException extends Exception {

	private static final long serialVersionUID = 496397954484680265L;

	public ZeroDistanceException() {
		super("Start-point and end-point need to be different.");
	}

	public ZeroDistanceException(String message) {
		super(message);
	}
}