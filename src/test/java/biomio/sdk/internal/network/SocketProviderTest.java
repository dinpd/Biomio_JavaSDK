package biomio.sdk.internal.network;

import com.neovisionaries.ws.client.WebSocket;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

/**
 * The type Socket provider test.
 */
@RunWith(JUnit4.class)
public class SocketProviderTest extends Assert {
	
	private SocketProvider mProvider;

    /**
     * Init.
     */
    @Before
	public void init() {
		mProvider = new SocketProvider();
	}

    /**
     * Dispose.
     */
    @After
	public void dispose() {
		mProvider = null;
	}

    /**
     * Create nullable socket.
     *
     * @throws NoSuchAlgorithmException the no such algorithm exception
     */
    @Test
	public void createNullableSocket() throws NoSuchAlgorithmException {
		WebSocket socket;
		socket = mProvider.createWebSocket(null, null);
		assertNull(socket);
		socket = mProvider.createWebSocket(null, "invalid_url");
		assertNull(socket);
		socket = mProvider.createWebSocket(SSLContext.getDefault(), null);
		assertNull(socket);
	}
	
}