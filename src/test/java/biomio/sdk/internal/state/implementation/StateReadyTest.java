package biomio.sdk.internal.state.implementation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * The type State ready test.
 */
public class StateReadyTest {

    private StateReady stateReady;

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        stateReady = new StateReady(null);

    }

    /**
     * Tear down.
     *
     * @throws Exception the exception
     */
    @After
    public void tearDown() throws Exception {
        stateReady = null;
    }

    /**
     * On finish nope.
     *
     * @throws Exception the exception
     */
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

    /**
     * On nope.
     *
     * @throws Exception the exception
     */
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