package state.implementation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class StateConnectedTest {

    private StateConnected stateConnected;

    @Before
    public void setUp() throws Exception {
        stateConnected = new StateConnected(null);

    }

    @After
    public void tearDown() throws Exception {
        stateConnected = null;
    }

    @Test
    public void onRegistrationHandshake() throws Exception {
        NullPointerException ex = null;
        try {
            stateConnected.onRegistrationHandshake(null, null);
        } catch (NullPointerException e) {
            ex = e;
        }
        assertTrue(ex == null);
    }

    @Test
    public void onRegularHandHandShake() throws Exception {
        NullPointerException ex = null;
        try {
            stateConnected.onRegularHandHandShake(null, null, null);
        } catch (NullPointerException e) {
            ex = e;
        }
        assertTrue(ex == null);
    }

}