package model;


public class Options {

    private String deviceId = "";
    private String osId = "";
    private String networkType = "";
    private String protoVer = "";

    private int ttl = 0;
    private String fingerPrint = "";
    private String refreshToken = "";
    private String headerToken = "";


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
