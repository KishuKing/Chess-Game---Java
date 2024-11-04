import java.io.*;
import java.net.*;

public class ChessGameServer implements Runnable {
    private int PORT;

    // Constructor to set the PORT
    public ChessGameServer(int portInput) {
        this.PORT = portInput > 0 ? portInput : 12345; // Set to default if invalid port is provided
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Chess server started on port " + PORT + ". Waiting for players...");

            // Accept two player connections
            Socket player1 = serverSocket.accept();
            System.out.println("Player 1 connected.");
            Socket player2 = serverSocket.accept();
            System.out.println("Player 2 connected.");

            // Start threads to handle each player's communication
            new Thread(new PlayerHandler(player1, player2)).start();
            new Thread(new PlayerHandler(player2, player1)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class PlayerHandler implements Runnable {
    private Socket playerSocket;
    private Socket opponentSocket;

    public PlayerHandler(Socket playerSocket, Socket opponentSocket) {
        this.playerSocket = playerSocket;
        this.opponentSocket = opponentSocket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
             PrintWriter out = new PrintWriter(opponentSocket.getOutputStream(), true)) {

            String move;
            while ((move = in.readLine()) != null) {
                System.out.println("Move received: " + move);
                out.println(move); // Forward the move to the opponent
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
