
import cn.hutool.core.io.FileUtil;
import exceptions.NotFoundFileException;


/**
 * @author HJW
 * @date 2022-09-20 21:58
 * 代码所需要用到的所有工具类
 */
public class Utils {

    /**
     * 从文件中读取文本(为方便直接使用工具包)
     * @param filePath 文件路径
     * @return 文本
     */
    public static String readFileToStr(String filePath) throws NotFoundFileException {
        try {
            return FileUtil.readUtf8String(filePath);
        } catch (Exception e) {
            throw new NotFoundFileException("该绝对路径的文件不存在");
        }
    }
}
