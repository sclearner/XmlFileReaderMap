package Models;

import java.util.List;
import java.util.ArrayList;

public class Shape {
	List<Point> vertices = new ArrayList<Point>();

	Shape() {}

	Shape(Point... vertices) {
		for (var vertex : vertices) {
			this.vertices.add(vertex);
		}
	}

	Shape(ArrayList<Point> vertices) {
		for (var vertex : vertices) {
			this.vertices.add(vertex);
		}
	}

	public String toString() {
		String res = "";
		for (var vertex : vertices) {
			res += vertex.toString() + " ";
		}
		return res.stripTrailing();
	}

	public static Shape parseShape(String s) {
		String[] points = s.split(" ");
		ArrayList<Point> currentVertices = new ArrayList<Point>();
		for (var point : points) {
			currentVertices.add(Point.parsePoint(point));
		}
		return new Shape(currentVertices);
	}

	public double length() {
		double sum = 0;
		Point recent = vertices.get(0);
		for (int i=1; i<vertices.size(); i++) {
			sum += recent.distance(vertices.get(i));
			recent = vertices.get(i);
		}
		return sum;
	}

	public String partition(double x) {
		ArrayList<ArrayList<Point>> newPartition = new ArrayList<ArrayList<Point>>();
		ArrayList<Point> thisPartition = new ArrayList<Point>();
		Point recent = vertices.get(0);
		double radius = x;
		int i = 1;
		thisPartition.add(recent);
		while (i < vertices.size()) {
			Point now = vertices.get(i);
			if (!now.isNearRadius(recent, radius)) {
				Point nearestRecent = now.onRadius(recent, radius);
				thisPartition.add(nearestRecent);
				newPartition.add((ArrayList<Point>)thisPartition.clone());
				thisPartition.clear();
				recent = nearestRecent;
				thisPartition.add(recent);
				radius = x;
			}
			else {
				radius -= now.distance(recent);
				recent = now;
				thisPartition.add(recent);
				i++;
			}
		}
		newPartition.add(thisPartition);
		String result = "";
		for (i = 0; i < newPartition.size(); i++) {
			result += String.format("%d", i);
			for (var point: newPartition.get(i)) result += String.format("_%s", point.toString());
			result += " ";
		}
		return result.stripTrailing();
	}
}
