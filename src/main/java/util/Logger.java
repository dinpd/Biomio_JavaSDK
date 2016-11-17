package util;


import main.BiomioSDK;

public class Logger {

    public static final String TAG = "BiomioSDK";


    public static void d(String msg) {
        if (BiomioSDK.debug) {
            System.out.println(TAG + " :: " + msg);
        }
    }

}
