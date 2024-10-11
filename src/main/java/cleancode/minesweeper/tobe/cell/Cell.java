package cleancode.minesweeper.tobe.cell;

public interface Cell {



    boolean hasNearbyLandMineCount();

    CellSnapshot getSnapshot();

    boolean isLandMine();

    void flag();

    void open();

    boolean isChecked();

    boolean isOpened();

}
