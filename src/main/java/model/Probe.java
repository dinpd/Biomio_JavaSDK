package model;

/**
 * This model is used to wrap Probe which must be sent as a response for Try
 */
public class Probe {
    private ProbeData probeData;

    private String status;
    private String tryType = "";
    private String tryId = "";

    public ProbeData getProbeData() {
        return probeData;
    }

    public void setProbeData(ProbeData probeData) {
        this.probeData = probeData;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTryType() {
        return tryType;
    }

    public void setTryType(String tryType) {
        this.tryType = tryType;
    }

    public String getTryId() {
        return tryId;
    }

    public void setTryId(String tryId) {
        this.tryId = tryId;
    }
    
    public boolean isValid() {
        return probeData != null;
    }
}
