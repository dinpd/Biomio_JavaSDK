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

@RunWith(JUnit4.class)
public class SocketCallManagerTest extends Assert {
	
	private static final int CONNECTION_TIMEOUT = 60000;
	private static final String SECRET = "15595343";
	private static final String REGULAR_HELLO_INVALID = "event clientHello inappropriate in current state appregistered";
	private static final String STATUS_TIMEOUT = "Connection timeout";
	
	private static BiomioSDK sInstance;
	
	//SETUP
	
	@BeforeClass
	public static void setup() throws NoSuchAlgorithmException {
		sInstance = BiomioSDK.initialize(
				SSLContext.getDefault(),
				"wss://gate.biom.io:8090/websocket",
				new Options("MockedDevice999", "Linux", "LAN", "1.0", "", "", "", 30)
		);
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
		}
		public void onResources(AbstractSocketCallManager callManager) {
			log("onResources");
		}
		public void onTry(String response, AbstractSocketCallManager callManager) {
			log("onTry :\n\tresponse=" + response);
		}
		public void onProbeStatus(String status) {
			log("onProbeStatus :\n\t" + status);
		}
		public void onError(String cause) {
			log("onError :\n\t" + cause);
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
			if (!mStatus.equals(STATUS_TIMEOUT)) {
				uLock.wait();
			}
			assertEquals(mStatus, STATUS_TIMEOUT);
		}
		disconnectionFlw();
		assertFalse(mIsConnected);
		unsubscribe();
	}
	
	@Test(timeout = CONNECTION_TIMEOUT / 2)
	public void sendRegistrationHandShake() throws Exception {
		subscribe();
		connectionFlw();
		assertTrue(mIsConnected);
		registrationFlw();
		assertTrue(mIsConnected);
		disconnectionFlw();
		unsubscribe();
	}
	
	@Ignore
	@Test(timeout = CONNECTION_TIMEOUT)
	public void sendRegularHello() throws Exception {
		connectionFlw();
		registrationFlw();
		assertNotNull(mCallManager);
		assertNotEquals(mStatus, REGULAR_HELLO_INVALID);
		mCallManager.sendRegularHello("mockedId");
		synchronized (uLock) {
			while (!mIsRegularHello && mIsConnected) {
				uLock.wait();
			}
		}
		assertEquals(mStatus, REGULAR_HELLO_INVALID);
		disconnectionFlw();
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
		sInstance.connect();
		synchronized (uLock) {
			while (mIsConnectivityTry) {
				uLock.wait();
			}
		}
	}
	
	private void disconnectionFlw() throws Exception {
		mIsConnectivityTry = true;
		sInstance.disconnect();
		synchronized (uLock) {
			while (mIsConnectivityTry) {
				uLock.wait();
			}
		}
	}
	
	private void registrationFlw() throws Exception {
		assertNotNull(mCallManager);
		assertNotEquals(SECRET, ""); //SECRET must be not empty
		mCallManager.sendClientHello(SECRET);
		synchronized (uLock) {
			while (!mIsRegistered && mIsConnected) {
				uLock.wait();
			}
			assertTrue(mIsRegistered);
		}
	}
	
	//Logger
	
	private void log(String message) {
		System.out.println(message);
	}
	
}