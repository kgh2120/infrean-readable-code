package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.cell.Cell;
import cleancode.minesweeper.tobe.cell.EmptyCell;
import cleancode.minesweeper.tobe.cell.LandMineCell;
import cleancode.minesweeper.tobe.cell.NumberCell;
import cleancode.minesweeper.tobe.gamelevel.GameLevel;

import java.util.Arrays;
import java.util.Random;

public class GameBoard {

    private final Cell[][] board;
    private final int landMineCount;

    public GameBoard(GameLevel gameLevel) {
        board = new Cell[gameLevel.getRowSize()][gameLevel.getColSize()];
        landMineCount = gameLevel.getLandMineCount();
    }

    public void initializeGame() {
        int rowSize = getRowSize();
        int colSize = getColSize();

        for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
            for (int colIndex = 0; colIndex < colSize; colIndex++) {
                board[rowIndex][colIndex] = new EmptyCell();
            }
        }

        for (int i = 0; i < landMineCount; i++) {
            int landMineCol = new Random().nextInt(colSize);
            int landMineRow = new Random().nextInt(rowSize);
            LandMineCell landMineCell = new LandMineCell();
            board[landMineRow][landMineCol] = landMineCell;
        }

        for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
            for (int colIndex = 0; colIndex < colSize; colIndex++) {
                if (isLandMineCell(rowIndex, colIndex)) {
                    continue;
                }

                int count = countNearByLandMines(rowIndex, colIndex);
                if (count == 0) {
                    continue;
                }
                NumberCell numberCell = new NumberCell(count);
                board[rowIndex][colIndex] = numberCell;
            }
        }
    }

    public String getSign(int rowIndex, int colIndex) {
        Cell cell = findCell(rowIndex, colIndex);
        return cell.getSign();
    }

    private Cell findCell(int rowIndex, int colIndex) {
        return board[rowIndex][colIndex];
    }

    public boolean isAllCellChecked() {
        return Arrays.stream(board)
                .flatMap(Arrays::stream)
                .allMatch(Cell::isChecked);
    }

    public int getRowSize() {
        return board.length;
    }

    public int getColSize() {
        return board[0].length;
    }

    private int countNearByLandMines(int rowIndex, int colIndex) {
        int rowSize = getRowSize();
        int colSize = getColSize();
        int count = 0;
        if (rowIndex - 1 >= 0 && colIndex - 1 >= 0 && isLandMineCell(rowIndex - 1, colIndex - 1)) {
            count++;
        }
        if (rowIndex - 1 >= 0 && isLandMineCell(rowIndex - 1, colIndex)) {
            count++;
        }
        if (rowIndex - 1 >= 0 && colIndex + 1 < colSize && isLandMineCell(rowIndex - 1, colIndex + 1)) {
            count++;
        }
        if (colIndex - 1 >= 0 && isLandMineCell(rowIndex, colIndex - 1)) {
            count++;
        }
        if (colIndex + 1 < colSize && isLandMineCell(rowIndex, colIndex + 1)) {
            count++;
        }
        if (rowIndex + 1 < rowSize && colIndex - 1 >= 0 && isLandMineCell(rowIndex + 1, colIndex - 1)) {
            count++;
        }
        if (rowIndex + 1 < rowSize && isLandMineCell(rowIndex + 1, colIndex)) {
            count++;
        }
        if (rowIndex + 1 < rowSize && colIndex + 1 < colSize && isLandMineCell(rowIndex + 1, colIndex + 1)) {
            count++;
        }
        return count;
    }

    public boolean isLandMineCell(int selectedRowIndex, int selectedColIndex) {
        return findCell(selectedRowIndex, selectedColIndex).isLandMine();
    }


    public void open(int rowIndex, int colIndex) {
        Cell cell = findCell(rowIndex, colIndex);
        cell.open();
    }

    public void flag(int rowIndex, int colIndex) {
        Cell cell = findCell(rowIndex, colIndex);
        cell.flag();
    }

    public void openSurroundedCells(int row, int col) {

        int rowSize = getRowSize();
        int colSize = getColSize();

        if (row < 0 || row >= rowSize || col < 0 || col >= colSize) {
            return;
        }

        if (isOpenedCell(row, col)) {
            return;
        }
        if (isLandMineCell(row, col)) {
            return;
        }
        open(row,col);
        if (doesCellHaveLandMineCount(row, col)) {
            return;
        }
        openSurroundedCells(row - 1, col - 1);
        openSurroundedCells(row - 1, col);
        openSurroundedCells(row - 1, col + 1);
        openSurroundedCells(row, col - 1);
        openSurroundedCells(row, col + 1);
        openSurroundedCells(row + 1, col - 1);
        openSurroundedCells(row + 1, col);
        openSurroundedCells(row + 1, col + 1);
    }

    private boolean doesCellHaveLandMineCount(int row, int col) {
        return findCell(row, col).hasNearbyLandMineCount();
    }

    private boolean isOpenedCell(int row, int col) {
        return findCell(row, col).isOpened();
    }

}
