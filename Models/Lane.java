package Models;

public class Lane {
	// Attibutes
	String id;
	int index;
	String allow;
	String disallow;
	double speed;
	double length;
	double width;
	public Shape shape;

	Lane(String id, int index, String allow, String disallow, double speed, double length, double width, Shape shape) {
		this.id = id;
		this.index = index;
		this.allow = allow;
		this.disallow = disallow;
		this.speed = speed;
		this.length = length;
		this.width = width;
		this.shape = shape;
	}
}
