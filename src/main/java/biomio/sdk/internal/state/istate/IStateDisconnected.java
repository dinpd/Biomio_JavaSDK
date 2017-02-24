package biomio.sdk.internal.state.istate;


/**
 * The interface State disconnected.
 */
public interface IStateDisconnected extends IState {

    /**
     * On connect.
     */
    void onConnect();

    /**
     * On disconnect.
     */
    void onDisconnect();

}
