package main;

import model.Options;
import state.StateMachine;
import util.Validator;

import javax.net.ssl.SSLContext;

public class BiomioSDK {

    private static final String EXCEPTION_URL_INVALID = "Invalid URL was passed to SDK";
    private static final String EXCEPTION_OPTIONS_NULL = "Provided options were null";
    private static final String EXCEPTION_SSL_CONTEXT_NULL = "SSLContext was null. Provide valid certificate first";
    private static final String EXCEPTION_ILLEGAL_INSTANCE = "Only one instance of SDK is allowed to be created";

    private static BiomioSDK instance = null;
    private StateMachine stateMachine;
    private static Options options;

    public static boolean debug = false;
    public static boolean connected = false;
    public static boolean retry = true;

    private boolean constructed = false;

    private BiomioSDK(SSLContext sslContext, String url, Options options) {
        if (constructed) {
            throw new IllegalArgumentException(EXCEPTION_ILLEGAL_INSTANCE);
        }
        if (!Validator.stringValid(url)) {
            throw new IllegalArgumentException(EXCEPTION_URL_INVALID);
        }
        if (sslContext == null) {
            throw new IllegalArgumentException(EXCEPTION_SSL_CONTEXT_NULL);
        }
        if (options == null) {
            throw new IllegalStateException(EXCEPTION_OPTIONS_NULL);
        }
        stateMachine = new StateMachine(sslContext, url);
        setOptions(options);
        debug(true);
        constructed = true;
    }

    /**
     * Initializes SDK by creating new instance of it.
     * Only one instance must be created during single session
     *
     * @param sslContext - Certificate
     * @param url        - Server URL
     * @param options    - {@link Options} - parameters required for SDK to work properly
     * @return - {@link BiomioSDK}
     */
    public static BiomioSDK initialize(SSLContext sslContext,
                                       String url,
                                       Options options) {
        if (instance == null) {
            instance = new BiomioSDK(sslContext, url, options);
        }
        return instance;
    }

    //*used for tests
    public static void destroy() {
        BiomioSDK.instance = null;
    }

    /**
     * Opens socket connection
     */
    public void connect() {
        stateMachine.connect();
    }

    /**
     * Closes socket connection
     */
    public void disconnect() {
        stateMachine.disconnect();
    }

    /**
     * Set to true to enable logs
     *
     * @param debug - boolean
     */
    public static void debug(boolean debug) {
        BiomioSDK.debug = debug;
    }

    /**
     * {@link Options} - required information to be passed to server by sdk from client
     * it is needed to provide them on the very start of application
     *
     * @param options - {@link Options}
     */
    public static void setOptions(Options options) {
        BiomioSDK.options = options;
    }

    public static Options getOptions() {
        return options;
    }

    public void subscribe(OnBiomioSdkListener listener) {
        this.stateMachine.setSdkListener(listener);
    }

    /**
     * Enables retry functionality if set to true.
     *
     * @param retry - boolean
     */
    public static void setRetry(boolean retry) {
        BiomioSDK.retry = retry;
    }
}
