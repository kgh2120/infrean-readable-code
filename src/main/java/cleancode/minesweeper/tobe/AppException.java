package cleancode.minesweeper.tobe;

/**
 * 프로그램에서 만든 의도한 익셉션
 */
public class AppException extends RuntimeException{

    public AppException(String message) {
        super(message);
    }
}
