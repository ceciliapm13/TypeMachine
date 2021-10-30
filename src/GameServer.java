import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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

    public GameServer() {

        portNumber = 3333;

        //criação de uma socket de Servidor && criação de uma socket de cliente
        try {
            serverSocket = new ServerSocket(portNumber);
            users = new LinkedList<>();

            ExecutorService cachedPool = Executors.newCachedThreadPool();

            while (true) {

                // o servidor fica à espera de uma socket de um novo user. Quando aceita (existe ligação), é atribuido à socket uma conexão do user
                userSocket = serverSocket.accept();
                userConnection = new UserConnection(userSocket);
                // representações do novo utilizador no servidor. criada a ligação após a criação da socket
                cachedPool.submit(userConnection);
                users.add(userConnection);

            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }


    public void broadcast(String message) {
        for (UserConnection user : users) {

            try {
                PrintWriter out = new PrintWriter(new OutputStreamWriter(user.userSocket.getOutputStream()),true);
                out.println(message);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //MAIN SERVER
    public static void main(String[] args) {
        new GameServer();
    }


    //USER CONNECTION (Esta classe Ñ é o cliente (user), é a ligação que o servidor estabelece com o cliente
    public class UserConnection implements Runnable {

        private Socket userSocket;
        private BufferedReader terminalReader;
        private String messageReceived;
        private PrintWriter terminalSender;


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

            broadcast(Thread.currentThread().getName() + ": " + messageReceived);

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

        public void changeThreadName() { // não é um "setName", porque setter são para as propriedades da classe, não para algo específico da thread

            try {
                sendTitleMessage("What's your name?");
                String userName = terminalReader.readLine();
                Thread.currentThread().setName(userName);

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }

        @Override
        public void run() {

            sendTitleMessage("Welcome to the You-Shouldn't-Be-Here-Chat!\n" + "We know you're a mortal. Enjoy this occult connection to the underworld.");
            changeThreadName();

            while(userSocket.isBound()) {
                receive();
                send(messageReceived);
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
