package Models;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class City {
	public double x;
	public HashMap<String, Edge> edges;
	private HashMap<String, String> fromToEdgesID= new HashMap<String, String>();
	private HashMap<String, ArrayList<String> > fromTo= new HashMap<String, ArrayList<String> >();
	private HashMap<String, ArrayList<String> > toFrom= new HashMap<String, ArrayList<String> >();
	public HashMap<String, Junction> junctions;
	private HashMap<String, ArrayList<String> > junctionsByType= new HashMap<String, ArrayList<String> >();
	public HashMap<String, Connection> connections;
	private HashMap<String, ArrayList<String> > superConnections = new HashMap<String, ArrayList<String> >();

	City() {
		edges = new HashMap<String, Edge>();
	}


	City(HashMap<String, Edge> edges, HashMap<String, Junction> junctions, HashMap<String, Connection> connections) {
		this.edges = edges;
		this.junctions = junctions;
		this.connections = connections;

		for (var edge: edges.values()) {
			if (edge.from == "" || edge.to == "") continue;
			fromToEdgesID.put(String.format("%s|%s", edge.from, edge.to), edge.id);
			if (!this.fromTo.containsKey(edge.from))
				this.fromTo.put(edge.from, new ArrayList<String>());
			ArrayList<String> newTo = this.fromTo.get(edge.from);
			newTo.add(edge.to);
			this.fromTo.replace(edge.from, newTo);
			if (!this.toFrom.containsKey(edge.to))
				this.toFrom.put(edge.to, new ArrayList<String>());
			ArrayList<String> newFrom = this.toFrom.get(edge.to);
			newFrom.add(edge.from);
			this.toFrom.replace(edge.to, newFrom);
		}

		for (var junction: junctions.values()) {
			if (!this.junctionsByType.containsKey(junction.type))
				this.junctionsByType.put(junction.type, new ArrayList<String>());
			ArrayList<String> newJunctions = this.junctionsByType.get(junction.type);
			newJunctions.add(junction.id);
			this.junctionsByType.replace(junction.type, newJunctions);
		}

		for (var connection : connections.values()) {
			if (!this.superConnections.containsKey(connection.from))
				this.superConnections.put(connection.from, new ArrayList<String>());
			ArrayList<String> toEdge = this.superConnections.get(connection.from);
			toEdge.add(connection.to);
			this.superConnections.replace(connection.from, toEdge);
		}
	}

	private static ArrayList<Lane> lanesFromXML(NodeList laneNList) {
		ArrayList<Lane> lanes = new ArrayList<Lane>();
		for (int j = 0; j < laneNList.getLength(); j++) {
			Node laneNode = laneNList.item(j);
			if (laneNode.getNodeType() != Node.ELEMENT_NODE) continue;
			Element laneElement = (Element) laneNode;
			String index = laneElement.getAttribute("index");
			String speed = laneElement.getAttribute("speed");
			String length = laneElement.getAttribute("length");
			String width = laneElement.getAttribute("width");
			Lane lane = new Lane(
				laneElement.getAttribute("id"),
				(index == "")? 0:Integer.parseInt(index), 
				laneElement.getAttribute("allow"),
				laneElement.getAttribute("disallow"),
				(speed == "")? 0: Double.parseDouble(speed),
				(length == "")? 0: Double.parseDouble(length),
				(width == "")? 0: Double.parseDouble(width),
				Shape.parseShape(laneElement.getAttribute("shape"))
			);
			lanes.add(lane);			
		}
		return lanes;
	}

	private static HashMap<String, Edge> edgesFromXML(NodeList nList) {
		HashMap<String, Edge> edges = new HashMap<String, Edge>();
		for (int i = 0; i < nList.getLength(); i++) {
			Node edgeNode = nList.item(i);
			if (edgeNode.getNodeType() == Node.ELEMENT_NODE) {
				Element edgeElement = (Element) edgeNode;
				NodeList laneNList = edgeElement.getElementsByTagName("lane");
				
				String priority = edgeElement.getAttribute("priority");
				try {
				Edge currentEdge = new Edge(
					edgeElement.getAttribute("id"), 
					edgeElement.getAttribute("from"),
					edgeElement.getAttribute("to"),
					edgeElement.getAttribute("function"),
					(priority == "")?0:Integer.parseInt(priority), 
					lanesFromXML(laneNList));
				edges.put(currentEdge.id, currentEdge);
				}
				catch (Exception e) {System.out.print(e.getMessage() + "\n");}
			}
		}
		return edges;
	}

	private static ArrayList<Request> requestsFromXML(NodeList nList) {
		ArrayList<Request> requests = new ArrayList<Request>();
		for (int i = 0; i < nList.getLength(); i++) {
			Node requestNode = nList.item(i);
			if (requestNode.getNodeType() == Node.ELEMENT_NODE) {
				Element rElement = (Element)requestNode;
				requests.add(new Request(
					rElement.getAttribute("index"),
					rElement.getAttribute("response"),
					rElement.getAttribute("foes"),
					rElement.getAttribute("cont")
					)
				);
			}
		}
		return requests;
	}

	private static HashMap<String, Junction> junctionsFromXML(NodeList nList) {
		HashMap<String, Junction> junctions = new HashMap<String, Junction>();
		for (int i = 0; i < nList.getLength(); i++) {
			Node junctionNode = nList.item(i);
			if (junctionNode.getNodeType() == Node.ELEMENT_NODE) {
				Element junctionElement = (Element) junctionNode;
				NodeList requestNList = junctionElement.getElementsByTagName("request");
				try {
				Junction currentJunction = new Junction(
					junctionElement.getAttribute("id"), 
					junctionElement.getAttribute("type"),
					junctionElement.getAttribute("x"),
					junctionElement.getAttribute("y"),
					junctionElement.getAttribute("incLanes"),
					junctionElement.getAttribute("intLanes"),
					junctionElement.getAttribute("shape"),
					requestsFromXML(requestNList));
				junctions.put(currentJunction.id, currentJunction);
				}
				catch (Exception e) {System.out.print(e.getMessage() + "\n");}
			}
		}
		return junctions;
	}

	private static HashMap<String, Connection> connectionsFromXML(NodeList nList) {
		HashMap<String, Connection> connections = new HashMap<String, Connection>();
		for (int i = 0; i < nList.getLength(); i++) {
			Node cNode = nList.item(i);
			if (cNode.getNodeType() == Node.ELEMENT_NODE) {
				Element cElement = (Element)cNode;
				Connection connection = new Connection(
					cElement.getAttribute("from"),
					cElement.getAttribute("to"),
					cElement.getAttribute("fromLane"),
					cElement.getAttribute("toLane"),
					cElement.getAttribute("via"),
					cElement.getAttribute("dir"),
					cElement.getAttribute("state")
					);
				connections.put(String.format("%s %s", connection.from, connection.to), connection
				);
			}
		}
		return connections;
	}

	public static City fromXML(Document doc) throws Exception {
		doc.getDocumentElement().normalize();
		NodeList netList = doc.getElementsByTagName("net");
		Element nElement = (Element)netList.item(0);
		HashMap<String, Edge> edges = edgesFromXML(nElement.getElementsByTagName("edge"));
		HashMap<String, Junction> junctions = junctionsFromXML(nElement.getElementsByTagName("junction"));
		HashMap<String, Connection> connections = connectionsFromXML(nElement.getElementsByTagName("connection"));

		return new City(edges, junctions, connections);
	}

	public ArrayList<String> EdgeConnected(String from) {
		return superConnections.get(from);
	}

	public ArrayList<String> JunctionsByType(String type) {
		return junctionsByType.get(type);
	}

	public ArrayList<String> FromDetectEdgeID(String from) {
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<String> tos = fromTo.get(from);
		if (tos != null) for (var to: tos) {
			String id = fromToEdgesID.get(String.format("%s|%s", from, to));
			if (id != "" && id != null) result.add(id);
		}
		return result;
	}

	public ArrayList<String> toEJ(String from) {
		ArrayList<String> res = new ArrayList<String>();
		Edge thisEdge = edges.get(from);
		if (thisEdge != null) {
			res.add(thisEdge.to); //Add junction
			for (var edgeID: FromDetectEdgeID(thisEdge.to)) res.add(edgeID);
		}
		return res;
	}

	public ArrayList<String> ToDetectEdgeID(String to) {
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<String> froms = toFrom.get(to);
		if (froms != null) for (var from: froms) {
			String id = fromToEdgesID.get(String.format("%s|%s", from, to));
			if (id != "" && id != null) result.add(id);
		}
		return result;
	}
}
