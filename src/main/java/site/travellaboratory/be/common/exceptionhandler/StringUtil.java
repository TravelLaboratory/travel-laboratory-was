package site.travellaboratory.be.common.exceptionhandler;

public class StringUtil {
    public static String camelToSnake(String str) {
        return str.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }
}
