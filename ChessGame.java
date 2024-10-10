import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class ChessGame extends Frame {
    private final int BOARD_SIZE = 8;
    private Button[][] board = new Button[BOARD_SIZE][BOARD_SIZE];
    private ChessPiece[][] pieces = new ChessPiece[BOARD_SIZE][BOARD_SIZE]; // ChessPiece array to track piece positions
    private String currentPlayer = "White";
    private ChessPiece selectedPiece = null;
    private int selectedRow = -1, selectedCol = -1;

    private Socket socket; // Socket for network communication
    private PrintWriter out; // Output stream for sending moves
    private BufferedReader in; // Input stream for receiving moves

    public ChessGame(String serverAddress, int port) {
        setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        initializeBoard();
        initializePieces();
        setSize(640, 640);
        setVisible(true);

        // Connect to the server
        connectToServer(serverAddress, port);

        // Start a thread to listen for moves from the opponent
        new Thread(this::listenForOpponentMove).start();
    }

    // Initialize the GUI Board
    private void initializeBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                board[row][col] = new Button();
                board[row][col].setActionCommand(row + "," + col);
                board[row][col].addActionListener(e -> handleMove(e));

                if ((row + col) % 2 == 0) {
                    board[row][col].setBackground(Color.WHITE);
                } else {
                    board[row][col].setBackground(Color.GRAY);
                }
                add(board[row][col]);
            }
        }
    }

    // Initialize the pieces on the board
    private void initializePieces() {
        // Initialize White pieces
        pieces[0][0] = new Rook("White");
        pieces[0][1] = new Knight("White");
        pieces[0][2] = new Bishop("White");
        pieces[0][3] = new Queen("White");
        pieces[0][4] = new King("White");
        pieces[0][5] = new Bishop("White");
        pieces[0][6] = new Knight("White");
        pieces[0][7] = new Rook("White");
        for (int i = 0; i < 8; i++) {
            pieces[1][i] = new Pawn("White");
        }

        // Initialize Black pieces
        pieces[7][0] = new Rook("Black");
        pieces[7][1] = new Knight("Black");
        pieces[7][2] = new Bishop("Black");
        pieces[7][3] = new Queen("Black");
        pieces[7][4] = new King("Black");
        pieces[7][5] = new Bishop("Black");
        pieces[7][6] = new Knight("Black");
        pieces[7][7] = new Rook("Black");
        for (int i = 0; i < 8; i++) {
            pieces[6][i] = new Pawn("Black");
        }

        // Display initial piece positions in the buttons
        updateBoardUI();
    }

    // Updates the button text to reflect piece positions
    private void updateBoardUI() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (pieces[row][col] != null) {
                    board[row][col].setLabel(pieces[row][col].getClass().getSimpleName());
                } else {
                    board[row][col].setLabel("");
                }
            }
        }
    }

    // Handles the move logic when a button is clicked
    private void handleMove(ActionEvent e) {
        String command = e.getActionCommand();
        int row = Integer.parseInt(command.split(",")[0]);
        int col = Integer.parseInt(command.split(",")[1]);

        if (selectedPiece == null) {
            // Select piece if it's the current player's turn and valid piece
            if (pieces[row][col] != null && pieces[row][col].getColor().equals(currentPlayer)) {
                selectedPiece = pieces[row][col];
                selectedRow = row;
                selectedCol = col;
            }
        } else {
            // Try to move the selected piece
            if (selectedPiece.isValidMove(selectedRow, selectedCol, row, col, pieces)) {
                // Move the piece
                pieces[row][col] = selectedPiece;
                pieces[selectedRow][selectedCol] = null;
                updateBoardUI();

                // Send the move to the opponent
                sendMove(selectedRow, selectedCol, row, col);

                // Switch player turn
                currentPlayer = currentPlayer.equals("White") ? "Black" : "White";
            }

            // Reset selection
            selectedPiece = null;
            selectedRow = -1;
            selectedCol = -1;
        }
    }

    // Connect to the chess server
    private void connectToServer(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Send the move to the opponent
    private void sendMove(int fromRow, int fromCol, int toRow, int toCol) {
        String move = fromRow + "," + fromCol + "," + toRow + "," + toCol;
        out.println(move);
    }

    // Listen for moves from the opponent
    private void listenForOpponentMove() {
        try {
            String move;
            while ((move = in.readLine()) != null) {
                String[] parts = move.split(",");
                int fromRow = Integer.parseInt(parts[0]);
                int fromCol = Integer.parseInt(parts[1]);
                int toRow = Integer.parseInt(parts[2]);
                int toCol = Integer.parseInt(parts[3]);

                // Apply the move
                ChessPiece piece = pieces[fromRow][fromCol];
                pieces[toRow][toCol] = piece;
                pieces[fromRow][fromCol] = null;
                updateBoardUI();

                // Switch player turn
                currentPlayer = currentPlayer.equals("White") ? "Black" : "White";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ChessGame("localhost", 12345); // Replace "localhost" with the server IP if needed
    }
}
