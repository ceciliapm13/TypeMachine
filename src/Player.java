import java.net.Socket;

public class Player {

    private Socket userSocket;
    private String name;
    // private ... color;
    private int score;

    public Player() {


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

    public void setScore(int score) {
        this.score = score;
    }

    public Socket getUserSocket() {
        return userSocket;
    }

    //TODO add socket to server
    public void setUserSocket(Socket userSocket) {
        this.userSocket = userSocket;
    }
}
