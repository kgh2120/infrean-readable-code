package cleancode.minesweeper.tobe;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class MinesweeperGame {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final int BOARD_ROW_SIZE = 8;
    private static final int BOARD_COL_SIZE = 10;
    private static final Cell[][] BOARD = new Cell[BOARD_ROW_SIZE][BOARD_COL_SIZE];
     private static final int LAND_MINE_COUNT = 10;

    private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public static void main(String[] args) {
        showGameStartComment();
        initializeGame();
        while (true) {
            try {
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
            } catch (AppException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) { // 의도하지 않은 예외가 터지면 프로그램이 터지니까 이거도 잡는다.
                System.out.println("프로그램에 문제가 생겼습니다."); // 사용자에게 보여주는 예외 정보
                // e.printStackTrace(); // 개발자가 확인할 정보. 하지만 실무에서는 안티패턴. 예외 상황에선 로그 시스템으로 남긴다.
            }
        }

    }

    private static void actOnCell(String cellInput, String userActionInput) {
        int selectedColIndex = getSelectedColIndex(cellInput);
        int selectedRowIndex = getSelectedRowIndex(cellInput);

        if (doseUserChooseToPlantFlag(userActionInput)) {
            BOARD[selectedRowIndex][selectedColIndex].flag();
            checkIfGameIsOver();
            return;
        }

        if (doseUserChooseToOpenCell(userActionInput)) {
            if (isLandMineCell(selectedRowIndex, selectedColIndex)) {
                BOARD[selectedRowIndex][selectedColIndex].open();
                changeGameStatusToLose();
                return;
            }

            open(selectedRowIndex, selectedColIndex);
            checkIfGameIsOver();
            return;
        }
        throw new AppException("잘못된 번호를 선택하셨습니다.");
    }

    private static void changeGameStatusToLose() {
        gameStatus = -1;
    }

    private static boolean isLandMineCell(int selectedRowIndex, int selectedColIndex) {
        return BOARD[selectedRowIndex][selectedColIndex].isLandMine();
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
        boolean isAllChecked = isAllCellChecked();
        if (isAllChecked) {
            changeGameStatusToWin();
        }
    }

    private static boolean isAllCellChecked() {
        return Arrays.stream(BOARD)
                .flatMap(Arrays::stream)
                .allMatch(Cell::isChecked);
    }

    private static void changeGameStatusToWin() {
        gameStatus = 1;
    }

    private static int convertRowFrom(char cellInputRow) {
        int convertedRow = Character.getNumericValue(cellInputRow) - 1;
        if (convertedRow >= BOARD_ROW_SIZE)
            throw new AppException("잘못된 입력입니다. [1~8]까지의 입력만이 가능합니다.");
        return convertedRow;
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
                throw new AppException("잘못된 입력입니다. [a-j]까지의 입력만이 가능합니다.");
        }
    }

    private static void showBoard() {
        System.out.println("   a b c d e f g h i j");
        for (int rowIndex = 0; rowIndex < BOARD_ROW_SIZE; rowIndex++) {
            System.out.printf("%d  ", rowIndex + 1);
            for (int colIndex = 0; colIndex < BOARD_COL_SIZE; colIndex++) {
                System.out.print(BOARD[rowIndex][colIndex].getSign() + " "); // 보드를 그리는 행위는 MinesweeperGame이 가지고 있음.
                // Cell에게 그려줘~ 하는건 관심사 분리가 안되는 것. Cell은 데이터를 줘, 내가(Mine~)이 그려줄께 하는게 맞음.
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void initializeGame() {
        for (int rowIndex = 0; rowIndex < BOARD_ROW_SIZE; rowIndex++) {
            for (int colIndex = 0; colIndex < BOARD_COL_SIZE; colIndex++) {
                BOARD[rowIndex][colIndex] = Cell.create();
            }
        }

        for (int i = 0; i < LAND_MINE_COUNT; i++) {
            int col = new Random().nextInt(BOARD_COL_SIZE);
            int row = new Random().nextInt(BOARD_ROW_SIZE);
            BOARD[row][col].turnOnLandMine();
        }

        for (int rowIndex = 0; rowIndex < BOARD_ROW_SIZE; rowIndex++) {
            for (int colIndex = 0; colIndex < BOARD_COL_SIZE; colIndex++) {
                if (isLandMineCell(rowIndex, colIndex)) {
                    continue;
                }

                int count = countNearByLandMines(rowIndex, colIndex);
                BOARD[rowIndex][colIndex].updateNearbyLandMineCount(count);
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
        if (BOARD[row][col].isOpened()) {
            return;
        }
        if (isLandMineCell(row, col)) {
            return;
        }
        BOARD[row][col].open();
        if (BOARD[row][col].hasNearbyLandMineCount()) {
            return;
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
