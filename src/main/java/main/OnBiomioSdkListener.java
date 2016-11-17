package main;

import network.AbstractSocketCallManager;

/**
 * Listener which should be implemented by client in order to receive
 * results of Sdk actions
 */
public interface OnBiomioSdkListener {

    void onDisconnected();

    void onConnecting();

    void onConnected(AbstractSocketCallManager callManager);

    void onRegistrationHello(int connectionTtl, int sessionTtl, String refreshToken, String privateKey, String fingerPrint);

    void onRegularHello(int connectionTtl, int sessionTtl, String refreshToken, String fingerPrint, String headerForDigest, AbstractSocketCallManager callManager);

    void onResources(AbstractSocketCallManager callManager);

    void onTry(String response, AbstractSocketCallManager callManager);

    void onProbeStatus(String status);

    void onError(String cause);

    void onResponseStatus(String status);

}
