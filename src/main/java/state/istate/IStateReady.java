package state.istate;


/**
 * Describes state when all the required actions were performed.
 * Responsible for sending heartbeats to server to keep connection alive
 */
public interface IStateReady extends IState {

    /**
     * Finished nope
     */
    void onFinishNope();

    /**
     * Sends heartbeat to server to keep connection open
     * @param fingerPrint - app fingerprint
     * @param refreshToken - refresh token
     * @param ttl - connection ttl
     */
    void onNope(final String fingerPrint, final String refreshToken, int ttl);

}
