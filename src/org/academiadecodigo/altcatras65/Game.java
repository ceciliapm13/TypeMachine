package org.academiadecodigo.altcatras65;

import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Game implements Runnable {

    //public static final String SPACE = new String(new char[15]).replace("\0", "\n");

    private Player[] players;
    public int playerNumber;
    private int answers = 0;
    private StringInputScanner challenge; // "challenge" é a palavra do array naquele momento
    private int challengeCounter;
    private int roundCounter;
    private PrintWriter out;
    private ExecutorService fixedPool;
    private boolean roundEnd;
    public boolean isFull;
    public static final String[] expressionsArray = {
            "Zaragatoa",
            "Modorrento",
            "Vicissitude",
            "Beneplácito",
            "Irrefragável",
            "Alvíssaras",
            "Numismática",
            "Insalutífero",
            "Elocubrava ao repetenar-se",
            "O Yanomami xifópago tergiversa sobre as suas idiossincrasias"
    };


    public Game() throws IOException {
        isFull = false;
        players = new Player[2];
        fixedPool = Executors.newFixedThreadPool(2);
        this.challenge = new StringInputScanner();
        roundEnd = false;

    }

    //game init: boas-vindas ao jogo
    //public void init() {
    //esta a espera de um typing buddy


    //}



    /*public void start() {

        out.println("A worthy opponent has joined the Typing Contest. \n" +
                "GET READY TO RUMBLE!");

        while(expressionsArray.length < 10) {
            nextChallenge();
            awaitPlayerInput();
        }
        //endMessage()
    }*/



    public void givePrompt() {
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null) { // verificação de se o player existe
                players[i].setPrompted(expressionsArray[challengeCounter] + "\n"); // mostra do challenge e pede resposta(prompt)
            }
        }
    }



    public void playerInput(Player player, String playerInput) {
        String answer = playerInput;
        System.out.println(answer);
        System.out.println(expressionsArray[challengeCounter]);
        if (answer.equals(expressionsArray[challengeCounter])) {
            sendMessage(player.getUserSocket(), player.getName() + " has won round " + roundCounter); // TODO
            player.setScore(player.getScore() + 1);
            answers++;
        } else {
            sendMessage(player.getUserSocket(), "You failed miserably. Try harder next round.");
        }

        if (answers == 1) {
            roundEnd = true;
            roundCounter++;
            challengeCounter++;
        }

    }

    public void givePlayer(Socket player) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) {
                System.out.println("added player " + player.getInetAddress());
                players[i] = new Player(player, this);
                fixedPool.submit(players[i]);

                return;
            }
        }
        System.out.println("Room is full");
    }

    public void endMessage() {
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null) {
                sendMessage(players[i].getUserSocket(), "org.academiadecodigo.altcatras65.Game over.\n" +
                        "Were you a TypeMachine? \n");
            }
        }
    }

    public void getMessage(Player client, String msg) {
        playerInput(client, msg);
    }

    public void sendMessage(Socket client, String message) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(client.getOutputStream(), true);
            writer.println(message);
        } catch (Exception e) {
        }
    }

    @Override
    public void run() {
        while (playerNumber != 2) {
            System.out.println("Waiting for players");
            for (int i = 0; i < players.length; i++) {
                if (players[i] != null) {
                    if (players[i].getName() != null) {
                        sendMessage(players[i].getUserSocket(), "Waiting for players!");
                    }
                }

            }

            try {
                Thread.sleep(1000); // verificação do 2º player
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (true) {

            //System.out.println("Players have joined");
            try {
                for (int i = 0; i < players.length; i++) { // o jogo não começa enquanto os players não tiverem nome
                    if (players[i] != null) {
                        if (players[i].getName() == null) {
                            i--;
                            Thread.sleep(50);
                        }
                    }
                }

                Thread.sleep(50);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public Player[] getPlayers() {
        return players;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }
}
