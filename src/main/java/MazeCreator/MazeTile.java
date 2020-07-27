package MazeCreator;

import java.util.Objects;

public class MazeTile {

    private int i;
    private int j;
    private String tileElement;

    public MazeTile(int i, int j) {
        this.i = i;
        this.j = j;
        this.tileElement = "H";
    }

    public void setTileElement(String tileElement) {
        this.tileElement = tileElement;
    }

    public String getTileElement() {
        return tileElement;
    }


    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public boolean isGoalTile() {
        return tileElement.equals("C");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MazeTile mazeTile = (MazeTile) o;
        return i == mazeTile.i && j == mazeTile.j;
    }

    @Override
    public int hashCode() {
        return Objects.hash(i, j, tileElement);
    }

    @Override
    public String toString() {
        return "MazeTile{" +
                "i=" + i +
                ", j=" + j +
                ", tileElement='" + tileElement + '\'' +
                '}';
    }

    public boolean isPlayer() {
        return tileElement.equals("1");
    }
}
