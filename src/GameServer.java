import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;

import javax.xml.bind.SchemaOutputResolver;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//CHAT SERVER
public class GameServer { //GameServer encarrega-se de estabelecer ligações e de fechá-las

    private int portNumber;
    private ServerSocket serverSocket;
    private Socket userSocket;
    private UserConnection userConnection;
    private LinkedList<UserConnection> users;
    private String ipDaMaquina;
    private int playerNumber = 0;
    private boolean twoPlayes;
    Prompt prompt;


    public GameServer() throws UnknownHostException {

        twoPlayes = false;

        prompt = new Prompt(System.in, System.out);
        portNumber = 3333;
        ipDaMaquina = InetAddress.getLocalHost().getHostAddress();
        System.out.println("ip: " + ipDaMaquina);

        //criação de uma socket de Servidor && criação de uma socket de cliente
        try {
            serverSocket = new ServerSocket(portNumber);
            users = new LinkedList<>();

            ExecutorService cachedPool = Executors.newCachedThreadPool();

            while (playerNumber < 2) {

                switch (playerNumber) {

                    case 0:
                    case 1:
                    case 2:
                        // o servidor fica à espera de uma socket de um novo user. Quando aceita (existe ligação), é atribuido à socket uma conexão do user
                        userSocket = serverSocket.accept();
                        userConnection = new UserConnection(userSocket);
                        // representações do novo utilizador no servidor. criada a ligação após a criação da socket

                        cachedPool.submit(userConnection);
                        users.add(userConnection);
                        break;
                    // representações do novo utilizador no servidor. criada a ligação após a criação da socket

                }

                playerNumber++;

            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }


    public void broadcast(String message) {
        for (UserConnection user : users) {

            try {
                PrintWriter out = new PrintWriter(new OutputStreamWriter(user.userSocket.getOutputStream()), true);
                out.println(message);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //MAIN SERVER
    public static void main(String[] args) throws UnknownHostException {
        new GameServer();
    }


    //USER CONNECTION (Esta classe Ñ é o cliente (user), é a ligação que o servidor estabelece com o cliente
    public class UserConnection implements Runnable {

        private Socket userSocket;
        private BufferedReader terminalReader;
        private String messageReceived;
        private String userName;
        private String namePlayer;


        public UserConnection(Socket userSocket) {
            this.userSocket = userSocket;

            //criação de um reader do terminal
            try {
                //leitura do input
                terminalReader = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }


        public void receive() {  //recepção de mensagem

            try {
                //leitura do input
                messageReceived = terminalReader.readLine();

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }

        public void send(String messageReceived) { //envio de mensagem

            broadcast(Thread.currentThread().getName() + " Array de palavras " + messageReceived);

        }

        //mensagem-título quando necessária
        public void sendTitleMessage(String titleMessage) {
            PrintWriter writer;

            try {
                writer = new PrintWriter(this.userSocket.getOutputStream(), true);
                writer.println(titleMessage);

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }

        public void serverFullMessage() {
            sendTitleMessage("\033[H\033[2J");
            sendTitleMessage("---------------------------\n");
            sendTitleMessage("     Server Is Full...    \n");
            sendTitleMessage("---------------------------\n");
        }

        public void changeThreadName() throws IOException { // não é um "setName", porque setter são para as propriedades da classe, não para algo específico da thread

            sendTitleMessage("What is your name? ");
            String username = terminalReader.readLine();

            this.userName = username;
           namePlayer = username;

            Thread.currentThread().setName(username);
            System.out.println(username + " Está no servidor");

        }
        public String getUserName(){
            return namePlayer;
        }

        @Override
        public void run() {

            sendTitleMessage("\033[H\033[2J");
            sendTitleMessage("---------------------------\n");
            sendTitleMessage("Welcome to TypingMachine!!!\n");
            sendTitleMessage("---------------------------\n");

            try {
                changeThreadName();
            } catch (IOException e) {
                e.printStackTrace();
            }

            sendTitleMessage("\033[H\033[2J");

            while (!userSocket.isClosed()) {

                sendTitleMessage("\033[H\033[2J");
                receive();
                if (messageReceived == null) {
                    break;
                }
               // send(messageReceived+": "+namePlayer);
                System.out.println(getUserName());
                //System.out.println(userName + " sms enviada -> " + messageReceived);
            }
            /*
            //novo método communicate:

            while (userSocket.isBound()) {

                if(line read contains "/quit") {

                } else {

                    send(messageReceived);
                }

            }
            */

        }

    }
}
