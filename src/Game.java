import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;


public class Game {

    public static final String SPACE = new String(new char[15]).replace("\0", "\n");

    private Player player;
    private Prompt prompt;
    private GameExpression expressions;
    private StringInputScanner challenge;
    private String answer;
    private int challengeCounter;
    public static final String[] expressionsArray = {
            "Alvíssaras" + SPACE,
            "Beneplácito" + SPACE,
            "Modorrento" + SPACE,
            "Numismática" + SPACE,
            "Vicissitude" + SPACE,
            "Zaragatoa" + SPACE,
            "Elocubrava ao repetenar-se" + SPACE,
            "O Yanomami xifópago tergiversa sobre suas idiossincrasias" + SPACE
    } ;
    private int countRound;
        private PrintWriter out;

    public Game() {
        player = new Player();
        challengeCounter = 0;
        Prompt prompt = new Prompt(System.in, System.out);
        this.challenge = new StringInputScanner();

    }

    //game init: boas-vindas ao jogo
    public void init() {


    }


    //game start: início do jogo a partir do momento em que dois players estão conectados
    public void start() {
        //metodo que introduz a 1ª ronda
        while(expressionsArray.length < 8) {
            nextChallenge();
            awaitPlayerInput();
        }
    }


    public void nextChallenge() {
        try (PrintWriter out = new PrintWriter(new OutputStreamWriter(player.getUserSocket().getOutputStream()), true)) {
            out.println("Next challenge \n" +
                            "Get ready \n" +
                    "3 \n" +
                    "2 \n" +
                    "1 \n");

            //StringInputScanner challenge = new StringInputScanner();
            challenge.setMessage(expressionsArray[challengeCounter]);
            answer = prompt.getUserInput(challenge);
            challengeCounter++;
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    public void awaitPlayerInput() {
        //Timer
        if(answer.equals(expressionsArray[challengeCounter])) {
            player.setScore(player.getScore() + 1);
        } else {

        }


        }

    public void endMessage() {

    }





    /*

    Timer timer = new Timer();

    public void timeExpired(int number) {

        for ( i = number; i <= number; i--){
            terminalwriter (i);
            timer.schedule(new ExpireTask(callbackClass), 1000);
          // este sout só se aplica
    }



    //---------------------- EXPIRED TASK --------------------------------------
    public class ExpireTask extends TimerTask {

        private Game game;

        ExpireTask(Game game) {
            this.game = game;
        }

        public void run() {
            game.timeExpired();
        }
    }
    */

}
