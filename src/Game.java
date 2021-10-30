import org.academiadecodigo.bootcamp.Prompt;

import java.util.Timer;
import java.util.TimerTask;

public class Game {

    private Player player;
    private Prompt prompt;
    private Timer timer;

    public Game() {
        Prompt prompt = new Prompt(System.in, System.out);
        Timer timer = new Timer();
    }

    //game init: boas-vindas ao jogo
    public void init() {
        //Timer

    }


    //game start: início do jogo a partir do momento em que dois players estão conectados
    public void start() {
        /*while(lista de expressões não tiver acabado) {
            nextChallenge();
            awaitPlayerInput();
        }
        */
    }



    //
    public void nextChallenge() {
        //Timer

    }

    //
    public void awaitPlayerInput() {
        //Timer
        //if(timer = !isOver && playerInput.equals()arrayWord)

    }

    public void endMessage() {

    }

    public void timeExpired(/*int number*/) {
        /*
        for ( i = number; i <= number; i--){
            terminalwriter (i);
            timer.schedule(new ExpireTask(callbackClass), 1000);
         */ // este sout só se aplica
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

}
