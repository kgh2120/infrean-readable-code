package cleancode.minesweeper.tobe;

/**
 * 프로그램에서 만든 의도한 익셉션
 */
public class GameException extends RuntimeException{

    public GameException(String message) {
        super(message);
    }
}
