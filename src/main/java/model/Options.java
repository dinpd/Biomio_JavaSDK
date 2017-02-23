package model;

/**
 * This model is designated to collect client's initial info for proper SDK functioning
 */
public class Options {

    /**
     * Some unique device id generated on client side
     */
    private String deviceId = "";
    /**
     * Client's operation system's id
     */
    private String osId = "";
    /**
     * Client's netwrok type: ex: Wifi, 3G, 4G, LTE
     */
    private String networkType = "";
    /**
     * TODO
     */
    private String protoVer = "";


    /*
    FIELDS REQUIRED FOR AUTHORIZED CLIENTS ONLY
     */
    /**
     * Connection duration
     */
    private int ttl = 0;
    /**
     * Fingerprint which was saved after registration
     */
    private String fingerPrint = "";
    /**
     * Refresh token which was saved after registration
     */
    private String refreshToken = "";
    /**
     * Header token which was saved after registration
     */
    private String headerToken = "";
    /*
    FIELDS REQUIRED FOR AUTHORIZED CLIENTS ONLY
     */

    public Options(String deviceId,
                   String osId,
                   String networkType,
                   String protoVer,
                   String refreshToken,
                   String fingerPrint,
                   String headerToken,
                   int ttl) {
        this.deviceId = deviceId;
        this.osId = osId;
        this.networkType = networkType;
        this.protoVer = protoVer;
        this.refreshToken = refreshToken;
        this.fingerPrint = fingerPrint;
        this.ttl = ttl;
        this.headerToken = headerToken;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getOsId() {
        return osId;
    }

    public String getNetworkType() {
        return networkType;
    }

    public String getProtoVer() {
        return protoVer;
    }

    public int getTtl() {
        return ttl;
    }

    public String getFingerPrint() {
        return fingerPrint;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public void setFingerPrint(String fingerPrint) {
        this.fingerPrint = fingerPrint;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getHeaderToken() {
        return headerToken;
    }

    public void setHeaderToken(String headerToken) {
        this.headerToken = headerToken;
    }
}
