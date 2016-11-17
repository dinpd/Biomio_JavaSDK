package util;


public class Validator {

    public static boolean stringValid(String str) {
        return str != null && str.length() > 0;
    }

    public static String stringNotNull(String str) {
        if (str != null) {
            return str;
        }
        return Constants.EMPTY_STRING;
    }
}
