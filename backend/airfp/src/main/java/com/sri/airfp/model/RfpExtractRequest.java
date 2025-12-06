package com.sri.airfp.model;

public class RfpExtractRequest {
    private String rfpText;

    public RfpExtractRequest(String rfpText) {
        this.rfpText = rfpText;
    }

    public String getRfpText() {
        return rfpText;
    }

    public void setRfpText(String rfpText) {
        this.rfpText = rfpText;
    }

    @Override
    public String toString() {
        return "RfpRequest{" +
                "rfpText='" + rfpText + '\'' +
                '}';
    }
}
