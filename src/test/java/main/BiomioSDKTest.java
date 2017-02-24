package main;

import biomio.sdk.BiomioSDK;
import biomio.sdk.internal.Options;
import org.junit.Assert;
import org.junit.Test;

import javax.net.ssl.SSLContext;

/**
 * The type Biomio sdk test.
 */
public class BiomioSDKTest {

    /**
     * Initialize null.
     *
     * @throws Exception the exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void initializeNull() throws Exception {
        BiomioSDK.destroy();
        BiomioSDK.initialize(null, null, null);
    }

    /**
     * Initialize one null.
     *
     * @throws Exception the exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void initializeOneNull() throws Exception {
        BiomioSDK.destroy();
        BiomioSDK.initialize(SSLContext.getDefault(), "", new Options("", "", "", "", "", "", "", -1));
    }

    /**
     * Initialize valid.
     *
     * @throws Exception the exception
     */
    @Test
    public void initializeValid() throws Exception {
        Exception caughtException = null;
        try {
            BiomioSDK.initialize(SSLContext.getDefault(), "wss://gate.biom.io:8090/websocket"
                    , new Options("", "", "", "", "", "", "", -1));
        } catch (IllegalArgumentException e) {
            Assert.assertFalse(false);
            caughtException = e;
        }
        Assert.assertTrue(caughtException == null);
    }
}