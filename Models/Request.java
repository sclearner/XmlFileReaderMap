package Models;

public class Request {
    int index;
    int respone;
    int foes;
    boolean cont;

    public Request(String index, String respone, String foes, String cont) {
        this.index = Integer.parseInt(index);
        this.respone = Integer.parseInt(respone, 2);
        this.foes = Integer.parseInt(foes, 2);
        this.cont = (cont.equals("0"))? false: true;
    }
}
