package network;

import model.Probe;

import java.util.HashMap;

/**
 * This abstract class is designated to describe actions which client must perform.
 * Implementation of this class is passed to the client via some of the {@link main.OnBiomioSdkListener}'s
 * callbacks to allow user perform required actions on demand
 */
public abstract class AbstractSocketCallManager {

    /**
     * This call is responsible for sending app secret to server
     * in order to register the app.
     *
     * @param secret - Code
     */
    public abstract void sendClientHello(String secret);

    /**
     * Sends appId of registered app to server.
     *
     * @param appId - appId
     */
    public abstract void sendRegularHello(String appId);

    /**
     * Sends regular handshake to server if app was already registered
     * and this is a second session after disconnection from socket
     *
     * @param appId  - fingerprint
     * @param digest - signed header with private key
     */
    public abstract void sendRegularHandShake(String appId, String digest);

    /**
     * Sends resources of the device.
     *
     * @param resourcesMap - map of key-value where key - property name, value -property
     * @param pushToken    - token for push notifications if available
     */
    public abstract void sendResources(HashMap<String, String> resourcesMap, String pushToken);

    /**
     * Sends {@link Probe} object to gate.
     *
     * @param probe - {@link Probe}
     */
    public abstract void sendProbe(Probe probe);

}
