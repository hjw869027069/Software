package exceptions;

/**
 * @author HJW
 * @date 2022-09-21 12:57
 * 文件解析异常（转字符串为空或者过滤时没有可用词）
 */
public class FileAnalyseException extends Exception {
    public FileAnalyseException(String message) {
        super(message);
    }
}
