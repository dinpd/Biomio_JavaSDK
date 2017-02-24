package biomio.sdk.internal.util;


import biomio.sdk.BiomioSDK;

/**
 * The type Logger.
 */
public class Logger {

    /**
     * The constant TAG.
     */
    public static final String TAG = "BiomioSDK";


    /**
     * D.
     *
     * @param msg the msg
     */
    public static void d(String msg) {
        if (BiomioSDK.debug) {
            System.out.println(TAG + " :: " + msg);
        }
    }

}
