package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.game.GameInitializable;
import cleancode.minesweeper.tobe.game.GameRunnable;
import cleancode.minesweeper.tobe.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.io.InputHandler;
import cleancode.minesweeper.tobe.io.OutputHandler;

public class Minesweeper implements GameRunnable, GameInitializable {

    private final GameBoard gameBoard;
    private final BoardIndexConverter boardIndexConverter = new BoardIndexConverter();
    private final InputHandler inputHandler;
    private final OutputHandler outputHandler;

    private int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public Minesweeper(GameLevel gameLevel, InputHandler inputHandler, OutputHandler outputHandler) {
        gameBoard = new GameBoard(gameLevel);
        this.inputHandler = inputHandler;
        this.outputHandler = outputHandler;
    }

    @Override
    public void initialize() {
        gameBoard.initializeGame();
    }

    public void run() {
        outputHandler.showGameStartComment();
//        gameBoard.initializeGame();
        while (true) {
            try {
                outputHandler.showBoard(gameBoard);
                if (doseUserClearTheGame()) {
                    outputHandler.showGameWinningComment();
                    break;
                }
                if (doseUserFailTheGame()) {
                    outputHandler.showGameLosingComment();
                    break;
                }

                String cellInput = getCellInputFromUser();
                String userActionInput = getActionInputFromUser();
                actOnCell(cellInput, userActionInput);
            } catch (GameException e) {
                outputHandler.showExceptionMessage(e);
            } catch (Exception e) { // 의도하지 않은 예외가 터지면 프로그램이 터지니까 이거도 잡는다.
                outputHandler.showSimpleMessage("프로그램에 문제가 생겼습니다.");// 사용자에게 보여주는 예외 정보
                // e.printStackTrace(); // 개발자가 확인할 정보. 하지만 실무에서는 안티패턴. 예외 상황에선 로그 시스템으로 남긴다.
            }
        }
    }


    private void actOnCell(String cellInput, String userActionInput) {
        int selectedColIndex = boardIndexConverter.getSelectedColIndex(cellInput, gameBoard.getColSize());
        int selectedRowIndex = boardIndexConverter.getSelectedRowIndex(cellInput, gameBoard.getRowSize());

        if (doseUserChooseToPlantFlag(userActionInput)) {
            gameBoard.flag(selectedRowIndex, selectedColIndex);
            checkIfGameIsOver();
            return;
        }

        if (doseUserChooseToOpenCell(userActionInput)) {
            if (gameBoard.isLandMineCell(selectedRowIndex, selectedColIndex)) {
                gameBoard.open(selectedRowIndex, selectedColIndex);
                changeGameStatusToLose();
                return;
            }

            gameBoard.openSurroundedCells(selectedRowIndex, selectedColIndex);
            checkIfGameIsOver();
            return;
        }
        throw new GameException("잘못된 번호를 선택하셨습니다.");
    }

    private void changeGameStatusToLose() {
        gameStatus = -1;
    }


    private boolean doseUserChooseToOpenCell(String userActionInput) {
        return userActionInput.equals("1");
    }

    private boolean doseUserChooseToPlantFlag(String userActionInput) {
        return userActionInput.equals("2");
    }

    private String getActionInputFromUser() {
        outputHandler.showCommentForUserAction();
        return inputHandler.getUserInput();
    }

    private String getCellInputFromUser() {
        outputHandler.showCommentForSelectingCell();
        return inputHandler.getUserInput();
    }

    private boolean doseUserFailTheGame() {
        return gameStatus == -1;
    }

    private boolean doseUserClearTheGame() {
        return gameStatus == 1;
    }

    private void checkIfGameIsOver() {
        if (gameBoard.isAllCellChecked()) {
            changeGameStatusToWin();
        }
    }


    private void changeGameStatusToWin() {
        gameStatus = 1;
    }


}
