import main.BiomioSDK;
import main.OnBiomioSdkListener;
import model.Probe;
import network.AbstractSocketCallManager;

import java.util.HashMap;


public class Example {
//
//    private BiomioSDK sdk;
//    private AbstractSocketCallManager socketCallManager;
//
//    public Example() {
//        Options options = new Options(
//           deviceId, id of the device generated on client side
//           osId, //os id
//           networkType, // type of network ex. WiFi
//          "1.0",
//           refreshToken, // refreshToken, may be empty if client doesn't have it
//           appId, // appId which may be empty if client doesn't have it
//          headerToken, //- headerToken which may be empty if client doesn't have it
//           connectionTimeout); //- timeout.
//
//        sdk = BiomioSDK.initialize(SSLContext.getDefault(), "wss://gate.biom.io:8090/websocket", options);
//    }
//
//    public void onDisconnected() {
//        //called when socket connection was lost
//    }
//
//    public void onConnecting() {
//        //called when SDK is trying to connect
//    }
//
//    public void onConnected(AbstractSocketCallManager callManager) {
//        //called when SDK is connected. AbstractSocketCallManager instance is received in order to do further actions
//        this.socketCallManager = callManager;
//    }
//
//    public void onRegistrationHello(int connectionTtl, int sessionTtl, String refreshToken, String privateKey, String fingerPrint) {
//        // you should save all the values received here to some storage and during next session pass this to SDK's Options
//    }
//
//    public void onRegularHello(int connectionTtl, int sessionTtl, String refreshToken, String fingerPrint, String headerForDigest, AbstractSocketCallManager callManager) {
//        //you should save all the values received here to some storage and during next session pass this to SDK's Options
//        //than sign headerForDigest string with RSA private key by SHA1withRSA algorithm
//        sendRegularHandShake(headerForDigest);
//    }
//
//    public void onResources(AbstractSocketCallManager callManager) {
//        this.socketCallManager = callManager;
//        sendResources();
//    }
//
//    public void onTry(String response, AbstractSocketCallManager callManager) {
//        //show try views, make user to perform auth actions and than sendProbe();
//    }
//
//    public void onProbeStatus(String status) {
//        //provides status of probe
//    }
//
//    public void onError(String cause) {
//        //returns error cause string
//    }
//
//    public void onResponseStatus(String status) {
//        //returns status of the response
//    }
//
//    public void registerDevice(String secret) {
//        if (socketCallManager != null) {
//            socketCallManager.sendClientHello(secret);
//        }
//    }
//
//    //call in onResources() callback
//    public void sendResources() {
//        if (socketCallManager != null) {
//            String pushToken = ""; // if device - is a mobile phone - than it's required to send a push token to server
//            socketCallManager.sendResources(new HashMap<String, String>(), pushToken);
//        }
//    }
//
//    public void sendRegualarHello() {
//        if (socketCallManager != null) {
//            socketCallManager.sendRegularHello("app id which previously was stored locally");
//        }
//    }
//
//    public void sendRegularHandShake(String headerForDigest) {
//        if (socketCallManager != null) {
//            socketCallManager.sendRegularHandShake("app id which previously was stored locally", "header headerForDigest with RSA private key");
//        }
//    }
//
//    private void sendProbe() {
//        if (socketCallManager != null) {
//            socketCallManager.sendProbe(new Probe());
//        }
//    }

    //having performed all actions don't forget to call sdk.unsubscribe();
}
