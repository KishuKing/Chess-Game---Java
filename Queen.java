public class Queen extends ChessPiece {
    public Queen(String color) {
        super(color);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, ChessPiece[][] board) {
        // Diagonal movement (Bishop's logic)
        if (Math.abs(startX - endX) == Math.abs(startY - endY)) {
            int stepX = (endX - startX) / Math.abs(endX - startX);
            int stepY = (endY - startY) / Math.abs(endY - startY);
            int x = startX + stepX;
            int y = startY + stepY;

            while (x != endX && y != endY) {
                if (board[x][y] != null) return false; // Blocked
                x += stepX;
                y += stepY;
            }
            return board[endX][endY] == null || !board[endX][endY].getColor().equals(color);
        }

        // Horizontal or vertical movement (Rook's logic)
        if (startX == endX || startY == endY) {
            int stepX = (startX < endX) ? 1 : (startX > endX) ? -1 : 0;
            int stepY = (startY < endY) ? 1 : (startY > endY) ? -1 : 0;
            int x = startX + stepX;
            int y = startY + stepY;

            while (x != endX || y != endY) {
                if (board[x][y] != null) return false; // Blocked
                x += stepX;
                y += stepY;
            }
            return board[endX][endY] == null || !board[endX][endY].getColor().equals(color);
        }

        return false;
    }
}
