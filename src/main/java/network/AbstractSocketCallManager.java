package network;

import model.Probe;

import java.util.HashMap;

public abstract class AbstractSocketCallManager {

    public abstract void onSendClientHello(String secret);

    public abstract void onSendRegularHello(String appId);

    public abstract void onSendRegularHandShake(String appId, String digest);

    public abstract void onSendResources(HashMap<String, String> resourcesMap, String pushToken);

    public abstract void onSendProbe(Probe probe);

}
