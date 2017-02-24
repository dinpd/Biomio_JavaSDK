package biomio.sdk.internal.state.implementation;

import biomio.sdk.internal.network.SocketCallManager;
import biomio.sdk.internal.state.istate.IStateReady;
import biomio.sdk.internal.util.Logger;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This biomio.sdk.internal.state is appropriate and takes place when registration or regular actions were performed
 */
public class StateReady implements IStateReady {

    private static final int TTL_DEFAULT = 30;
    private Timer timer;

    private SocketCallManager socketCallManager;

    public StateReady(SocketCallManager socketCallManager) {
        this.socketCallManager = socketCallManager;
    }

    /**
     * Finalizes nop timer
     */
    public void onFinishNope() {
        if (timer != null) synchronized (timer) {
            if (timer != null) {
                Logger.d("onFinishNope :: canceling timer");
                timer.cancel();
                timer = null;
            }
        }
    }

    /**
     * Sends ping (nop) message to server to notify sdk is alive
     *
     * @param fingerPrint  - App's fingerprint
     * @param refreshToken - Refresh token
     * @param ttl          - time between two nops
     */
    public void onNope(final String fingerPrint, final String refreshToken, int ttl) {
        if (socketCallManager == null) {
            return;
        }
        if (timer != null) {
            timer = null;
        }
        timer = new Timer();
        Logger.d("onNope :: ttl " + ttl);
        int ttlDefault = TTL_DEFAULT;
        if (ttl > 0) {
            ttlDefault = ttl;
        }
        final int finalTtlDefault = ttlDefault;
        timer.scheduleAtFixedRate(new TimerTask() {

            int i = finalTtlDefault;

            public void run() {
                i--;
                if (i == 5) {
                    socketCallManager.sendNop(fingerPrint, refreshToken);
                    timer.cancel();
                }
            }
        }, 0, 1000);
    }

}
