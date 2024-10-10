public abstract class ChessPiece {
    public String color;
    public int x, y; // Current position
    public int oldX, oldY; // Previous position

    public ChessPiece(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setPosition(int x, int y) {
        this.oldX = this.x; // Store old position
        this.oldY = this.y;
        this.x = x;
        this.y = y;
    }

    // Add the following methods
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getOldX() {
        return oldX;
    }

    public int getOldY() {
        return oldY;
    }

    public abstract boolean isValidMove(int startX, int startY, int endX, int endY, ChessPiece[][] board);
}
