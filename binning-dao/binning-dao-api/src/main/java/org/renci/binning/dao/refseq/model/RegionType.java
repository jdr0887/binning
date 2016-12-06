package org.renci.binning.dao.refseq.model;

public enum RegionType {

    UTR("UTR"),

    UTR5("UTR-5"),

    UTR3("UTR-3"),

    EXON("exon"),

    INTRON("intron");

    private String value;

    private RegionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
