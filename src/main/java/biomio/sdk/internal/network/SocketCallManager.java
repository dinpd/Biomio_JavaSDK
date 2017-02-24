package biomio.sdk.internal.network;


import com.neovisionaries.ws.client.*;

import biomio.sdk.BiomioSDK;
import biomio.sdk.OnBiomioSdkListener;
import biomio.sdk.internal.Probe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import biomio.sdk.internal.state.StateMachine;
import biomio.sdk.internal.util.Constants;
import biomio.sdk.internal.util.JsonParser;
import biomio.sdk.internal.util.Logger;

import javax.net.ssl.SSLContext;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SocketCallManager
        extends AbstractSocketCallManager
        implements
        WebSocketListener {

    private static final String TAG = SocketCallManager.class.getSimpleName();

    private OnBiomioSdkListener sdkListener;

    private WebSocket webSocket = null;
    private SSLContext sslContext;

    private SocketProvider socketProvider;
    private StateMachine stateMachine;

    private String url;
    private AtomicInteger seqInteger = new AtomicInteger(0);

    private boolean connected = false;

    public SocketCallManager(SSLContext sslContext, String url, StateMachine stateMachine) {
        this.socketProvider = new SocketProvider();
        this.stateMachine = stateMachine;
        this.sslContext = sslContext;
        this.url = url;
    }

    /**
     * Opens Socket connection
     */
    public void connect() {
        if (sdkListener != null) {
            sdkListener.onConnecting();

            if (webSocket == null) {
                webSocket = socketProvider.createWebSocket(sslContext, url);
            } else {
                webSocket.removeListener(this);
                webSocket.disconnect();
                webSocket = null;
                webSocket = socketProvider.createWebSocket(sslContext, url);
            }
            webSocket.addListener(this);
            webSocket.connectAsynchronously();
            Logger.d(TAG + " :: connect");
        }

    }

    /**
     * Closes socket connection
     */
    public void disconnect() {
        if (webSocket != null) {
            Logger.d(TAG + " :: disconnect");
            webSocket.disconnect();
        }
    }

    private static int TIMER_TIMEOUT = 2;
    private boolean isTimerRunning = false;
    private int retryCount = 0;
    private Timer retryTimer = null;

    /**
     * Retries connection with socket if it was lost.
     * Increases timeout 5 times. Max is 14 seconds
     */
    private void retryConnect() {
        if (BiomioSDK.retry && !isTimerRunning && !connected) {
            Logger.d("retryConnect :: can retry connect to socket in ");
            isTimerRunning = true;

            int MAX_INCREASE = 5;
            if (retryCount != 0 && retryCount <= MAX_INCREASE) {
                TIMER_TIMEOUT += 2;
            }
            retryCount += 1;
            retryTimer = new Timer();
            retryTimer.scheduleAtFixedRate(new TimerTask() {

                int i = TIMER_TIMEOUT;

                public void run() {
                    Logger.d("retryConnect :: " + i);
                    i--;
                    if (i == 0) {
                        connect();
                        Logger.d("retryConnect :: connecting ");
                        isTimerRunning = false;
                        retryTimer.cancel();
                        retryTimer = null;
                    }
                }
            }, 0, 1000);
        }
    }

    private void invalidateRetryValues() {
        TIMER_TIMEOUT = 2;
        retryCount = 0;
        if (isTimerRunning && retryTimer != null) {
            retryTimer.cancel();
            retryTimer = null;
            isTimerRunning = false;
        }
    }

    /**
     * This call is responsible for sending app secret to server
     * in order to register the app.
     *
     * @param secret - Code
     */
    @Override
    public void sendClientHello(String secret) {
        Logger.d("sendClientHello :: " + secret);

        JSONObject msg = new JSONObject();

        try {
            JSONObject msgBody = new JSONObject();
            msgBody.put(Constants.OID, Constants.CLIENT_HELLO);
            msgBody.put(Constants.SECRET, secret);

            msg.put(Constants.HEADER, createHeader());
            msg.put(Constants.MSG, msgBody);

        } catch (JSONException e) {
            Logger.d("sendClientHello :: " + e.getMessage());
        }

        Logger.d(msg.toString());
        sendMessage(msg);
    }


    /**
     * Sends appId of registered app to server.
     *
     * @param appId - appId
     */
    @Override
    public void sendRegularHello(String appId) {
        JSONObject header = createHeader();
        JSONObject msg = new JSONObject();
        JSONObject msgBody = new JSONObject();

        try {
            header.put(Constants.APP_ID, appId);
            msgBody.put(Constants.OID, Constants.CLIENT_HELLO);

            msg.put(Constants.HEADER, header);
            msg.put(Constants.MSG, msgBody);

        } catch (JSONException e) {
            Logger.d("sendRegularHello :: " + e.getMessage());
        }

        sendMessage(msg);
    }

    /**
     * Sends regular handshake to server if app was already registered
     * and this is a second session after disconnection from socket
     *
     * @param appId  - fingerprint
     * @param digest - signed header with private key
     */
    public void sendRegularHandShake(String appId, String digest) {
        incrementSeq();

        JSONObject header = createHeader();
        JSONObject msg = new JSONObject();
        JSONObject msgBody = new JSONObject();

        try {
            header.put(Constants.APP_ID, appId);
            header.put(Constants.TOKEN, BiomioSDK.getOptions().getHeaderToken());

            msgBody.put(Constants.KEY, digest);
            msgBody.put(Constants.OID, Constants.AUTH);

            msg.put(Constants.HEADER, header);
            msg.put(Constants.MSG, msgBody);

        } catch (JSONException e) {
            e.printStackTrace();
            Logger.d("sendRegularHandShake :: " + e.getMessage());
        }
        sendMessage(msg);
    }

    /**
     * Sends resources of the device.
     *
     * @param resourcesMap - map of key-value where key - property name, value -property
     * @param pushToken    - token for push notifications if available
     */
    public void sendResources(HashMap<String, String> resourcesMap, String pushToken) {
        incrementSeq();
        JSONObject header = createHeader();
        JSONObject msg = new JSONObject();
        JSONObject msgBody = new JSONObject();

        try {
            header.put(Constants.APP_ID, BiomioSDK.getOptions().getFingerPrint());
            header.put(Constants.TOKEN, BiomioSDK.getOptions().getHeaderToken());

            JSONArray array = new JSONArray();

            for (Object o : resourcesMap.entrySet()) {
                Map.Entry thisEntry = (Map.Entry) o;
                String key = (String) thisEntry.getKey();
                String value = (String) thisEntry.getValue();
                JSONObject jsonResource = createResourceJSONObject(key, value);
                array.put(jsonResource);
            }

            msgBody.put(Constants.DATA, array);
            msgBody.put(Constants.OID, Constants.RESOURCES);
            msgBody.put(Constants.PUSH_TOKEN, pushToken);

            msg.put(Constants.HEADER, header);
            msg.put(Constants.MSG, msgBody);
        } catch (Exception e) {
            Logger.d("sendResources :: " + e.toString());
        }

        sendMessage(msg);
    }

    /**
     * Sends {@link Probe} object to gate.
     *
     * @param probe - {@link Probe}
     */
    public void sendProbe(Probe probe) {
        incrementSeq();
        JSONObject header = createHeader();

        JSONObject msg = new JSONObject();
        JSONObject msgBody = new JSONObject();

        JSONObject log = new JSONObject();
        JSONObject logBody = new JSONObject();

        try {
            if (probe == null || !probe.isValid()) throw new IllegalArgumentException();
            header.put(Constants.APP_ID, BiomioSDK.getOptions().getFingerPrint());
            header.put(Constants.TOKEN, BiomioSDK.getOptions().getHeaderToken());

            msg.put(Constants.HEADER, header);
            log.put(Constants.HEADER, header);

            msgBody.put(Constants.OID, Constants.PROBE);

            logBody.put(Constants.OID, Constants.PROBE);

            if (probe.getProbeData().getSample().equals(Constants.CANCELED)) {
                msgBody.put(Constants.PROBE_STATUS, probe.getProbeData().getSample());
                log.put(Constants.PROBE_STATUS, probe.getProbeData().getSample());
                msg.put(Constants.MSG, msgBody);
                sendMessage(msg);
                return;
            }

            JSONObject probeData = new JSONObject();
            JSONObject logData = new JSONObject();

            probeData.put(Constants.OID, probe.getProbeData().getOid());
            logData.put(Constants.OID, probe.getProbeData().getOid());

            JSONArray array = new JSONArray();
            array.put(probe.getProbeData().getSample());
            probeData.put(Constants.SAMPLES, array);
            logData.put(Constants.SAMPLES, "[base64]");
            msgBody.put(Constants.PROBE_DATA, probeData);
            logBody.put(Constants.PROBE_DATA, logData);

            msgBody.put(Constants.PROBE_STATUS, probe.getStatus());
            msgBody.put(Constants.TRY_TYPE, probe.getTryType());
            msgBody.put(Constants.TRY_ID, probe.getTryId());

            logBody.put(Constants.PROBE_STATUS, probe.getStatus());
            logBody.put(Constants.TRY_TYPE, probe.getTryType());
            logBody.put(Constants.TRY_ID, probe.getTryId());

            msg.put(Constants.MSG, msgBody);
            log.put(Constants.MSG, logBody);


        } catch (JSONException e) {
            Logger.d(TAG + " sendProbe :: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            Logger.d(TAG + " sendProbe :: " + e.getMessage());
        }

        sendMessage(msg);
    }

    /**
     * Sends registration handshake to server
     *
     * @param token - header token
     * @param appId - app's fingerprint received from server
     */

    public void sendRegistrationHandShake(String token, String appId) {
        incrementSeq();
        JSONObject header = createHeader();
        JSONObject msg = new JSONObject();
        JSONObject msgBody = new JSONObject();

        try {
            header.put(Constants.TOKEN, token);
            header.put(Constants.APP_ID, appId);

            msgBody.put(Constants.OID, Constants.ACK);

            msg.put(Constants.HEADER, header);
            msg.put(Constants.MSG, msgBody);

        } catch (JSONException e) {
            Logger.d("sendRegistrationHandShake :: " + e.getMessage());
        }

        sendMessage(msg);
    }

    /**
     * Sends ping (nop) message to server to notify sdk is alive
     *
     * @param fingerPrint  - App's fingerprint
     * @param refreshToken - Refresh token
     */
    public void sendNop(final String fingerPrint, final String refreshToken) {
        incrementSeq();
        JSONObject header = createHeader();
        JSONObject msg = new JSONObject();
        JSONObject msgBody = new JSONObject();

        try {
            header.put(Constants.APP_ID, fingerPrint);
            header.put(Constants.TOKEN, refreshToken);

            msgBody.put(Constants.OID, Constants.NOP);

            msg.put(Constants.HEADER, header);
            msg.put(Constants.MSG, msgBody);
        } catch (JSONException e) {
            Logger.d("onNope :: " + e.getMessage());
        }

        sendMessage(msg);
    }

    /**
     * Used to interrupt connection
     */
    public void sendBye() {
        incrementSeq();

        JSONObject header = createHeader();
        JSONObject msg = new JSONObject();
        JSONObject msgBody = new JSONObject();

        try {
            header.put(Constants.APP_ID, BiomioSDK.getOptions().getFingerPrint());
            header.put(Constants.TOKEN, BiomioSDK.getOptions().getHeaderToken());

            msg.put(Constants.HEADER, header);

            msgBody.put(Constants.OID, Constants.BYE);

            msg.put(Constants.MSG, msgBody);

        } catch (Exception e) {
            Logger.d("sendBye :: " + e.toString());
        }

        sendMessage(msg);

    }

    //sends message to socket
    private void sendMessage(JSONObject jsonObject) {
        if (webSocket != null) {
            Logger.d("SENDING " + jsonObject.toString());
            webSocket.sendText(jsonObject.toString());
            sdkListener.onLog(JsonParser.generateImageProbeLog(jsonObject).toString(), true);
        }
    }


    /******************************************************************************************************************/
    /********************************************  General internal logic  ********************************************/
    /******************************************************************************************************************/


    /**
     * Increments sequence of app's calls
     */
    private void incrementSeq() {
        seqInteger.incrementAndGet();
        seqInteger.incrementAndGet();
    }

    /**
     * Creates Header JSONObject
     */
    private JSONObject createHeader() {
        JSONObject headerObject = new JSONObject();
        try {
            headerObject.put(Constants.OID, Constants.CLIENT_HEADER);
            headerObject.put(Constants.PROTO_VER, BiomioSDK.getOptions().getProtoVer());
            headerObject.put(Constants.APP_TYPE, Constants.PROBE);
            headerObject.put(Constants.DEV_ID, BiomioSDK.getOptions().getDeviceId());
            headerObject.put(Constants.OS_ID, BiomioSDK.getOptions().getOsId());
            headerObject.put(Constants.NETWORK, BiomioSDK.getOptions().getNetworkType());
            headerObject.put(Constants.SEQ, seqInteger);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return headerObject;
    }


    /**
     * Generates Header String (order matters) to be sign with private Key
     * //TODO - note that seq is hardcoded here. Try to figure out how to make it dynamic
     *
     * @return Digest String
     */
    public String generateHeaderStringForDigest() {
        int seq = seqInteger.intValue();
        seq += 2;
        return "{" + "\"" + Constants.OID + "\":\"" + Constants.CLIENT_HEADER + "\","
                + "\"" + Constants.SEQ + "\":" + seq + ","
                + "\"" + Constants.PROTO_VER + "\":" + "\"1.0\"" + ","
                + "\"" + Constants.APP_TYPE + "\":" + "\"" + Constants.PROBE + "\","
                + "\"" + Constants.APP_ID + "\":\"" + BiomioSDK.getOptions().getFingerPrint() + "\","
                + "\"" + Constants.OS_ID + "\":\"" + BiomioSDK.getOptions().getOsId() + "\","
                + "\"" + Constants.DEV_ID + "\":\"" + BiomioSDK.getOptions().getDeviceId() + "\","
                + "\"" + Constants.TOKEN + "\":\"" + BiomioSDK.getOptions().getHeaderToken() + "\"}";
    }

    /**
     * Creates json object of resource item
     *
     * @param property - hardware properties
     * @param propName - hardware property name
     * @return - {@link JSONObject}
     */
    private JSONObject createResourceJSONObject(Object propName, Object property) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(Constants.RTYPE, propName);
            jsonObject.put(Constants.RPROPERTIES, property);
        } catch (JSONException e) {
            Logger.d("createResourceJSONObject :: error " + e.getMessage());
        }

        return jsonObject;
    }


    /**
     * Subscribes a listener and initializes {@link SocketProvider}
     *
     * @param sdkListener - {@link OnBiomioSdkListener}
     */
    public void subscribe(OnBiomioSdkListener sdkListener) {
        this.sdkListener = sdkListener;
    }

    /******************************************************************************************************************/
    /********************************************* {@link WebSocket callbacks} ****************************************/
    /******************************************************************************************************************/

    public void onStateChanged(WebSocket websocket, WebSocketState newState) throws Exception {
        Logger.d(TAG + "onStateChanged");
    }

    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
        Logger.d(TAG + "onConnected");
        sdkListener.onConnected(this);
        invalidateRetryValues();
        stateMachine.registerConnected();
        connected = true;
    }

    public void onConnectError(WebSocket websocket, WebSocketException cause) throws Exception {
        connected = false;
        Logger.d(TAG + "onConnectError");
        sdkListener.onError(cause.toString());
        stateMachine.registerDisconnected();
        retryConnect();
    }

    public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
        connected = false;
        Logger.d(TAG + "onDisconnected");
        sdkListener.onDisconnected();
        stateMachine.registerDisconnected();
        retryConnect();
    }

    public void onFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
        Logger.d(TAG + "onFrame");
    }

    public void onContinuationFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

    }

    public void onTextFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

    }

    public void onBinaryFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

    }

    public void onCloseFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

    }

    public void onPingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

    }

    public void onPongFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

    }

    public void onTextMessage(WebSocket websocket, String text) throws Exception {
        Logger.d(TAG + "onTextMessage " + text);
        sdkListener.onLog(text, false);
        stateMachine.switchState(text);
    }

    public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {

    }

    public void onSendingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

    }

    public void onFrameSent(WebSocket websocket, WebSocketFrame frame) throws Exception {

    }

    public void onFrameUnsent(WebSocket websocket, WebSocketFrame frame) throws Exception {

    }

    public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
        Logger.d(TAG + "onError " + cause.toString());
    }

    public void onFrameError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {
        Logger.d(TAG + "onFrameError " + cause.toString());
    }

    public void onMessageError(WebSocket websocket, WebSocketException cause, List<WebSocketFrame> frames) throws Exception {
        Logger.d(TAG + "onMessageError " + cause.toString());
    }

    public void onMessageDecompressionError(WebSocket websocket, WebSocketException cause, byte[] compressed) throws Exception {
        Logger.d(TAG + "onMessageDecompressionError " + cause.toString());
    }

    public void onTextMessageError(WebSocket websocket, WebSocketException cause, byte[] data) throws Exception {
        Logger.d(TAG + "onTextMessageError " + cause.toString());
    }

    public void onSendError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {
        Logger.d(TAG + "onSendError " + cause.toString());
    }

    public void onUnexpectedError(WebSocket websocket, WebSocketException cause) throws Exception {
        Logger.d(TAG + "onUnexpectedError " + cause.toString());
    }

    public void handleCallbackError(WebSocket websocket, Throwable cause) throws Exception {
        Logger.d(TAG + "handleCallbackError " + cause.toString());
    }

    public void onSendingHandshake(WebSocket websocket, String requestLine, List<String[]> headers) throws Exception {
        Logger.d(TAG + " onSendingHandshake ");
    }

}
