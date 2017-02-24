package biomio.sdk.internal.state.istate;


public interface IStateDisconnected extends IState {

    void onConnect();

    void onDisconnect();

}
