package network;

import model.Probe;

import java.util.HashMap;

public abstract class AbstractSocketCallManager {

    public abstract void sendClientHello(String secret);

    public abstract void sendRegularHello(String appId);

    public abstract void sendRegularHandShake(String appId, String digest);

    public abstract void sendResources(HashMap<String, String> resourcesMap, String pushToken);

    public abstract void sendProbe(Probe probe);

}
