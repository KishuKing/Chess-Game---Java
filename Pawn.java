public class Pawn extends ChessPiece {
    private boolean firstMove = true;

    public Pawn(String color) {
        super(color);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, ChessPiece[][] board) {
        int direction = color.equals("White") ? 1 : -1; // White moves up, Black moves down

        // Forward move (without capturing)
        if (startY == endY && board[endX][endY] == null) {
            // Move forward by 1
            if (startX + direction == endX) {
                firstMove = false;
                return true;
            }
            // Move forward by 2 if it's the first move and the path is clear
            if (firstMove && startX + 2 * direction == endX && board[startX + direction][startY] == null) {
                firstMove = false;
                return true;
            }
        }

        // Diagonal capture
        if (Math.abs(startY - endY) == 1 && startX + direction == endX && board[endX][endY] != null) {
            if (!board[endX][endY].getColor().equals(color)) {
                firstMove = false;
                return true;
            }
        }

        return false;
    }
}
