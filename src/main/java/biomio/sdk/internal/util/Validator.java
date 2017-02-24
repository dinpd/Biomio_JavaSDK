package biomio.sdk.internal.util;


/**
 * The type Validator.
 */
public class Validator {

    /**
     * Validates if String not null and not empty
     *
     * @param str - String to check
     * @return - true if valid
     */
    public static boolean stringValid(String str) {
        return str != null && str.length() > 0;
    }

    /**
     * Validates if String not null
     *
     * @param str - String to check
     * @return - true if not null
     */
    public static String stringNotNull(String str) {
        if (str != null) {
            return str;
        }
        return Constants.EMPTY_STRING;
    }
}
