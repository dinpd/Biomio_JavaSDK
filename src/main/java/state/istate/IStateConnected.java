package state.istate;


public interface IStateConnected extends IState {

    void onRegistrationHandshake(String token, String appID);

    void onRegularHandHandShake(String appId, String digest);

}
