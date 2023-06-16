package Models;

public class Connection implements Comparable<Connection>{
    String from;
    String to;
    public int fromLane;
    public int toLane;
    public String via;
    String dir;
    String state;

    public Connection(String from, String to, String fromLane, String toLane, String via, String dir, String state) {
        this.from = from;
        this.to = to;
        this.fromLane = (fromLane.equals(""))? -1 : Integer.parseInt(fromLane); 
        this.toLane = (toLane.equals(""))? -1: Integer.parseInt(toLane);
        this.via = via;
        this.dir = dir;
        this.state = state;
    }

    @Override
    public int compareTo(Connection that) {
        return String.format("%s %s", this.from, this.to).compareTo(String.format("%s %s", that.from, that.to));
    } 
}
