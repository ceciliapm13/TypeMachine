import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Game implements Runnable {

    //public static final String SPACE = new String(new char[15]).replace("\0", "\n");

    private Player[] players;
    private int playerNumber;
    private boolean roundEnd = true;
    private int answers = 0;
    private boolean roundWon = false;

    private GameServer gameServer;
    private Player player;
    private Prompt prompt;
    private StringInputScanner challenge; // "challenge" é a palavra do array naquele momento
    private String answer;
    private int challengeCounter;
    private int roundCounter;
    private PrintWriter out;
    private ExecutorService fixedPool;
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
        players = new Player[2];
        challengeCounter = 0;
        roundCounter = 1;
        fixedPool = Executors.newFixedThreadPool(2);
        this.challenge = new StringInputScanner();

    }

    //game init: boas-vindas ao jogo
    //public void init() {
    //esta a espera de um typing buddy


    //}


    //game start: início do jogo a partir do momento em que dois players estão conectados
    public void start(Socket playerSocket) {
        if (roundEnd) {
            System.out.println(Thread.currentThread().getName() + " inside other start method");
            nextChallenge(playerSocket);
            givePrompt(); // pedido da resposta do player
            roundEnd = false;
        }


    }

    /*public void start() {

        out.println("A worthy opponent has joined the Typing Contest. \n" +
                "GET READY TO RUMBLE!");

        while(expressionsArray.length < 10) {
            nextChallenge();
            awaitPlayerInput();
        }
        //endMessage()
    }*/


    public void nextChallenge(Socket playerSocket) {

            try {
                System.out.println(Thread.currentThread().getName() + " inside next challenge method");
                sendMessage(playerSocket, "New challenge \n");
                Thread.sleep(3000);
                sendMessage(playerSocket, "Get ready... \n");
                Thread.sleep(3000);
                sendMessage(playerSocket, "3\n");
                Thread.sleep(1000);
                sendMessage(playerSocket, "2\n");
                Thread.sleep(1000);
                sendMessage(playerSocket, "1\n");
                Thread.sleep(1000);
                sendMessage(playerSocket, "TYPE!\n");
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }



    }

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

        if (answers == 1) { //todo
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
                playerNumber++;
                return;
            }
        }
        System.out.println("Room is full");
    }

    public void endMessage() {
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null) {
                sendMessage(players[i].getUserSocket(), "Were you a TypeMachine? \n");
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
                    start(players[i].getUserSocket());
                }

                Thread.sleep(50);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
