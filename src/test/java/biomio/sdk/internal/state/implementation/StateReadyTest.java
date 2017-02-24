package biomio.sdk.internal.state.implementation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class StateReadyTest {

    private StateReady stateReady;

    @Before
    public void setUp() throws Exception {
        stateReady = new StateReady(null);

    }

    @After
    public void tearDown() throws Exception {
        stateReady = null;
    }

    @Test
    public void onFinishNope() throws Exception {
        NullPointerException ex = null;
        try {
            stateReady.onFinishNope();
        } catch (NullPointerException e) {
            ex = e;
        }
        assertTrue(ex == null);

    }

    @Test
    public void onNope() throws Exception {
        NullPointerException ex = null;
        try {
            stateReady.onNope(null, null, 1);
        } catch (NullPointerException e) {
            ex = e;
        }
        assertTrue(ex == null);
    }

}