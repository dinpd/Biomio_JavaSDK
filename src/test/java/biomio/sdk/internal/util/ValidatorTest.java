package biomio.sdk.internal.util;

import org.junit.Assert;
import org.junit.Test;


/**
 * The type Validator test.
 */
public class ValidatorTest {

    /**
     * The Nul string.
     */
    String nulString = null;

    /**
     * String valid.
     *
     * @throws Exception the exception
     */
    @Test
    public void stringValid() throws Exception {
        Assert.assertTrue(Validator.stringValid("String"));
    }

    /**
     * String no valid.
     *
     * @throws Exception the exception
     */
    @Test
    public void stringNoValid() throws Exception {
        Assert.assertFalse(Validator.stringValid(""));
    }

    /**
     * String not null.
     *
     * @throws Exception the exception
     */
    @Test
    public void stringNotNull() throws Exception {
        Assert.assertTrue(Validator.stringNotNull(nulString).length() == 0);
    }

}