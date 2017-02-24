package biomio.sdk.internal;

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

    /**
     * Instantiates a new Options.
     *
     * @param deviceId     the device id
     * @param osId         the os id
     * @param networkType  the network type
     * @param protoVer     the proto ver
     * @param refreshToken the refresh token
     * @param fingerPrint  the finger print
     * @param headerToken  the header token
     * @param ttl          the ttl
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

    /**
     * Gets device id.
     *
     * @return the device id
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * Gets os id.
     *
     * @return the os id
     */
    public String getOsId() {
        return osId;
    }

    /**
     * Gets network type.
     *
     * @return the network type
     */
    public String getNetworkType() {
        return networkType;
    }

    /**
     * Gets proto ver.
     *
     * @return the proto ver
     */
    public String getProtoVer() {
        return protoVer;
    }

    /**
     * Gets ttl.
     *
     * @return the ttl
     */
    public int getTtl() {
        return ttl;
    }

    /**
     * Gets finger print.
     *
     * @return the finger print
     */
    public String getFingerPrint() {
        return fingerPrint;
    }

    /**
     * Gets refresh token.
     *
     * @return the refresh token
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * Sets ttl.
     *
     * @param ttl the ttl
     */
    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    /**
     * Sets finger print.
     *
     * @param fingerPrint the finger print
     */
    public void setFingerPrint(String fingerPrint) {
        this.fingerPrint = fingerPrint;
    }

    /**
     * Sets refresh token.
     *
     * @param refreshToken the refresh token
     */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     * Gets header token.
     *
     * @return the header token
     */
    public String getHeaderToken() {
        return headerToken;
    }

    /**
     * Sets header token.
     *
     * @param headerToken the header token
     */
    public void setHeaderToken(String headerToken) {
        this.headerToken = headerToken;
    }
}
