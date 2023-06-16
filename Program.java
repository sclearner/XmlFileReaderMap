import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import Models.City;
import Models.Connection;
import Models.Edge;
import Models.Junction;

public class Program {
	static City city;

	static void printEdge(String nameOfEdge) {
		if (city.edges.containsKey(nameOfEdge)) {
				System.out.print(city.edges.get(nameOfEdge).toString() + "\n");
		}
	}

	static void printEdge2Juncs() {
		ArrayList<String> edgeIDs = new ArrayList<String>();
		for(var key: city.edges.keySet()) {
			edgeIDs.add(key);
		};
		for (var fromEdge : edgeIDs) {
			ArrayList<String> junc = new ArrayList<String>(); 
			ArrayList<String> toEdges = city.EdgeConnected(fromEdge);
			if (toEdges == null) continue;
			for (var toEdge: toEdges) {
				Connection connection = city.connections.get(String.format("%s %s",fromEdge, toEdge));
				if (!(connection.fromLane == 1 && connection.toLane == 1)) continue;
				if (connection.via == "") continue;
				String[] viaProcess = connection.via.split("_");
				String jID = String.format("%s_%s", viaProcess[0], viaProcess[1]);
				if (!city.edges.containsKey(jID)) continue;
				if (city.edges.get(jID).getDisallowPedestrian() == -1) continue;
				if (junc.size() < 3) junc.add(jID);		
			}
			if (junc.size() > 0) {
				System.out.print(city.edges.get(fromEdge).toStr());
				for (var jID: junc) {
					System.out.print(" " + city.edges.get(jID).toStr());
				}
				System.out.print("\n");
			}
		}
	}

	static ArrayList<String> StartEdges() {
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<String> startJunctions = city.JunctionsByType("dead_end");
		for (var junction: startJunctions) {
			for(var id: city.FromDetectEdgeID(junction)) {
				result.add(id);
			}
		}
		return result;
	}

	static void printStart() {
		ArrayList<String> startJunctions = city.JunctionsByType("dead_end");
		for (var junction: startJunctions) {
			for(var id: city.FromDetectEdgeID(junction)) {
				printEdge(id);
			}
		}
	}

	static void printEnd() {
		ArrayList<String> startJunctions = city.JunctionsByType("dead_end");
		for (var junction: startJunctions) {
			for(var id: city.ToDetectEdgeID(junction)) {
				printEdge(id);
			}
		}
	}

	static ArrayList<String> EndEdges() {
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<String> startJunctions = city.JunctionsByType("dead_end");
		for (var junction: startJunctions) {
			for(var id: city.ToDetectEdgeID(junction)) {
				result.add(id);
			}
		}
		return result;
	}

	static void splitStart(double x, String name) {
		if (city.edges.containsKey(name)) {
			Edge edge = city.edges.get(name);
			int pedestrian = edge.getDisallowPedestrian();
			System.out.print(String.format("%s %s\n", name, edge.lanes.get((pedestrian >= 0)? pedestrian : 0).shape.partition(x)));
		}
	}

	static void split(double x, String name) {
		if (city.edges.containsKey(name)) {
			Edge edge = city.edges.get(name);
			int pedestrian = edge.getDisallowPedestrian();
			System.out.print(String.format("%s %s\n", name, edge.lanes.get((pedestrian >= 0)? pedestrian : 0).shape.partition(x)));
		}
		else if (city.junctions.containsKey(name)) {
			Junction junction = city.junctions.get(name);
			System.out.print(String.format("%s %s\n", name, junction.shape.partition(x)));
		}
	}

	static String splitString(double x, String name) {
		if (city.edges.containsKey(name)) {
			Edge edge = city.edges.get(name);
			int pedestrian = edge.getDisallowPedestrian();
			return String.format("%s %s\n", name, edge.lanes.get((pedestrian >= 0)? pedestrian : 0).shape.partition(x));
		}
		else if (city.junctions.containsKey(name)) {
			Junction junction = city.junctions.get(name);
			return String.format("%s %s\n", name, junction.shape.partition(x));
		}
		return null;
	}

	static void allPath(String path, double x) throws Exception {
		City city = City.fromXML(ReadFile(path));
		try(PrintWriter out = new PrintWriter("AllPaths.txt")){
			ArrayList<String> S = new ArrayList<String>();
			ArrayList<String> T = StartEdges();
			ArrayList<String> E = EndEdges();
			while (!T.isEmpty()) {
				String e = T.remove(0);
				if (!E.contains(e)) {
					out.printf(splitString(x, e));
					S.add(e);
					ArrayList<String> N = city.toEJ(e);
					for (var s: S) {
						if (N.contains(s)) N.remove(s);
					}
					for (var n: N) {
						if (!T.contains(n)) T.add(n);
					}
				}
			}
		};
		
	}

	private static Document ReadFile(String path) throws ParserConfigurationException, SAXException, IOException {
		File file = new File(path);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		return db.parse(file);
	}

	public static void main(String[] args) throws Exception {
		Document doc = ReadFile("vd13.net.xml");
		city = City.fromXML(doc);
		System.out.print("=============== Câu a ===================\n");
		printEdge("E1");
		System.out.print("=============== Câu b ===================\n");
		printEdge2Juncs();
		System.out.print("=============== Câu c ===================\n");
		printStart();
		System.out.print("=============== Câu d ===================\n");
		splitStart(1.4, "E0");
		System.out.print("=============== Câu e ===================\n");
		splitStart(1.4, ":J271_1");
		System.out.print("=============== Câu f ===================\n");
		printEnd();
		System.out.print("=============== Câu g ===================\n");
		allPath("vd13.net.xml", 1.4);
		System.out.print("Hoàn thành");
	}
}
