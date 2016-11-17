package main;

import model.Options;
import network.AbstractSocketCallManager;
import org.junit.Assert;
import org.junit.Test;
import util.Logger;

import javax.net.ssl.SSLContext;

public class BiomioSDKTest {

    @Test(expected = IllegalArgumentException.class)
    public void initializeNull() throws Exception {
        BiomioSDK.destroy();
        BiomioSDK.initialize(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void initializeOneNull() throws Exception {
        BiomioSDK.destroy();
        BiomioSDK.initialize(SSLContext.getDefault(), "", new Options("", "", "", "", "", "", "", -1));
    }

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