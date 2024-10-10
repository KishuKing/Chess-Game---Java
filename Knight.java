public class Knight extends ChessPiece {
    public Knight(String color) {
        super(color);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, ChessPiece[][] board) {
        // Knight moves in "L" shapes
        int dx = Math.abs(startX - endX);
        int dy = Math.abs(startY - endY);
        return (dx == 2 && dy == 1) || (dx == 1 && dy == 2);
    }
}
