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

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }
}
