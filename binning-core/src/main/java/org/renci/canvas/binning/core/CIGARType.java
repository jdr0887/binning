package org.renci.canvas.binning.core;

public enum CIGARType {

    MATCH("M"),

    INSERT("I"),

    DELETION("D");

    private String name;

    private CIGARType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
