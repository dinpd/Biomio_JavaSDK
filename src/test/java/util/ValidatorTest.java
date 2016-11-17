package util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;


public class ValidatorTest {

    String nulString = null;

    @Test
    public void stringValid() throws Exception {
        Assert.assertTrue(Validator.stringValid("String"));
    }

    @Test
    public void stringNoValid() throws Exception {
        Assert.assertFalse(Validator.stringValid(""));
    }

    @Test
    public void stringNotNull() throws Exception {
        Assert.assertTrue(Validator.stringNotNull(nulString).length() == 0);
    }

}