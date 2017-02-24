package biomio.sdk.internal.state;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * The type State machine test.
 */
public class StateMachineTest {
    /**
     * Switch state.
     *
     * @throws Exception the exception
     */
    @Test
    public void switchState() throws Exception {
        StateMachine stateMachine = new StateMachine(null, "");

        NullPointerException ex = null;
        try {
            stateMachine.switchState(null);
        } catch (NullPointerException e) {
            ex = e;
        }
        assertFalse(ex != null);
    }

}