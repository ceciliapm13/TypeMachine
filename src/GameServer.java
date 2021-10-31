import java.net.ServerSocket;

public class GameServer {
    ServerSocket serverSocket;
    Game game;
    Thread gameThread;

    public void init() {
        try {
            serverSocket = new ServerSocket(3333);
            game = new Game();
            gameThread = new Thread(game);
            gameThread.start();

            listen();
        } catch (Exception e) {}
    }

    private void listen() {
        while (true) {
            try {
                game.givePlayer(serverSocket.accept()); // servidor vai dar ao game o player
            } catch (Exception e) {
            }
        }
    }
}
