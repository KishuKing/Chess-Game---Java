public class Bishop extends ChessPiece {
    public Bishop(String color) {
        super(color);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, ChessPiece[][] board) {
        if (Math.abs(startX - endX) == Math.abs(startY - endY)) {
            int stepX = (endX - startX) / Math.abs(endX - startX); // +1 or -1
            int stepY = (endY - startY) / Math.abs(endY - startY); // +1 or -1
            int x = startX + stepX;
            int y = startY + stepY;

            while (x != endX && y != endY) {
                if (board[x][y] != null) return false; // Blocked
                x += stepX;
                y += stepY;
            }
            return board[endX][endY] == null || !board[endX][endY].getColor().equals(color);
        }
        return false;
    }
}
