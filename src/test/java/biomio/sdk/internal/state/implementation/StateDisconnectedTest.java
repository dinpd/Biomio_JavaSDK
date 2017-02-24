package biomio.sdk.internal.state.implementation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * The type State disconnected test.
 */
public class StateDisconnectedTest {

    private StateDisconnected stateDisconnected;

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        stateDisconnected = new StateDisconnected(null);
    }

    /**
     * Tear down.
     *
     * @throws Exception the exception
     */
    @After
    public void tearDown() throws Exception {
        stateDisconnected = null;
    }

    /**
     * On connect.
     *
     * @throws Exception the exception
     */
    @Test
    public void onConnect() throws Exception {
        NullPointerException ex = null;
        try {
            stateDisconnected.onConnect();
        } catch (NullPointerException e) {
            ex = e;
        }
        assertTrue(ex == null);
    }

    /**
     * On disconnect.
     *
     * @throws Exception the exception
     */
    @Test
    public void onDisconnect() throws Exception {
        NullPointerException ex = null;
        try {
            stateDisconnected.onDisconnect();
        } catch (NullPointerException e) {
            ex = e;
        }
        assertTrue(ex == null);
    }

}