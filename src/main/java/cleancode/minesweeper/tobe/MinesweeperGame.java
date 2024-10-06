package cleancode.minesweeper.tobe;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class MinesweeperGame {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final int BOARD_ROW_SIZE = 8;
    private static final int BOARD_COL_SIZE = 10;
    private static final String[][] BOARD = new String[BOARD_ROW_SIZE][BOARD_COL_SIZE];
    private static final Integer[][] NEARBY_LAND_MINE_COUNTS = new Integer[BOARD_ROW_SIZE][BOARD_COL_SIZE];
    private static final boolean[][] LAND_MINES = new boolean[BOARD_ROW_SIZE][BOARD_COL_SIZE];
    private static final int LAND_MINE_COUNT = 10;
    private static final String FLAG_SIGN = "⚑";
    private static final String LAND_MINE_SIGN = "☼";
    private static final String CLOSE_CELL_SIGN = "□";
    private static final String OPENED_CELL_SIGN = "■";

    private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public static void main(String[] args) {
        showGameStartComment();
        initializeGame();

        while (true) {
            showBoard();

            if (doseUserClearTheGame()) {
                System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
                break;
            }
            if (doseUserFailTheGame()) {
                System.out.println("지뢰를 밟았습니다. GAME OVER!");
                break;
            }

            String cellInput = getCellInputFromUser();
            String userActionInput = getActionInputFromUser();
            actOnCell(cellInput, userActionInput);
        }
    }

    private static void actOnCell(String cellInput, String userActionInput) {
        int selectedColIndex = getSelectedColIndex(cellInput);
        int selectedRowIndex = getSelectedRowIndex(cellInput);

        if (doseUserChooseToPlantFlag(userActionInput)) {
            BOARD[selectedRowIndex][selectedColIndex] = FLAG_SIGN;
            checkIfGameIsOver();
            return;
        }

        if (doseUserChooseToOpenCell(userActionInput)) {
            if (isLandMineCell(selectedRowIndex, selectedColIndex)) {
                BOARD[selectedRowIndex][selectedColIndex] = LAND_MINE_SIGN;
                changeGameStatusToLose();
                return;
            }

            open(selectedRowIndex, selectedColIndex);
            checkIfGameIsOver();
            return;
        }

        System.out.println("잘못된 번호를 선택하셨습니다.");
    }

    private static void changeGameStatusToLose() {
        gameStatus = -1;
    }

    private static boolean isLandMineCell(int selectedRowIndex, int selectedColIndex) {
        return LAND_MINES[selectedRowIndex][selectedColIndex];
    }

    private static boolean doseUserChooseToOpenCell(String userActionInput) {
        return userActionInput.equals("1");
    }

    private static boolean doseUserChooseToPlantFlag(String userActionInput) {
        return userActionInput.equals("2");
    }

    private static int getSelectedRowIndex(String cellInput) {
        char cellInputRow = cellInput.charAt(1);
        return convertRowFrom(cellInputRow);
    }

    private static int getSelectedColIndex(String cellInput) {
        char cellInputCol = cellInput.charAt(0);
        return convertColFrom(cellInputCol);
    }

    private static String getActionInputFromUser() {
        System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
        return SCANNER.nextLine();
    }

    private static String getCellInputFromUser() {
        System.out.println("선택할 좌표를 입력하세요. (예: a1)");
        return SCANNER.nextLine();
    }

    private static boolean doseUserFailTheGame() {
        return gameStatus == -1;
    }

    private static boolean doseUserClearTheGame() {
        return gameStatus == 1;
    }

    private static void checkIfGameIsOver() {
        boolean isAllOpened = isAllCellOpened();
        if (isAllOpened) {
            changeGameStatusToWin();
        }
    }

    private static void changeGameStatusToWin() {
        gameStatus = 1;
    }

    private static boolean isAllCellOpened() {
        return Arrays.stream(BOARD)
                .flatMap(Arrays::stream)
                .noneMatch(cell -> cell.equals(CLOSE_CELL_SIGN));
    }

    private static int convertRowFrom(char cellInputRow) {
        return Character.getNumericValue(cellInputRow) - 1;
    }

    private static int convertColFrom(char cellInputCol) {
        switch (cellInputCol) {
            case 'a':
                return 0;
            case 'b':
                return 1;
            case 'c':
                return 2;
            case 'd':
                return 3;
            case 'e':
                return 4;
            case 'f':
                return 5;
            case 'g':
                return 6;
            case 'h':
                return 7;
            case 'i':
                return 8;
            case 'j':
                return 9;
            default:
                return -1;
        }
    }

    private static void showBoard() {
        System.out.println("   a b c d e f g h i j");
        for (int rowIndex = 0; rowIndex < BOARD_ROW_SIZE; rowIndex++) {
            System.out.printf("%d  ", rowIndex + 1);
            for (int colIndex = 0; colIndex < BOARD_COL_SIZE; colIndex++) {
                System.out.print(BOARD[rowIndex][colIndex] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void initializeGame() {
        for (int rowIndex = 0; rowIndex < BOARD_ROW_SIZE; rowIndex++) {
            for (int colIndex = 0; colIndex < BOARD_COL_SIZE; colIndex++) {
                BOARD[rowIndex][colIndex] = CLOSE_CELL_SIGN;
            }
        }

        for (int i = 0; i < LAND_MINE_COUNT; i++) {
            int col = new Random().nextInt(BOARD_COL_SIZE);
            int row = new Random().nextInt(BOARD_ROW_SIZE);
            LAND_MINES[row][col] = true;
        }

        for (int rowIndex = 0; rowIndex < BOARD_ROW_SIZE; rowIndex++) {
            for (int colIndex = 0; colIndex < BOARD_COL_SIZE; colIndex++) {
                if (isLandMineCell(rowIndex, colIndex)) {
                    NEARBY_LAND_MINE_COUNTS[rowIndex][colIndex] = 0;
                    continue;
                }

                int count = countNearByLandMines(rowIndex, colIndex);
                NEARBY_LAND_MINE_COUNTS[rowIndex][colIndex] = count;
            }
        }
    }

    private static int countNearByLandMines(int rowIndex, int colIndex) {
        int count = 0;
        if (rowIndex - 1 >= 0 && colIndex - 1 >= 0 && isLandMineCell(rowIndex - 1, colIndex - 1)) {
            count++;
        }
        if (rowIndex - 1 >= 0 && isLandMineCell(rowIndex - 1, colIndex)) {
            count++;
        }
        if (rowIndex - 1 >= 0 && colIndex + 1 < BOARD_COL_SIZE && isLandMineCell(rowIndex - 1, colIndex + 1)) {
            count++;
        }
        if (colIndex - 1 >= 0 && isLandMineCell(rowIndex, colIndex - 1)) {
            count++;
        }
        if (colIndex + 1 < BOARD_COL_SIZE && isLandMineCell(rowIndex, colIndex + 1)) {
            count++;
        }
        if (rowIndex + 1 < BOARD_ROW_SIZE && colIndex - 1 >= 0 && isLandMineCell(rowIndex + 1, colIndex - 1)) {
            count++;
        }
        if (rowIndex + 1 < BOARD_ROW_SIZE && isLandMineCell(rowIndex + 1, colIndex)) {
            count++;
        }
        if (rowIndex + 1 < BOARD_ROW_SIZE && colIndex + 1 < BOARD_COL_SIZE && isLandMineCell(rowIndex + 1, colIndex + 1)) {
            count++;
        }
        return count;
    }

    private static void showGameStartComment() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    private static void open(int row, int col) {
        if (row < 0 || row >= BOARD_ROW_SIZE || col < 0 || col >= BOARD_COL_SIZE) {
            return;
        }
        if (!BOARD[row][col].equals(CLOSE_CELL_SIGN)) {
            return;
        }
        if (isLandMineCell(row, col)) {
            return;
        }
        if (NEARBY_LAND_MINE_COUNTS[row][col] != 0) {
            BOARD[row][col] = String.valueOf(NEARBY_LAND_MINE_COUNTS[row][col]);
            return;
        } else {
            BOARD[row][col] = OPENED_CELL_SIGN;
        }
        open(row - 1, col - 1);
        open(row - 1, col);
        open(row - 1, col + 1);
        open(row, col - 1);
        open(row, col + 1);
        open(row + 1, col - 1);
        open(row + 1, col);
        open(row + 1, col + 1);
    }

}
