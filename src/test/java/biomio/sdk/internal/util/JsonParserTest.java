package biomio.sdk.internal.util;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;


/**
 * The type Json parser test.
 */
public class JsonParserTest {


    private static final String VALID_JSON_STRING
            = "{\"msg\": {\"oid\": \"getResources\"}, \"header\": {\"token\": \"e0f4a372c12b7d88fe66023d86d6ef26963a5ad1\", \"oid\": \"serverHeader\", \"protoVer\": \"1.0\", \"seq\": 3}, \"status\": \"Session expired\"}";
    /**
     * The Empty json.
     */
    JSONObject emptyJSON = new JSONObject("{}");
    /**
     * The Valid json.
     */
    JSONObject validJSON = new JSONObject(VALID_JSON_STRING);

    /**
     * Gets string value from json.
     *
     * @throws Exception the exception
     */
    @Test
    public void getStringValueFromJSON() throws Exception {
        Assert.assertTrue(JsonParser.getStringValueFromJSON(emptyJSON, "msg").length() == 0);
        Assert.assertTrue(JsonParser.getStringValueFromJSON(validJSON, "msg").length() == 0);
        Assert.assertTrue(JsonParser.getStringValueFromJSON(validJSON, "status").length() > 0);
    }

    /**
     * Gets string value from json 1.
     *
     * @throws Exception the exception
     */
    @Test
    public void getStringValueFromJSON1() throws Exception {
        Assert.assertTrue(JsonParser.getStringValueFromJSON(emptyJSON, "msg", "oid").length() == 0);
        Assert.assertTrue(JsonParser.getStringValueFromJSON(validJSON, "msg", "oid").length() > 0);
        Assert.assertTrue(JsonParser.getStringValueFromJSON(validJSON, "msg", "oid").equals("getResources"));
        Assert.assertFalse(JsonParser.getStringValueFromJSON(validJSON, "msg", "status").equals("Session expired"));
        Assert.assertTrue(JsonParser.getStringValueFromJSON(validJSON, "msg", "status").length() == 0);
        Assert.assertTrue(JsonParser.getStringValueFromJSON(validJSON, "header", "token").equals("e0f4a372c12b7d88fe66023d86d6ef26963a5ad1"));
    }

    /**
     * Gets int value from json.
     *
     * @throws Exception the exception
     */
    @Test
    public void getIntValueFromJSON() throws Exception {
        Assert.assertTrue(JsonParser.getIntValueFromJSON(emptyJSON, "msg", "status") == 0);
        Assert.assertTrue(JsonParser.getIntValueFromJSON(validJSON, "msg", "status") == 0);
        Assert.assertTrue(JsonParser.getIntValueFromJSON(validJSON, "header", "seq") == 3);
        Assert.assertFalse(JsonParser.getIntValueFromJSON(validJSON, "header", "seffq") == 3);
        Assert.assertFalse(JsonParser.getIntValueFromJSON(validJSON, "assa", "seffq") == 3);
    }

}