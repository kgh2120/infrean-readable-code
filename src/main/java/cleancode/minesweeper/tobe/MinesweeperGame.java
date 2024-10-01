package cleancode.minesweeper.tobe;

import java.util.Random;
import java.util.Scanner;

public class MinesweeperGame {

    private static String[][] board = new String[8][10];
    private static Integer[][] landMineCounts = new Integer[8][10];
    private static boolean[][] landMines = new boolean[8][10];
    private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public static void main(String[] args) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        Scanner scanner = new Scanner(System.in);
        for (int rowIndex = 0; rowIndex < 8; rowIndex++) {
            for (int colIndex = 0; colIndex < 10; colIndex++) {
                board[rowIndex][colIndex] = "□";
            }
        }
        for (int i = 0; i < 10; i++) {
            int col = new Random().nextInt(10);
            int row = new Random().nextInt(8);
            landMines[row][col] = true;
        }
        for (int rowIndex = 0; rowIndex < 8; rowIndex++) {
            for (int colIndex = 0; colIndex < 10; colIndex++) {
                int count = 0;
                if (!landMines[rowIndex][colIndex]) {
                    if (rowIndex - 1 >= 0 && colIndex - 1 >= 0 && landMines[rowIndex - 1][colIndex - 1]) {
                        count++;
                    }
                    if (rowIndex - 1 >= 0 && landMines[rowIndex - 1][colIndex]) {
                        count++;
                    }
                    if (rowIndex - 1 >= 0 && colIndex + 1 < 10 && landMines[rowIndex - 1][colIndex + 1]) {
                        count++;
                    }
                    if (colIndex - 1 >= 0 && landMines[rowIndex][colIndex - 1]) {
                        count++;
                    }
                    if (colIndex + 1 < 10 && landMines[rowIndex][colIndex + 1]) {
                        count++;
                    }
                    if (rowIndex + 1 < 8 && colIndex - 1 >= 0 && landMines[rowIndex + 1][colIndex - 1]) {
                        count++;
                    }
                    if (rowIndex + 1 < 8 && landMines[rowIndex + 1][colIndex]) {
                        count++;
                    }
                    if (rowIndex + 1 < 8 && colIndex + 1 < 10 && landMines[rowIndex + 1][colIndex + 1]) {
                        count++;
                    }
                    landMineCounts[rowIndex][colIndex] = count;
                    continue;
                }
                landMineCounts[rowIndex][colIndex] = 0;
            }
        }
        while (true) {
            System.out.println("   a b c d e f g h i j");
            for (int rowIndex = 0; rowIndex < 8; rowIndex++) {
                System.out.printf("%d  ", rowIndex + 1);
                for (int colIndex = 0; colIndex < 10; colIndex++) {
                    System.out.print(board[rowIndex][colIndex] + " ");
                }
                System.out.println();
            }
            if (gameStatus == 1) {
                System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
                break;
            }
            if (gameStatus == -1) {
                System.out.println("지뢰를 밟았습니다. GAME OVER!");
                break;
            }
            System.out.println();
            System.out.println("선택할 좌표를 입력하세요. (예: a1)");
            String cellInput = scanner.nextLine();
            System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
            String userActionInput = scanner.nextLine();
            char cellInputCol = cellInput.charAt(0);
            char cellInputRow = cellInput.charAt(1);
            int selectedColIndex;
            switch (cellInputCol) {
                case 'a':
                    selectedColIndex = 0;
                    break;
                case 'b':
                    selectedColIndex = 1;
                    break;
                case 'c':
                    selectedColIndex = 2;
                    break;
                case 'd':
                    selectedColIndex = 3;
                    break;
                case 'e':
                    selectedColIndex = 4;
                    break;
                case 'f':
                    selectedColIndex = 5;
                    break;
                case 'g':
                    selectedColIndex = 6;
                    break;
                case 'h':
                    selectedColIndex = 7;
                    break;
                case 'i':
                    selectedColIndex = 8;
                    break;
                case 'j':
                    selectedColIndex = 9;
                    break;
                default:
                    selectedColIndex = -1;
                    break;
            }
            int selectedRowIndex = Character.getNumericValue(cellInputRow) - 1;
            if (userActionInput.equals("2")) {
                board[selectedRowIndex][selectedColIndex] = "⚑";
                boolean isAllOpend = true;
                for (int rowIndex = 0; rowIndex < 8; rowIndex++) {
                    for (int colIndex = 0; colIndex < 10; colIndex++) {
                        if (board[rowIndex][colIndex].equals("□")) {
                            isAllOpend = false;
                        }
                    }
                }
                if (isAllOpend) {
                    gameStatus = 1;
                }
            } else if (userActionInput.equals("1")) {
                if (landMines[selectedRowIndex][selectedColIndex]) {
                    board[selectedRowIndex][selectedColIndex] = "☼";
                    gameStatus = -1;
                    continue;
                } else {
                    open(selectedRowIndex, selectedColIndex);
                }
                boolean isAllOpend = true;
                for (int rowIndex = 0; rowIndex < 8; rowIndex++) {
                    for (int colIndex = 0; colIndex < 10; colIndex++) {
                        if (board[rowIndex][colIndex].equals("□")) {
                            isAllOpend = false;
                        }
                    }
                }
                if (isAllOpend) {
                    gameStatus = 1;
                }
            } else {
                System.out.println("잘못된 번호를 선택하셨습니다.");
            }
        }
    }

    private static void open(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 10) {
            return;
        }
        if (!board[row][col].equals("□")) {
            return;
        }
        if (landMines[row][col]) {
            return;
        }
        if (landMineCounts[row][col] != 0) {
            board[row][col] = String.valueOf(landMineCounts[row][col]);
            return;
        } else {
            board[row][col] = "■";
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
