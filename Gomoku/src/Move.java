import java.util.Objects;

public class Move {
    private int row, col;

    /**returns the row*/
    public int getRow() {
        return row;
    }
    /**sets the row*/
    public void setRow(int row) {
        this.row = row;
    }
    /**returns the col*/
    public int getCol() {
        return col;
    }
    /**sets the col*/
    public void setCol(int col) {
        this.col = col;
    }
    /**default construction*/
    public Move() {
        this.row = 0;
        this.col = 0;
    }
    /**complicated construction*/
    public Move(int row,int col) {
        this.row = row;
        this.col = col;
    }
    /**return string of the move*/
    @Override
    public String toString() {
        return "Move{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }
    /**if equal to another move*/
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return row == move.row &&
                col == move.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
