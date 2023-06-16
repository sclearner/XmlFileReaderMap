package Exceptions;

public class NoIdInEdgeException extends Exception {

	private static final long serialVersionUID = -1436943664664104956L;

	public NoIdInEdgeException() {
		super("Need ID in edge.");
	}

	public NoIdInEdgeException(String message) {
		super(message);
	}
}
