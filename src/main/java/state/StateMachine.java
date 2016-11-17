package state;

import main.BiomioSDK;
import main.OnBiomioSdkListener;
import network.SocketCallManager;
import org.json.JSONObject;
import state.implementation.StateConnected;
import state.implementation.StateDisconnected;
import state.implementation.StateReady;
import state.istate.IState;
import state.istate.IStateConnected;
import state.istate.IStateDisconnected;
import state.istate.IStateReady;
import util.Constants;
import util.JsonParser;
import util.Logger;
import util.Validator;

import javax.net.ssl.SSLContext;

/**
 * This class is responsible for switching states basing on client actions as well as
 * on server responses. Basing on response - manages private socket calls such as:
 * 1)registration handshake
 * 2)regular handshake
 * 3)nop
 * Sends notifications to client if actions from his side are required
 */
public class StateMachine {

    private SocketCallManager socketCallManager;
    private OnBiomioSdkListener sdkListener;

    private IState currentState = null;
    private IStateDisconnected stateDisconnected;
    private IStateConnected stateConnected;
    private IStateReady stateReady;

    public StateMachine(SSLContext context, String url) {
        this.socketCallManager = new SocketCallManager(context, url, this);

        this.stateDisconnected = new StateDisconnected(socketCallManager);
        this.stateConnected = new StateConnected(socketCallManager);
        this.stateReady = new StateReady(socketCallManager);
    }

    /**
     * Opens connection to socket
     */
    public void connect() {
        Logger.d("connect");
        registerStateChange(stateDisconnected);
        stateDisconnected.onConnect();
    }

    /**
     * Finalizes {@link IStateReady} if necessary and close socket connection
     */
    public void disconnect() {
        registerStateChange(stateDisconnected);
        stateDisconnected.onDisconnect();
    }

    /**
     * Registers {@link StateConnected}
     */
    public void registerConnected() {
        BiomioSDK.connected = true;
        registerStateChange(stateConnected);
    }

    /**
     * Registers {@link StateDisconnected}
     */
    public void registerDisconnected() {
        BiomioSDK.connected = false;
        registerStateChange(stateDisconnected);
    }

    /**
     * Registers current state.
     *
     * @param state - {@link IState}
     */
    private void registerStateChange(IState state) {
        if (currentState != null && currentState instanceof StateReady) {
            ((StateReady) currentState).onFinishNope();
        }
        currentState = state;
        Logger.d("registerStateChange :: " + state.getClass().getSimpleName());
    }

    /**
     * This method is invoked in response callback of socket in order to switch the state,
     * basing on received response
     *
     * @param jsonString - Response Json String
     */
    public void switchState(String jsonString) {

        if (!Validator.stringValid(jsonString)) {
            return;
        }

        checkForStatusAndDeliver(jsonString);
        JSONObject responseJsonObject = new JSONObject(jsonString);

        String oid = JsonParser.getOid(jsonString);
        String headerToken = JsonParser.getHeaderToken(responseJsonObject);

        if (Validator.stringValid(headerToken)) {
            BiomioSDK.getOptions().setHeaderToken(headerToken);
        }

        Logger.d("switchState :: oid " + oid);

        if (oid.equals(Constants.SERVER_HELLO)) {

            Logger.d("serverHello");
            String privateKey = JsonParser.getRsaPrivateKey(new JSONObject(jsonString));

            if (Validator.stringValid(JsonParser.getFingerprint(responseJsonObject))) {
                BiomioSDK.getOptions().setFingerPrint(JsonParser.getFingerprint(responseJsonObject));
            }
            BiomioSDK.getOptions().setRefreshToken(JsonParser.getRefreshToken(responseJsonObject));
            BiomioSDK.getOptions().setTtl(JsonParser.getConnectionTTl(responseJsonObject));


            if (!Validator.stringValid(privateKey)) {
                Logger.d("serverHello :: regular");

                //regular

                sdkListener.onRegularHello(
                        BiomioSDK.getOptions().getTtl(),
                        JsonParser.getSessionTTl(responseJsonObject),
                        BiomioSDK.getOptions().getRefreshToken(),
                        BiomioSDK.getOptions().getFingerPrint(),
                        socketCallManager.generateHeaderStringForDigest(),
                        socketCallManager
                );


            } else {
                //registration
                Logger.d("serverHello :: registration");
                sdkListener.onRegistrationHello(
                        BiomioSDK.getOptions().getTtl(),
                        JsonParser.getSessionTTl(responseJsonObject),
                        BiomioSDK.getOptions().getRefreshToken(),
                        JsonParser.getRsaPrivateKey(responseJsonObject),
                        BiomioSDK.getOptions().getFingerPrint()
                );

                //handshake
                stateConnected.onRegistrationHandshake(BiomioSDK.getOptions().getHeaderToken(),
                        BiomioSDK.getOptions().getFingerPrint());

                Logger.d("serverHello :: handshake");

            }

            registerStateChange(stateReady);
            Logger.d("serverHello :: ready");
            sendNop();

        } else if (oid.equals(Constants.GET_RESOURCES)) {
            Logger.d("getResources :: ");
            sdkListener.onResources(socketCallManager);

        } else if (oid.equals(Constants.NOP)) {

            sendNop();

        } else if (oid.equals(Constants.TRY)) {
            Logger.d("try :: ");
            sdkListener.onTry(jsonString, socketCallManager);

        } else if (oid.equals(Constants.PROBE)) {
            Logger.d("probe :: ");
            String status = JsonParser.getProbeStatus(responseJsonObject);
            if (Validator.stringValid(status)) {
                sdkListener.onProbeStatus(status);
            }
        }
    }

    /**
     * If state is {@link IStateReady} - sends ping message to server
     * to notify that app is alive
     */
    private void sendNop() {
        if (currentState instanceof StateReady) {
            stateReady.onNope(
                    BiomioSDK.getOptions().getFingerPrint(),
                    BiomioSDK.getOptions().getRefreshToken(),
                    BiomioSDK.getOptions().getTtl()
            );
        }
    }

    /**
     * Ejects status string from response received from server if it's not null or empty.
     * In case of success - delivers the status to listener
     *
     * @param text - String response
     */
    private void checkForStatusAndDeliver(String text) {
        String status = JsonParser.getStatus(text);
        if (Validator.stringValid(status)) {
            sdkListener.onResponseStatus(status);
        }
    }

    /**
     * Registers {@link OnBiomioSdkListener}
     *
     * @param sdkListener - {@link OnBiomioSdkListener}
     */
    public void setSdkListener(OnBiomioSdkListener sdkListener) {
        this.sdkListener = sdkListener;
        this.socketCallManager.subscribe(sdkListener);
    }
}
