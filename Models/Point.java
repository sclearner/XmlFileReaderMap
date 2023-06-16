package Models;

public class Point {
	double x;
	double y;

	Point() {
		x = 0;
		y = 0;
	}

	Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public String toString(){
		return String.format("%.2f,%.2f", x, y);
	}

	public static Point parsePoint(String s) {
		String[] axis = s.split(",");
		return new Point(Double.parseDouble(axis[0]), Double.parseDouble(axis[1]));
	}

	public Point distanceVector(Point that) {
		return new Point(that.x - this.x, that.y - this.y);
	}

	public double distance(Point that) {
 		return (double)Math.sqrt(Math.pow(this.x - that.x, 2) + Math.pow(this.y - that.y, 2));
	}

	public boolean isNearRadius(Point O, double R) {
		return O.distance(this) <= R;
	}

	public Point onRadius(Point O, double R) {
		if (this.equals(O)) return O;
		double ratio = R / O.distance(this);
		Point dir = O.distanceVector(this);
		return new Point(O.x + ratio * dir.x, O.y + ratio*dir.y);
	}
}
