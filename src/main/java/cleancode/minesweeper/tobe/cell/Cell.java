package cleancode.minesweeper.tobe.cell;

public abstract class Cell {

    protected static final String FLAG_SIGN = "⚑";
    protected static final String UNCHECKED_SIGN = "□";

    protected boolean isFlagged;
    protected boolean isOpened;

    public abstract boolean hasNearbyLandMineCount();

    public abstract String getSign();

    public abstract boolean isLandMine();

    public void flag() {
        this.isFlagged = true;
    }

    public void open() {
        this.isOpened = true;
    }

    public boolean isChecked() {
        return isOpened || isFlagged;
    }

    public boolean isOpened() {
        return this.isOpened;
    }

}
