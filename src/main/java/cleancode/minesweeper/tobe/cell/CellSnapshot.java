package cleancode.minesweeper.tobe.cell;

import java.util.Objects;

public class CellSnapshot {

    private final CellSnapshotStatus staus;
    private final int nearbyLandMineCount;

    private CellSnapshot(CellSnapshotStatus staus, int nearbyLandMineCount) {
        this.staus = staus;
        this.nearbyLandMineCount = nearbyLandMineCount;
    }

    public static CellSnapshot of(CellSnapshotStatus staus, int nearbyLandMineCount) {
        return new CellSnapshot(staus, nearbyLandMineCount);
    }

    public static CellSnapshot ofEmpty(){
        return of(CellSnapshotStatus.EMPTY, 0);
    }
    public static CellSnapshot ofLandMine(){
        return of(CellSnapshotStatus.LAND_MINE, 0);
    }
    public static CellSnapshot ofNumber(int nearbyLandMineCount){
        return of(CellSnapshotStatus.NUMBER, nearbyLandMineCount);
    }
    public static CellSnapshot ofFlag(){
        return of(CellSnapshotStatus.FLAG, 0);
    }
    public static CellSnapshot ofUnchecked(){
        return of(CellSnapshotStatus.UNCHECKED, 0);
    }

    public CellSnapshotStatus getStaus() {
        return staus;
    }

    public int getNearbyLandMineCount() {
        return nearbyLandMineCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellSnapshot snapshot = (CellSnapshot) o;
        return nearbyLandMineCount == snapshot.nearbyLandMineCount && staus == snapshot.staus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(staus, nearbyLandMineCount);
    }
}
