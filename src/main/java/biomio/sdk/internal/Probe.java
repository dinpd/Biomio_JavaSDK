package biomio.sdk.internal;

/**
 * This model is used to wrap Probe which must be sent as a response for Try
 */
public class Probe {
    private ProbeData probeData;

    /**
     * Status of Try (success/failure)
     */
    private String status;
    /**
     * Type of the try. For ex. face,fingerprint.
     */
    private String tryType = "";
    /**
     * Id of a try
     */
    private String tryId = "";

    /**
     * Gets probe data.
     *
     * @return the probe data
     */
    public ProbeData getProbeData() {
        return probeData;
    }

    /**
     * Sets probe data.
     *
     * @param probeData the probe data
     */
    public void setProbeData(ProbeData probeData) {
        this.probeData = probeData;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets try type.
     *
     * @return the try type
     */
    public String getTryType() {
        return tryType;
    }

    /**
     * Sets try type.
     *
     * @param tryType the try type
     */
    public void setTryType(String tryType) {
        this.tryType = tryType;
    }

    /**
     * Gets try id.
     *
     * @return the try id
     */
    public String getTryId() {
        return tryId;
    }

    /**
     * Sets try id.
     *
     * @param tryId the try id
     */
    public void setTryId(String tryId) {
        this.tryId = tryId;
    }

    /**
     * Is valid boolean.
     *
     * @return the boolean
     */
    public boolean isValid() {
        return probeData != null;
    }
}
