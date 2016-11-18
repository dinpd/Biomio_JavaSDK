package state.implementation;

import network.SocketCallManager;
import state.istate.IStateConnected;


/**
 * This state is registered when socket connection is open
 */
public class StateConnected implements IStateConnected {

    private SocketCallManager socketCallManager;

    public StateConnected(SocketCallManager socketCallManager) {
        this.socketCallManager = socketCallManager;
    }

    /**
     * Sends registration handshake to server
     *
     * @param token - header token
     * @param appID - app's fingerprint received from server
     */
    public void onRegistrationHandshake(String token, String appID) {
        if(socketCallManager != null) {
            socketCallManager.sendRegistrationHandShake(token, appID);
        }
    }

    /**
     * Sends regular handshake to server if app was already registered
     * and this is a second session after disconnection from socket
     *
     * @param appId       - fingerprint
     * @param digest      - signed header with private key
     */
    public void onRegularHandHandShake(String appId, String digest) {
        if(socketCallManager != null) {
            socketCallManager.sendRegularHandShake(appId,digest);
        }
    }

}
