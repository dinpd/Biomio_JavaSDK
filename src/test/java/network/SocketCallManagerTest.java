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
	private static final String DEVICE_SECRET = "64845178";
	
	private static BiomioSDK sInstance;
	
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
	
	private final Object uLock = new Object();
	
	private boolean mIsConnected;
	private boolean mIsRegistered;
	private boolean mIsRecivedRegistrationHello;
	
	@Test(timeout = CONNECTION_TIMEOUT)
	public void connectDisconnect() throws Exception {
		sInstance.subscribe(new OnBiomioSdkListener() {
			public void onConnecting() {}
			public void onConnected(AbstractSocketCallManager callManager) {
				mIsConnected = true;
				synchronized (uLock) {
					uLock.notifyAll();
				}
			}
			public void onDisconnected() {
				mIsConnected = false;
				synchronized (uLock) {
					uLock.notifyAll();
				}
			}
			public void onRegistrationHello(
					int connectionTtl,
					int sessionTtl,
					String refreshToken,
					String privateKey,
					String fingerPrint) {}
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
		});
		mIsConnected = false;
		sInstance.connect();
		synchronized (uLock) {
			while (!mIsConnected) {
				uLock.wait();
			}
			assertTrue(mIsConnected);
		}
		sInstance.disconnect();
		synchronized (uLock) {
			while (mIsConnected) {
				uLock.wait();
			}
			assertFalse(mIsConnected);
		}
		sInstance.subscribe(null);
	}
	
	@Test(timeout = CONNECTION_TIMEOUT)
	public void sendRegistrationHandShake() throws Exception {
		sInstance.subscribe(new OnBiomioSdkListener() {
			public void onConnecting() {}
			public void onConnected(AbstractSocketCallManager callManager) {
				mIsConnected = true;
				assertNotEquals(DEVICE_SECRET, "");
				callManager.onSendClientHello(DEVICE_SECRET);
			}
			public void onDisconnected() {
				mIsConnected = false;
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
		});
		mIsRegistered = false;
		sInstance.connect();
		synchronized (uLock) {
			while (!mIsRegistered || !mIsConnected) {
				uLock.wait();
			}
			assertTrue(mIsRegistered);
		}
		
		sInstance.disconnect();
		sInstance.subscribe(null);
	}
	
}