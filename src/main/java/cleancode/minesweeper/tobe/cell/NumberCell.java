package cleancode.minesweeper.tobe.cell;

public class NumberCell implements Cell {

    private final int nearbyLandMineCount;
    private final CellState cellState = CellState.initialize();

    public NumberCell(int count) {
        nearbyLandMineCount = count;
    }

    @Override
    public boolean hasNearbyLandMineCount() {
        return true;

    }

    public CellSnapshot getSnapShot() {
        if(cellState.isChecked()){
            return CellSnapshot.ofNumber(nearbyLandMineCount);
        }
        if (cellState.isFlagged()) {
            return CellSnapshot.ofFlag();
        }
        return CellSnapshot.ofUnchecked();
    }


    @Override
    public CellSnapshot getSnapshot() {
        return null;
    }

    @Override
    public boolean isLandMine() {
        return false;
    }
    @Override
    public void flag() {
        cellState.flag();
    }

    @Override
    public void open() {
        cellState.open();
    }

    @Override
    public boolean isChecked() {
        return cellState.isChecked();
    }

    @Override
    public boolean isOpened() {
        return cellState.isOpened();
    }
}
