package network;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import main.BiomioSDK;
import main.OnBiomioSdkListener;
import model.Options;
import model.Probe;

@RunWith(JUnit4.class)
public class SocketCallManagerTest extends Assert {
	
	//Admin
	
	private static final String SECRET_CODE_VALID = "15595343";
	
	//Const
	
	private static final int CONNECTION_TIMEOUT = 55000;
	
	private static final String SECRET_CODE_INVALID = "22222222";
	
	private static final String STATUS_HELLO_INVALID = "event clientHello inappropriate in current state appregistered";
	private static final String STATUS_TIMEOUT = "Connection timeout";
	private static final String STATUS_NO_INTERNET
			= "com.neovisionaries.ws.client.WebSocketException: Failed to connect to 'gate.biom.io:8090': gate.biom.io";
	private static final String STATUS_INVALID_PROBE = "Invalid message sent (message string:{})";
	private static final String STATUS_REGULAR_HELLO_INVALID = "event clientHello inappropriate in current state appregistered";
	
	private static BiomioSDK sInstance;
	
	//SETUP
	
	@BeforeClass
	public static void setup() throws NoSuchAlgorithmException {
		sInstance = BiomioSDK.initialize(
				SSLContext.getDefault(),
				"wss://gate.biom.io:8090/websocket",
				new Options("MockedDevice999", "Linux", "LAN", "1.0", "", "", "", 30)
		);
		BiomioSDK.setRetry(false);
	}
	
	@AfterClass
	public static void shutdown() {
		BiomioSDK.destroy();
	}
	
	//Fields
	
	private final Object uLock = new Object();
	private final OnBiomioSdkListener uListener = new OnBiomioSdkListener() {
		public void onConnecting() {
			log("onConnecting");
			synchronized (uLock) {
				uLock.notifyAll();
			}
		}
		public void onConnected(AbstractSocketCallManager callManager) {
			log("onConnected");
			mIsConnectivityTry = false;
			mIsConnected = true;
			mCallManager = callManager;
			synchronized (uLock) {
				uLock.notifyAll();
			}
		}
		public void onDisconnected() {
			log("onDisconnected");
			mIsConnectivityTry = false;
			mIsConnected = false;
			mCallManager = null;
			synchronized (uLock) {
				uLock.notifyAll();
			}
		}
		public void onRegistrationHello(
				int connectionTtl,
				int sessionTtl,
				String refreshToken,
				String privateKey,
				String fingerPrint) {
			log(
					"onRegistrationHello :\n\tconnectionTtl=" + connectionTtl
							+ " sessionTtl=" + sessionTtl
							+ " refreshToken=" + refreshToken
							+ " privateKey=" + privateKey
							+ " fingerPrint=" + fingerPrint
			);
			mIsRegistered = true;
			synchronized (uLock) {
				uLock.notifyAll();
			}
		}
		public void onRegularHello(
				int connectionTtl,
				int sessionTtl,
				String refreshToken,
				String fingerPrint,
				String headerForDigest,
				AbstractSocketCallManager callManager) {
			log(
					"onRegistrationHello :\n\tconnectionTtl=" + connectionTtl
							+ " sessionTtl=" + sessionTtl
							+ " refreshToken=" + refreshToken
							+ " fingerPrint=" + fingerPrint
							+ " headerForDigest=" + headerForDigest
			);
			mIsRegularHello = true;
			synchronized (uLock) {
				uLock.notifyAll();
			}
		}
		public void onResources(AbstractSocketCallManager callManager) {
			log("onResources");
			synchronized (uLock) {
				uLock.notifyAll();
			}
		}
		public void onTry(String response, AbstractSocketCallManager callManager) {
			log("onTry :\n\tresponse=" + response);
			synchronized (uLock) {
				uLock.notifyAll();
			}
		}
		public void onProbeStatus(String status) {
			log("onProbeStatus :\n\t" + status);
			synchronized (uLock) {
				uLock.notifyAll();
			}
		}
		public void onError(String cause) {
			log("onError :\n\t" + cause);
			mErrorStatus = cause;
			synchronized (uLock) {
				uLock.notifyAll();
			}
		}
		public void onResponseStatus(String status) {
			log("onResponseStatus :\n\t" + status);
			mStatus = status;
			synchronized (uLock) {
				uLock.notifyAll();
			}
		}
	};
	
	private boolean mIsConnectivityTry;
	private boolean mIsConnected;
	private boolean mIsRegistered;
	private boolean mIsRegularHello;
	private String mStatus = "";
	private String mErrorStatus = "";
	private AbstractSocketCallManager mCallManager;
	
