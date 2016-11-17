package network;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
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
	
	private static final int CONNECTION_TIMEOUT = 20000;
	private static final String DEVICE_SECRET = "15595343";
	
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
		public void onConnecting() {}
		public void onConnected(AbstractSocketCallManager callManager) {
			mIsConnectivityTry = false;
			mIsConnected = true;
			mCallManager = callManager;
			synchronized (uLock) {
				uLock.notifyAll();
			}
		}
		public void onDisconnected() {
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
				AbstractSocketCallManager callManager) {}
		public void onResources(AbstractSocketCallManager callManager) {}
		public void onTry(String response, AbstractSocketCallManager callManager) {}
		public void onProbeStatus(String status) {}
		public void onError(String cause) {}
		public void onResponseStatus(String status) {}
	};
	
	private boolean mIsConnectivityTry;
	private boolean mIsConnected;
	private boolean mIsRegistered;
	private AbstractSocketCallManager mCallManager;
	
	@Test(timeout = CONNECTION_TIMEOUT)
	public void connectionFlow() throws Exception {
		connection();
		disconnection();
	}
	
	@Test(timeout = CONNECTION_TIMEOUT)
	public void sendRegistrationHandShake() throws Exception {
		connection();
		assertNotNull(mCallManager);
		assertNotEquals(DEVICE_SECRET, "");
		mCallManager.onSendClientHello(DEVICE_SECRET);
		synchronized (uLock) {
			while (!mIsRegistered || !mIsConnected) {
				uLock.wait();
			}
			assertTrue(mIsRegistered);
		}
		disconnection();
	}
	
	//Support
	
	private void connection() throws Exception {
		sInstance.subscribe(uListener);
		assertFalse(mIsConnected);
		mIsConnectivityTry = true;
		sInstance.connect();
		synchronized (uLock) {
			while (mIsConnectivityTry) {
				uLock.wait();
			}
			assertTrue(mIsConnected);
		}
	}
	
	private void disconnection() throws Exception {
		sInstance.subscribe(uListener);
		assertTrue(mIsConnected);
		mIsConnectivityTry = true;
		sInstance.disconnect();
		synchronized (uLock) {
			while (mIsConnectivityTry) {
				uLock.wait();
			}
			assertFalse(mIsConnected);
		}
		sInstance.subscribe(null);
	}
	
}