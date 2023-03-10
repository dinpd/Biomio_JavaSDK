package biomio.sdk.internal.network;


import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;
import biomio.sdk.internal.util.Logger;
import biomio.sdk.internal.util.Validator;

import javax.net.ssl.SSLContext;

/**
 * The type Socket provider.
 */
class SocketProvider {

    /**
     * Instantiates a new Socket provider.
     */
    SocketProvider() {}

    /**
     * Create web socket web socket.
     *
     * @param sslContext the ssl context
     * @param url        the url
     * @return the web socket
     */
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
