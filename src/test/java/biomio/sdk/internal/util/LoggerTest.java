package biomio.sdk.internal.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The type Logger test.
 */
public class LoggerTest {

    /**
     * D.
     *
     * @throws Exception the exception
     */
    @Test
    public void d() throws Exception {

        Exception ex = null;

        try {
            Logger.d(null);
        } catch (Exception e) {
            ex = e;
        }

        assertTrue(ex == null);
    }

}