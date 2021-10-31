import java.net.Socket;

public class Player {

    private Socket userSocket;
    private String name;
    // private ... color;
    private int score;

    public Player(Socket userSocket) {
        this.userSocket = userSocket;

    }



    public String getName() {
        return name;
    }

    public String setName(String name) {
        return this.name = name;
    }

    public int getScore() {
        return score;
    }

    public int score(int score) {
        return this.score = score;
    }



}
