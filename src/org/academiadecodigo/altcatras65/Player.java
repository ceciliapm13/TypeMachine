package org.academiadecodigo.altcatras65;

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
    private int challengeCounter;
    private int roundCounter;
    private boolean roundEnd;

    public Player(Socket player, Game game) {
        this.game = game;
        this.userSocket = player;
        score = 0;
        challengeCounter = 0;
        roundCounter = 1;
        roundEnd = true;

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

    //game start: início do jogo a partir do momento em que dois players estão conectados
    public void start(Socket playerSocket) {
        if (roundEnd) {
            System.out.println(Thread.currentThread().getName() + " inside other start method");
            nextChallenge(playerSocket);
            game.givePrompt(); // pedido da resposta do player
            roundEnd = false;
            if (roundCounter == 10) {
                game.endMessage();
            }
        }


    }

    public void prompt(String message) {
        try {
            if (prompted) {
                prompted = false;
                Prompt prompt = new Prompt(getUserSocket().getInputStream(), new PrintStream(getUserSocket().getOutputStream()));

                StringInputScanner question1 = new StringInputScanner();

                question1.setMessage(message);

                String input = prompt.getUserInput(question1);
                game.getMessage(this, input);
            }
        } catch (Exception e) {
        }
    }

    public void nextChallenge(Socket playerSocket) {

        try {
            System.out.println(Thread.currentThread().getName() + " inside next challenge method");
            game.sendMessage(playerSocket, "New challenge \n");
            Thread.sleep(3000);
            game.sendMessage(playerSocket, "Get ready... \n");
            Thread.sleep(2000);
            game.sendMessage(playerSocket, "3\n");
            Thread.sleep(1000);
            game.sendMessage(playerSocket, "2\n");
            Thread.sleep(1000);
            game.sendMessage(playerSocket, "1\n");
            Thread.sleep(1000);
            game.sendMessage(playerSocket, "TYPE!\n");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
            this.game.setPlayerNumber(this.game.getPlayerNumber() + 1);

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        loop:
        while (true) {
            System.out.println("Waiting for players inside while loop");

            for (int i = 0; i < game.getPlayers().length; i++) {
                if (game.getPlayers()[i] != null) {
                    if (game.getPlayers()[i].getName() != null) {
                        game.sendMessage(game.getPlayers()[i].getUserSocket(), "Waiting for players!");
                    }
                }
                if (game.playerNumber == 2) {
                    break loop;
                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        start(this.getUserSocket());


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
