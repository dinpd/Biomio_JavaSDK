package biomio.sdk.internal.state.implementation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * The type State connected test.
 */
public class StateConnectedTest {

    private StateConnected stateConnected;

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        stateConnected = new StateConnected(null);

    }

    /**
     * Tear down.
     *
     * @throws Exception the exception
     */
    @After
    public void tearDown() throws Exception {
        stateConnected = null;
    }

    /**
     * On registration handshake.
     *
     * @throws Exception the exception
     */
    @Test
    public void onRegistrationHandshake() throws Exception {
        NullPointerException ex = null;
        try {
            stateConnected.sendRegistrationHandshake(null, null);
        } catch (NullPointerException e) {
            ex = e;
        }
        assertTrue(ex == null);
    }

    /**
     * On regular hand hand shake.
     *
     * @throws Exception the exception
     */
    @Test
    public void onRegularHandHandShake() throws Exception {
        NullPointerException ex = null;
        try {
            stateConnected.sendRegularHandHandShake(null, null);
        } catch (NullPointerException e) {
            ex = e;
        }
        assertTrue(ex == null);
    }

}