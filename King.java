public class King extends ChessPiece {
    public King(String color) {
        super(color);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, ChessPiece[][] board) {
        // A king can move one square in any direction
        if (Math.abs(startX - endX) <= 1 && Math.abs(startY - endY) <= 1) {
            // Check if the destination square is occupied by a piece of the same color
            if (board[endX][endY] == null || !board[endX][endY].getColor().equals(color)) {
                return true; // Valid move
            }
        }
        return false; // Invalid move
    }
}
