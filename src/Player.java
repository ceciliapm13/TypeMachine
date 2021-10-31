import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Player implements Runnable {

    private Socket userSocket;
    private String name;
    private int score;
    private PrintWriter out;
    private Game game;
    private String promptMsg;
    private boolean prompted;

    public Player(Socket player, Game game) {
        this.game = game;
        this.userSocket = player;
        score = 0;

        try {
            out = new PrintWriter(new OutputStreamWriter(this.getUserSocket().getOutputStream()), true);
        } catch (Exception e) {
        }
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

    public void prompt(String message) {
        try {
            if (prompted) {
                Prompt prompt = new Prompt(getUserSocket().getInputStream(), new PrintStream(getUserSocket().getOutputStream()));

                StringInputScanner question1 = new StringInputScanner();

                question1.setMessage(message);

                String input = prompt.getUserInput(question1);
                game.getMessage(this, input);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void run() {

        try {
            Prompt prompt = new Prompt(getUserSocket().getInputStream(), new PrintStream(getUserSocket().getOutputStream()));

            StringInputScanner question1 = new StringInputScanner();
            String welcome = ("\033[H\033[2J" +
                    "---------------------------\n" +
                    "Welcome to TypingMachine!!!\n" +
                    "---------------------------\n" +
                    "\n\nWhat is your name? \n");

            question1.setMessage(welcome);

            this.name = prompt.getUserInput(question1);

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        while (true) {

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            prompt(promptMsg);
        }
    }

    public void setPrompted(String challenge) {
        promptMsg = challenge;
        prompted = true;
    }
}
