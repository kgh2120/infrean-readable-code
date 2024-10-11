package cleancode.minesweeper.tobe.io;

import cleancode.minesweeper.tobe.GameBoard;
import cleancode.minesweeper.tobe.GameException;
import cleancode.minesweeper.tobe.cell.CellSnapshot;
import cleancode.minesweeper.tobe.cell.CellSnapshotStatus;
import cleancode.minesweeper.tobe.position.CellPosition;

import java.util.List;
import java.util.stream.IntStream;

public class ConsoleOutputHandler implements OutputHandler {

    private static final String EMPTY_SIGN = "■";
    private static final String LAND_MINE_SIGN = "☼";
    private static final String FLAG_SIGN = "⚑";
    private static final String UNCHECKED_SIGN = "□";

    @Override
    public void showGameStartComment() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    @Override
    public void showBoard(GameBoard board) {

        String alphabets = generateColAlphabets(board);

        System.out.println("    " + alphabets);
        for (int rowIndex = 0; rowIndex < board.getRowSize(); rowIndex++) {
            System.out.printf("%2d  ", rowIndex + 1);
            for (int colIndex = 0; colIndex < board.getColSize(); colIndex++) {
                CellPosition cellPosition = CellPosition.of(rowIndex, colIndex);
                CellSnapshot snapshot = board.getSnapshot(cellPosition);

                String cellSign = decideCellSignFrom(snapshot);



                System.out.print(cellSign + " "); // 보드를 그리는 행위는 MinesweeperGame이 가지고 있음.
                // Cell에게 그려줘~ 하는건 관심사 분리가 안되는 것. Cell은 데이터를 줘, 내가(Mine~)이 그려줄께 하는게 맞음.
            }
            System.out.println();
        }
        System.out.println();
    }

    private String decideCellSignFrom(CellSnapshot snapshot) {
        CellSnapshotStatus staus = snapshot.getStaus();
        if(staus == CellSnapshotStatus.EMPTY) {
            return EMPTY_SIGN;
        }
        if(staus == CellSnapshotStatus.FLAG) {
            return FLAG_SIGN;
        }
        if(staus == CellSnapshotStatus.UNCHECKED) {
            return UNCHECKED_SIGN;
        }
        if (staus == CellSnapshotStatus.LAND_MINE) {
            return LAND_MINE_SIGN;
        }
        if(staus == CellSnapshotStatus.NUMBER){
            return String.valueOf(snapshot.getNearbyLandMineCount());
        }

        throw new IllegalArgumentException("확인할 수 없는 셀입니다.");
    }

    private String generateColAlphabets(GameBoard board) {
        List<String> alphabets = IntStream.range(0, board.getColSize())
                .mapToObj(index -> (char) ('a' + index))
                .map(Object::toString)
                .toList();
        return String.join(" ", alphabets);
    }

    @Override
    public void showGameWinningComment() {
        System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
    }

    @Override
    public void showGameLosingComment() {
        System.out.println("지뢰를 밟았습니다. GAME OVER!");
    }

    @Override
    public void showCommentForSelectingCell() {
        System.out.println("선택할 좌표를 입력하세요. (예: a1)");
    }

    @Override
    public void showCommentForUserAction() {
        System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
    }

    @Override
    public void showExceptionMessage(GameException e) {
        System.out.println(e.getMessage());
    }

    @Override
    public void showSimpleMessage(String message) {
        System.out.println(message);
    }
}
