package state.istate;


public interface IStateReady extends IState {

    void onFinishNope();
    void onNope(final String fingerPrint, final String refreshToken, int ttl);
}
