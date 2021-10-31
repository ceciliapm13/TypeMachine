import org.academiadecodigo.bootcamp.Prompt;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Game {

    public static final String SPACE = new String(new char[15]).replace("\0", "\n");

    private Player player;
    private Prompt prompt;
    private GameExpression expressions;
    private String[] expressionsArray = {
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
        countRound =1;
        Prompt prompt = new Prompt(System.in, System.out);

    }

    //game init: boas-vindas ao jogo
    public void init() {


    }


    //game start: início do jogo a partir do momento em que dois players estão conectados
    public void start() {
        while(expressionsArray.length < 8) {
            nextChallenge();
            awaitPlayerInput();
        }

    }



    //
    public void nextChallenge() {
        /*PrintWriter out = new PrintWriter(new OutputStreamWriter(user.userSocket.getOutputStream()),true);
        out.println(message)
        */


    }

    //
    public void awaitPlayerInput() {
        //Timer
        //if(playerInput.equals()arrayWord)

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
