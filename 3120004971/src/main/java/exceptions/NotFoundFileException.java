package exceptions;

import java.io.FileNotFoundException;

/**
 * @author HJW
 * 找不到文件的自定义异常
 */
public class NotFoundFileException extends FileNotFoundException {
    public NotFoundFileException(String message) {
        super(message);
    }
}
