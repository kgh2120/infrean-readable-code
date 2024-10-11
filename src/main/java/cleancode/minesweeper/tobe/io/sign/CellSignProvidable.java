package cleancode.minesweeper.tobe.io.sign;

import cleancode.minesweeper.tobe.cell.CellSnapshot;

import java.util.List;

public interface CellSignProvidable {




    String provide(CellSnapshot cellSnapshot);

    boolean supports(CellSnapshot cellSnapshot);
}
