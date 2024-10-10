public class Rook extends ChessPiece {
    public Rook(String color) {
        super(color);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, ChessPiece[][] board) {
        if (startX == endX) {
            // Moving horizontally
            int step = (startY < endY) ? 1 : -1;
            for (int y = startY + step; y != endY; y += step) {
                if (board[startX][y] != null) return false; // Blocked
            }
            return board[endX][endY] == null || !board[endX][endY].getColor().equals(color);
        } else if (startY == endY) {
            // Moving vertically
            int step = (startX < endX) ? 1 : -1;
            for (int x = startX + step; x != endX; x += step) {
                if (board[x][startY] != null) return false; // Blocked
            }
            return board[endX][endY] == null || !board[endX][endY].getColor().equals(color);
        }
        return false;
    }
}
