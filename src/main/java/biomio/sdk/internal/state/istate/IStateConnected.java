package biomio.sdk.internal.state.istate;


/**
 * State which describes open connection of a
 */
public interface IStateConnected extends IState {

    /**
     * Sends registration handshake
     *
     * @param headerToken    - header token
     * @param appFingerprint - fingerprint of the app
     */
    void sendRegistrationHandshake(String headerToken, String appFingerprint);

    /**
     * Sends regular handshake
     *
     * @param appId  -
     * @param digest -
     */
    void sendRegularHandHandShake(String appId, String digest);

}
