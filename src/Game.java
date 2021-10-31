import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Game implements Runnable {

    public static final String SPACE = new String(new char[15]).replace("\0", "\n");

    private Player[] players;
    private int playerNumber;
private boolean roundEnd = true;
private int answers = 0;
private boolean roundOwn = false;

    private GameServer gameServer;
    private Player player;
    private Prompt prompt;
    private StringInputScanner challenge; // "challenge" é uma palavra do array naquele momento
    private String answer;
    private int challengeCounter;
    private int roundCounter;
    private PrintWriter out;
    ExecutorService fixedPool;
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
            "O Yanomami xifópago tergiversa sobre suas idiossincrasias"
    };


    public Game() throws IOException {
        players = new Player[2];
        //gameServer = new GameServer;
        challengeCounter = 0;
        roundCounter = 0;
        fixedPool = Executors.newFixedThreadPool(2);
        Prompt prompt = new Prompt(System.in, System.out); // TODO !!!!!
        this.challenge = new StringInputScanner();

    }

    //game init: boas-vindas ao jogo
    //public void init() {
    //esta a espera de um typing buddy


    //}


    //game start: início do jogo a partir do momento em que dois players estão conectados
    public void start() {
        if(roundEnd) {
            nextChallenge();
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


    public void nextChallenge() {
        for (int i = 0; i < players.length; i++) {
            if(players[i] != null) {
                try {
                    sendMessage(players[i].getUserSocket(), "New challenge \n"); //TODO ACABAR THREADS SLEEPS
                    Thread.sleep(1000);

                    /**+
                     "Get ready in: \n" +
                     "3 \n" +
                     "2 \n" +
                     "1 \n" +
                     "Go!");*/
                } catch (Exception e) {}
            }
        }

        //StringInputScanner challenge = new StringInputScanner();

        //challenge.setMessage(expressionsArray[challengeCounter]);
        //answer = prompt.getUserInput(challenge);

    }

    public void givePrompt() {
        for (int i = 0; i < players.length; i++) {
            if(players[i] != null) { // verificação de se o player existe
                players[i].setPrompted(expressionsArray[challengeCounter] + "\n"); // mostra do challenge e pede resposta(prompt)
            }
        }
    }


    public void playerInput(Player player, String msg) { // TODO synchronized
        String answer = msg;
        System.out.println(answer);
        System.out.println(expressionsArray[challengeCounter]);
        if (answer.equals(expressionsArray[challengeCounter])) {
            sendMessage(player.getUserSocket() ,player.getName() + " has won round " + roundCounter); // TODO
            player.setScore(player.getScore() + 1);
            answers++;
            return;
        } else {
            sendMessage(player.getUserSocket(), "You failed miserably. Try harder next round.");
        }

        if(answers == 2) { //todo
            roundEnd = true;
            roundCounter++;
            challengeCounter++;
        }

    }

    public void givePlayer(Socket player) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) {
                System.out.println("added player " + player.getInetAddress());
                players[i] = new Player(player,this);
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
                sendMessage(players[i].getUserSocket(), "Ah! There's no next round. \n" +
                        "Get ready in: \n" +
                        "3 \n" +
                        "2 \n" +
                        "1 \n" +
                        "Go!");
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
                start();
                Thread.sleep(50);
            } catch(Exception e) {}
        }
    }
}
