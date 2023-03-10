package biomio.sdk;

import biomio.sdk.internal.Options;
import biomio.sdk.internal.Probe;
import biomio.sdk.internal.network.AbstractSocketCallManager;
import biomio.sdk.internal.util.Constants;
import biomio.sdk.internal.util.Logger;

import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class Example implements OnBiomioSdkListener {

    private String deviceId = Constants.EMPTY_STRING;
    private String osId = Constants.EMPTY_STRING;
    private String networkType = Constants.EMPTY_STRING;
    private String refreshToken = Constants.EMPTY_STRING;
    private String appId = Constants.EMPTY_STRING;
    private String headerToken = Constants.EMPTY_STRING;
    private int connectionTimeout = -1;

    private BiomioSDK sdk;
    private AbstractSocketCallManager socketCallManager;

    public Example() {
        try {
            //initialize the SDK
            sdk = BiomioSDK.initialize(SSLContext.getDefault(), "wss://gate.biom.io:8090/websocket", createOptions());
        } catch (NoSuchAlgorithmException e) {
            Logger.d(e.toString());
        }
    }

    /**
     * Prepare details for the sdk before initializing it
     */
    private Options createOptions() {
        return new Options(
                deviceId,// id of the device generated on client side
                osId, //os id
                networkType, // type of network ex. WiFi
                "1.0",
                refreshToken, // refreshToken, may be empty if client doesn't have it
                appId, // appId which may be empty if client doesn't have it
                headerToken, //- headerToken which may be empty if client doesn't have it
                connectionTimeout); //- timeout.
    }

    /**
     * Triggered when socket connection was lost
     */
    public void onDisconnected() {
        //called when socket connection was lost
    }

    /**
     * Triggered when socket connection started opening
     */
    public void onConnecting() {
        //called when SDK is trying to connect
    }

    /**
     * Triggered when socket connection was opened
     *
     * @param callManager - Instance of {@link AbstractSocketCallManager} responsible for interactions with SDK                    see {@link AbstractSocketCallManager}
     */
    public void onConnected(AbstractSocketCallManager callManager) {
        //called when SDK is connected. AbstractSocketCallManager instance is received in order to do further actions
        this.socketCallManager = callManager;
    }

    /**
     * Callback which notifies about registration success event.
     *
     * @param connectionTtl - connection duration
     * @param sessionTtl    - session duration
     * @param refreshToken  - refresh token
     * @param privateKey    - RSA private key
     * @param fingerPrint   - fingerprint
     */
    public void onRegistrationHello(int connectionTtl, int sessionTtl, String refreshToken, String privateKey, String fingerPrint) {
        // you should save all the values received here to some storage and during next session pass this to SDK's Options
    }

    /**
     * Callback which notifies about regular (registered user's session) handshake
     *
     * @param connectionTtl   - connection duration
     * @param sessionTtl      - session duration
     * @param refreshToken    - refresh token
     * @param fingerPrint     - fingerprint
     * @param headerForDigest - signed with RSA private key header generated by client
     * @param callManager     - Instance of {@link AbstractSocketCallManager} responsible for interactions with SDK                        see {@link AbstractSocketCallManager}
     */
    public void onRegularHello(int connectionTtl, int sessionTtl, String refreshToken, String fingerPrint, String headerForDigest, AbstractSocketCallManager callManager) {
        //you should save all the values received here to some storage and during next session pass this to SDK's Options
        //than sign headerForDigest string with RSA private key by SHA1withRSA algorithm
        sendRegularHandShake(headerForDigest);
    }

    /**
     * Callback which notifies that Server gate requires resources available on client side
     *
     * @param callManager - Instance of {@link AbstractSocketCallManager} responsible for interactions with SDK see {@link AbstractSocketCallManager}
     */
    public void onResources(AbstractSocketCallManager callManager) {
        this.socketCallManager = callManager;
        sendResources();
    }

    /**
     * Callback which notifies about Auth event by passing server response String with required Auth types
     *
     * @param response    - String JSON response
     * @param callManager - Instance of {@link AbstractSocketCallManager} responsible for interactions with SDK see {@link AbstractSocketCallManager}
     */
    public void onTry(String response, AbstractSocketCallManager callManager) {
        //show try views, make user to perform auth actions and than sendProbe();
    }

    /**
     * Callback which notifies about Auth result
     *
     * @param status - JSON String
     */
    public void onProbeStatus(String status) {
        //provides status of probe
    }

    /**
     * Callback responsible for error notifications
     *
     * @param cause - String description of error cause
     */
    public void onError(String cause) {
        //returns error cause string
    }

    /**
     * Callback responsible for error notifications
     *
     * @param status - "
     */
    public void onResponseStatus(String status) {
        //returns status of the response
    }

    /**
     * Callback which returns logs to client
     *
     * @param log  - Log String
     * @param send - true if ping, false if pong
     */
    public void onLog(String log, boolean send) {

    }

    public void registerDevice(String secret) {
        if (socketCallManager != null) {
            socketCallManager.sendClientHello(secret);
        }
    }

    public void sendResources() {
        if (socketCallManager != null) {
            String pushToken = ""; // if device - is a mobile phone - than it's required to send a push token to server
            socketCallManager.sendResources(new HashMap<String, String>(), pushToken);
        }
    }

    public void sendRegualarHello() {
        if (socketCallManager != null) {
            socketCallManager.sendRegularHello("app id which previously was stored locally");
        }
    }

    public void sendRegularHandShake(String headerForDigest) {
        if (socketCallManager != null) {
            socketCallManager.sendRegularHandShake("app id which previously was stored locally",
                    "header headerForDigest with RSA private key");
        }
    }

    private void sendProbe() {
        if (socketCallManager != null) {
            socketCallManager.sendProbe(new Probe());
        }
    }
}
