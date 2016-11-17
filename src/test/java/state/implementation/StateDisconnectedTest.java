package state.implementation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class StateDisconnectedTest {

    private StateDisconnected stateDisconnected;

    @Before
    public void setUp() throws Exception {
        stateDisconnected = new StateDisconnected(null);
    }

    @After
    public void tearDown() throws Exception {
        stateDisconnected = null;
    }

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