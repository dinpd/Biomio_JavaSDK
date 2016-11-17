package network;


import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;
import sun.rmi.runtime.Log;
import util.Logger;
import util.Validator;

import javax.net.ssl.SSLContext;

class SocketProvider {

    SocketProvider() {
    }

    WebSocket createWebSocket(SSLContext sslContext, String url) {

        Logger.d("createWebSocket :: start");
        WebSocketFactory socketFactory = new WebSocketFactory();
        try {
            socketFactory.setSSLContext(sslContext);
        } catch (Exception e) {
            Logger.d("createWebSocket :: error setting SSLContext to socket factory");
        }
        try {
            return socketFactory.createSocket(Validator.stringNotNull(url));
        } catch (Exception e) {
            Logger.d("createWebSocket :: " + e.toString());
        }
        return null;
    }

}
