package biomio.sdk.internal.state.implementation;

import biomio.sdk.internal.network.SocketCallManager;
import biomio.sdk.internal.state.istate.IStateDisconnected;
import biomio.sdk.internal.util.Logger;

/**
 * IS registered when the connection with socket wasn't established yet
 * or it was closed
 */
public class StateDisconnected implements IStateDisconnected {

    private SocketCallManager socketCallManager;

    public StateDisconnected(SocketCallManager socketCallManager) {
        this.socketCallManager = socketCallManager;
    }

    /**
     * Opens socket connection
     */
    public void onConnect() {
        Logger.d("onConnect :: StateDisconnected");
        if (socketCallManager != null) {
            Logger.d("onConnect :: socketCallManager != null");
            socketCallManager.connect();
        }
    }

    /**
     * Closes socket connection
     */
    public void onDisconnect() {
        if (socketCallManager != null) {
            socketCallManager.sendBye();
            socketCallManager.disconnect();
        }
    }

}
