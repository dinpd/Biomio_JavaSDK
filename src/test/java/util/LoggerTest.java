package util;

import org.junit.Test;

import static org.junit.Assert.*;

public class LoggerTest {

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