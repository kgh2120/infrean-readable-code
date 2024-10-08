package cleancode.minesweeper.tobe.cell;

public interface Cell {

    String FLAG_SIGN = "⚑";
    String UNCHECKED_SIGN = "□";

    boolean hasNearbyLandMineCount();

    String getSign();

    boolean isLandMine();

    void flag();

    void open();

    boolean isChecked();

    boolean isOpened();

}
