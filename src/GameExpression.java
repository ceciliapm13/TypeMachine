import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class GameExpression {


    private String[] expressionsArray = {
            "Alvíssaras",
            "Beneplácito",
            "Modorrento",
            "Numismática",
            "Vicissitude",
            "Zaragatoa",
            "Elocubrava ao repetenar-se",
            "O Yanomami xifópago tergiversa sobre suas idiossincrasias"
    } ;

     static LinkedList expressionsList = new LinkedList();



    public GameExpression() {
        expressionsList.addAll(Arrays.asList(expressionsArray));

    }

        public static void main(String[] args) {
            /*for (LinkedList expression : expressionsList) {
                System.out.println(expression);

                }
            }*/

        }


}
