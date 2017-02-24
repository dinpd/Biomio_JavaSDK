package biomio.sdk.internal;

/**
 * This model is responsible to overlap result of Authentication type -(this result is called Probe)
 */
public class ProbeData {

    /**
     * Auth type
     */
    private String oid = "";
    /**
     * Sample of auth data created by client
     */
    private String sample = "";

    /**
     * Gets oid.
     *
     * @return the oid
     */
    public String getOid() {
        return oid;
    }

    /**
     * Sets oid.
     *
     * @param oid the oid
     */
    public void setOid(String oid) {
        this.oid = oid;
    }

    /**
     * Gets sample.
     *
     * @return the sample
     */
    public String getSample() {
        return sample;
    }

    /**
     * Sets sample.
     *
     * @param sample the sample
     */
    public void setSample(String sample) {
        this.sample = sample;
    }
}