	@Test(timeout = CONNECTION_TIMEOUT / 2)
	public void connectionFlow() throws Exception {
		subscribe();
		assertFalse(mIsConnected);
		connectionFlw();
		assertTrue(mIsConnected);
		disconnectionFlw();
		assertFalse(mIsConnected);
		unsubscribe();
		boolean isError = false;
		try {
			connectionFlw();
		} catch (IllegalStateException e) {
			isError = true;
		}
		assertTrue(isError);
	}
	
	@Test(timeout = CONNECTION_TIMEOUT * 2)
	public void waitForRegistration() throws Exception {
		subscribe();
		assertFalse(mIsConnected);
		connectionFlw();
		assertTrue(mIsConnected);
		synchronized (uLock) {
			while (!mStatus.equals(STATUS_TIMEOUT)) {
				uLock.wait();
			}
		}
		assertEquals(mStatus, STATUS_TIMEOUT);
		disconnectionFlw();
		assertFalse(mIsConnected);
		unsubscribe();
	}
	
	@Test(timeout = CONNECTION_TIMEOUT / 2)
	public void sendRegistrationHandShake() throws Exception {
		subscribe();
		
		connectionFlw();
		assertTrue(mIsConnected);
		registrationFlw(SECRET_CODE_VALID);
		assertTrue(mIsRegistered);
		disconnectionFlw();
		
		connectionFlw();
		assertTrue(mIsConnected);
		registrationFlw(SECRET_CODE_INVALID);
		assertFalse(mIsRegistered);
		assertTrue(mIsConnected);
		disconnectionFlw();
		
		unsubscribe();
	}
	
	@Test(timeout = CONNECTION_TIMEOUT)
	public void sendRegularHello() throws Exception {
		subscribe();
		
		connectionFlw();
		registrationFlw(SECRET_CODE_VALID);
		assertTrue(mIsRegistered);
		
		assertNotNull(mCallManager);
		synchronized (uLock) {
			mCallManager.sendRegularHello("mockedId");
			while (!mStatus.equals(STATUS_REGULAR_HELLO_INVALID) && mIsConnected) {
				uLock.wait();
			}
		}
		assertEquals(mStatus, STATUS_REGULAR_HELLO_INVALID);
		
		assertTrue(mIsConnected);
		disconnectionFlw();
		assertFalse(mIsConnected);
		
		unsubscribe();
	}
	
	@Test(timeout = CONNECTION_TIMEOUT)
	public void sendProbe() throws Exception {
		subscribe();
		connectionFlw();
		assertTrue(mIsConnected);
		assertNotNull(mCallManager);
		mCallManager.sendProbe(new Probe());
		synchronized (uLock) {
			while (!mStatus.equals(STATUS_INVALID_PROBE)) {
				uLock.wait();
			}
		}
		disconnectionFlw();
		assertFalse(mIsConnected);
		unsubscribe();
	}
	
	/**
	 * To pass this test remove your internet connection
	 */
	@Ignore
	@Test(timeout = CONNECTION_TIMEOUT / 2)
	public void reconnectingFlow() throws Exception {
		subscribe();
		assertFalse(mIsConnected);
		sInstance.connect();
		synchronized (uLock) {
			while (!mErrorStatus.equals(STATUS_NO_INTERNET)) {
				uLock.wait();
			}
		}
		assertEquals(mErrorStatus, STATUS_NO_INTERNET);
		disconnectionFlw();
		//assertFalse(mIsConnected);
		unsubscribe();
	}
	
	//Support
	
	private void subscribe() {
		sInstance.subscribe(uListener);
	}
	
	private void unsubscribe() {
		sInstance.unsubscribe();
	}
	
	private void connectionFlw() throws Exception {
		mIsConnectivityTry = true;
		synchronized (uLock) {
			sInstance.connect();
			while (mIsConnectivityTry) {
				uLock.wait();
			}
		}
	}
	
	private void disconnectionFlw() throws Exception {
		mIsConnectivityTry = true;
		synchronized (uLock) {
			sInstance.disconnect();
			while (mIsConnectivityTry) {
				uLock.wait();
			}
		}
	}
	
	private void registrationFlw(String secret) throws Exception {
		assertNotNull(mCallManager);
		assertNotEquals(secret, "");
		synchronized (uLock) {
			mIsRegistered = false;
			mCallManager.sendClientHello(secret);
			while (!mIsRegistered && mStatus.equals("") && mIsConnected) {
				uLock.wait();
			}
		}
	}
	
	//Logger
	
	private void log(String message) {
		System.out.println(message);
	}
	
}