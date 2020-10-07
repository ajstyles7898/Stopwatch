package com.example.appstopwatch;

public class LapItem {
    private String serial;
    private String minl;
    private String minr;
    private String secl;
    private String secr;
    private String msecl;
    private String msecr;

    public LapItem(String serial,String minl, String minr, String secl, String secr, String msecl, String msecr) {
        this.serial = serial;
        this.minl = minl;
        this.minr = minr;
        this.secl = secl;
        this.secr = secr;
        this.msecl = msecl;
        this.msecr = msecr;
    }

    public String getSerial() {
        return serial;
    }

    public String getMinl() {
        return minl;
    }

    public String getMinr() {
        return minr;
    }

    public String getSecl() {
        return secl;
    }

    public String getSecr() {
        return secr;
    }

    public String getMsecl() {
        return msecl;
    }

    public String getMsecr() {
        return msecr;
    }
}
