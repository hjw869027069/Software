package exceptions;

import java.security.NoSuchAlgorithmException;

/**
 * @author HJW
 * @date 2022-09-21 12:57
 * hash异常 md5
 */
public class HashException extends NoSuchAlgorithmException {
    public HashException(String message) {
        super(message);
    }
}
