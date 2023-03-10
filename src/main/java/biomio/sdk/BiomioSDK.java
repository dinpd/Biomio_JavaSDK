package biomio.sdk;

import biomio.sdk.internal.Options;
import biomio.sdk.internal.state.StateMachine;
import biomio.sdk.internal.util.Validator;

import javax.net.ssl.SSLContext;

/**
 * The type Biomio sdk.
 */
//TODO Sign header inside SDK
public class BiomioSDK {

    private static final String EXCEPTION_URL_INVALID = "Invalid URL was passed to SDK";
    private static final String EXCEPTION_OPTIONS_NULL = "Provided options were null";
    private static final String EXCEPTION_SSL_CONTEXT_NULL = "SSLContext was null. Provide valid certificate first";
    private static final String EXCEPTION_ILLEGAL_INSTANCE = "Only one instance of SDK is allowed to be created";

    private static BiomioSDK instance = null;
    private static Options options;

    private StateMachine stateMachine;
    private OnBiomioSdkListener listener;

    /**
     * The constant debug.
     */
    public static boolean debug = false;
    /**
     * The constant connected.
     */
    public static boolean connected = false;
    /**
     * The constant retry.
     */
    public static boolean retry = true;

    private boolean constructed = false;

    private BiomioSDK(SSLContext sslContext, String url, Options options) {
		debug = true;
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

    /**
     * Destroy.
     */
//*used for tests
    public static void destroy() {
        BiomioSDK.instance = null;
    }

    /**
     * Opens socket connection
     */
    public void connect() {
        if(listener == null){
            throw new IllegalStateException("Listener is not registered. Call subscribe() in order to receive SDK events");
        }
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

    /**
     * Gets options.
     *
     * @return the options
     */
    public static Options getOptions() {
        return options;
    }

    /**
     * Subscribe.
     *
     * @param listener the listener
     */
    public void subscribe(OnBiomioSdkListener listener) {
        this.listener = listener;
        this.stateMachine.setSdkListener(this.listener);
    }

    /**
     * Releases listeners
     */
    public void unSubscribe() {
        this.listener = null;
        this.stateMachine.setSdkListener(null);
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
