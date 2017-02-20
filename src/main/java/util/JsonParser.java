package util;


import org.json.JSONObject;

public class JsonParser {

    public static String getStatus(String json) {
        return getStringValueFromJSON(new JSONObject(json), Constants.STATUS);
    }

    //retrieves probe status from response JSONObject
    public static String getProbeStatus(JSONObject jsonObject) {
        return getStringValueFromJSON(jsonObject, Constants.MSG, Constants.PROBE_STATUS);
    }

    //retrives OID of response
    public static String getOid(String json) {
        return getStringValueFromJSON(new JSONObject(json), Constants.MSG, Constants.OID);
    }

    //returns RSA private key
    public static String getRsaPrivateKey(JSONObject jsonObject) {
        return getStringValueFromJSON(jsonObject, Constants.MSG, Constants.KEY);
    }

    //returns refresh token
    public static String getRefreshToken(JSONObject jsonObject) {
        return getStringValueFromJSON(jsonObject, Constants.MSG, Constants.REFRESH_TOKEN);
    }

    //returns refresh token
    public static String getHeaderToken(JSONObject jsonObject) {
        return getStringValueFromJSON(jsonObject, Constants.HEADER, Constants.TOKEN);
    }

    //returns fingerprint
    public static String getFingerprint(JSONObject jsonObject) {
        return getStringValueFromJSON(jsonObject, Constants.MSG, Constants.FINGERPRINT);
    }

    //returns connectionttl int
    public static int getConnectionTTl(JSONObject jsonObject) {
        return getIntValueFromJSON(jsonObject, Constants.MSG, Constants.CONNECTION_TTL);
    }

    //returns sessionttl int
    public static int getSessionTTl(JSONObject jsonObject) {
        return getIntValueFromJSON(jsonObject, Constants.MSG, Constants.SESSION_TTL);
    }

    //general method to get string value from JSON only by child key
    public static String getStringValueFromJSON(JSONObject jsonObject, String childKey) {
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
    public static String getStringValueFromJSON(JSONObject jsonObject, String parentKey, String childKey) {
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

    public static int getIntValueFromJSON(JSONObject jsonObject, String parentKey, String childKey) {
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
