package util;


import org.json.JSONObject;

/**
 * This class is responsible for parsing JSON response strings
 */
public class JsonParser {

    /**
     * Retrieves status string from JSON sting
     *
     * @param jsonString - JSON string
     * @return - String value of status, empty value if couldn't retrieve
     */
    public static String getStatus(String jsonString) {
        return getStringValueFromJSON(new JSONObject(jsonString), Constants.STATUS);
    }


    /**
     * Retrieves probe status from response JSONObject
     *
     * @param jsonObject {@link JSONObject}
     * @return - String value of probe status, empty value if couldn't retrieve
     */
    public static String getProbeStatus(JSONObject jsonObject) {
        return getStringValueFromJSON(jsonObject, Constants.MSG, Constants.PROBE_STATUS);
    }

    /**
     * Retrieves OID of response
     *
     * @param json -JSON String
     * @return - String value of oid , empty value if couldn't retrieve
     */

    public static String getOid(String json) {
        return getStringValueFromJSON(new JSONObject(json), Constants.MSG, Constants.OID);
    }

    /**
     * Returns RSA private key
     *
     * @param jsonObject - {@link JSONObject}
     * @return - String value of RSA private key , empty value if couldn't retrieve
     */
    public static String getRsaPrivateKey(JSONObject jsonObject) {
        return getStringValueFromJSON(jsonObject, Constants.MSG, Constants.KEY);
    }

    /**
     * Returns refresh token
     *
     * @param jsonObject - {@link JSONObject}
     * @return - String value of refresh token , empty value if couldn't retrieve
     */
    public static String getRefreshToken(JSONObject jsonObject) {
        return getStringValueFromJSON(jsonObject, Constants.MSG, Constants.REFRESH_TOKEN);
    }

    /**
     * Returns header token
     *
     * @param jsonObject - {@link JSONObject}
     * @return - String value of header token , empty value if couldn't retrieve
     */
    public static String getHeaderToken(JSONObject jsonObject) {
        return getStringValueFromJSON(jsonObject, Constants.HEADER, Constants.TOKEN);
    }

    /**
     * Returns fingerprint token
     *
     * @param jsonObject - {@link JSONObject}
     * @return - String value of fingerprint, empty value if couldn't retrieve
     */
    public static String getFingerprint(JSONObject jsonObject) {
        return getStringValueFromJSON(jsonObject, Constants.MSG, Constants.FINGERPRINT);
    }

    /**
     * Returns connectionttl int
     *
     * @param jsonObject - {@link JSONObject}
     * @return - int value of connectionTTl, empty value if couldn't retrieve
     */
    public static int getConnectionTTl(JSONObject jsonObject) {
        return getIntValueFromJSON(jsonObject, Constants.MSG, Constants.CONNECTION_TTL);
    }

    /**
     * Rreturns sessionttl int
     *
     * @param jsonObject - {@link JSONObject}
     * @return - int value of sessionttl, empty value if couldn't retrieve
     */
    public static int getSessionTTl(JSONObject jsonObject) {
        return getIntValueFromJSON(jsonObject, Constants.MSG, Constants.SESSION_TTL);
    }

    //general method to get string value from JSON only by child key
    static String getStringValueFromJSON(JSONObject jsonObject, String childKey) {
        if (jsonObject == null) {
            return Constants.EMPTY_STRING;
        }
        try {
            if (jsonObject.toString().contains(childKey)) {
                return (String) jsonObject.get(childKey);
            }
        } catch (Exception e) {
            return Constants.EMPTY_STRING;
        }
        return Constants.EMPTY_STRING;
    }

    //general method to get string value from JSON by parent key and child key
    static String getStringValueFromJSON(JSONObject jsonObject, String parentKey, String childKey) {
        if (jsonObject == null) {
            return Constants.EMPTY_STRING;
        }
        try {
            if (jsonObject.toString().contains(parentKey) && jsonObject.toString().contains(childKey)) {
                return jsonObject.getJSONObject(parentKey).getString(childKey);
            }
        } catch (Exception e) {
            return Constants.EMPTY_STRING;
        }
        return Constants.EMPTY_STRING;
    }

    static int getIntValueFromJSON(JSONObject jsonObject, String parentKey, String childKey) {
        if (jsonObject == null) {
            return 0;
        }
        try {
            if (jsonObject.toString().contains(parentKey) && jsonObject.toString().contains(childKey)) {
                return jsonObject.getJSONObject(parentKey).getInt(childKey);
            }
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }

    public static JSONObject generateImageProbeLog(JSONObject jsonObject) {
        try {
            JSONObject msg = jsonObject.getJSONObject(Constants.MSG);
            JSONObject probeData = msg.getJSONObject(Constants.PROBE_DATA);
            if (probeData != null) {
                String oid = probeData.getString(Constants.OID);
                if (oid.equals(Constants.IMAGE_SAMPLES)) {
                    probeData.getJSONArray(Constants.SAMPLES).remove(0);
                }
            }
        } catch (Exception e) {
            return jsonObject;
        }
        return jsonObject;
    }

}
