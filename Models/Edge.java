package Models;

import java.util.ArrayList;
import java.util.List;

import Exceptions.*;

public class Edge implements Comparable<Edge>{
	public String id;
	public String from;
	public String to;
	String function;
	int priority;
	public List<Lane> lanes;
	private int whereDisallowPedestrian = -1;

	Edge(String id, String from, String to, String function, int priority, Lane... lanes)
			throws NoIdInEdgeException, TooMuchDisallowPedestrianLaneException, ZeroDistanceException {
		if (id == null)
			throw new NoIdInEdgeException();
		if (from.equals(to))
			throw new ZeroDistanceException();
		this.id = id;
		this.from = from;
		this.to = to;
		this.function = function;
		this.priority = priority;
		this.lanes = new ArrayList<Lane>();
		for (int i = 0; i < lanes.length; i++) {
			Lane lane = lanes[i];
			if (lane.disallow == "pedestrian") {
				if (whereDisallowPedestrian == -1)
					whereDisallowPedestrian = i;
				else
					throw new TooMuchDisallowPedestrianLaneException();
			}
			this.lanes.add(lane);
		}
	}

	Edge(String id, String from, String to, String function, int priority, ArrayList<Lane> lanes)
			throws NoIdInEdgeException, TooMuchDisallowPedestrianLaneException, ZeroDistanceException, NoDisallowPedestrianLaneException {
		if (id == null)
			throw new NoIdInEdgeException();
		if (from.equals(to) && function == "")
			throw new ZeroDistanceException();
		this.id = id;
		this.from = from;
		this.to = to;
		this.function = function;
		this.priority = priority;
		this.lanes = new ArrayList<Lane>();
		for (int i = 0; i < lanes.size(); i++) {
			Lane lane = lanes.get(i);
			if (lane.disallow.equals("pedestrian")) {
				if (whereDisallowPedestrian == -1)
					whereDisallowPedestrian = i;
				else
					throw new TooMuchDisallowPedestrianLaneException();
			}
			this.lanes.add(lane);
		}
		if (whereDisallowPedestrian == -1 && !(function.equals("crossing") || function.equals("walkingarea"))) throw new NoDisallowPedestrianLaneException();
	}

	public String toString() {
		Lane lane = this.lanes.get(whereDisallowPedestrian);
		return String.format("%s %s %s %s", id, from, to, lane.shape.toString());
	}

	public String toStr() {
		Lane lane = this.lanes.get(whereDisallowPedestrian);
		return String.format("%s %s", id, lane.shape.toString());
	}

	@Override
    public int compareTo(Edge that) {
        return this.id.compareTo(that.id);
    } 

	public int getDisallowPedestrian() {
		return whereDisallowPedestrian;
	}
}