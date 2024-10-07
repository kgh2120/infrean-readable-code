package cleancode.minesweeper.tobe.cell;

public class NumberCell extends Cell {

    private final int nearbyLandMineCount;

    public NumberCell(int count) {
        nearbyLandMineCount = count;
    }

    @Override
    public boolean hasNearbyLandMineCount() {
        return true;
    }

    @Override
    public String getSign() {
        if(isOpened){
            return String.valueOf(nearbyLandMineCount);
        }
        if (isFlagged) {
            return FLAG_SIGN;
        }

        return UNCHECKED_SIGN;
    }

    @Override
    public boolean isLandMine() {
        return false;
    }
}
