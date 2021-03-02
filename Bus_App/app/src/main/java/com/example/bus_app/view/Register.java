package com.example.bus_app.view;

public class Register {
    public int id;
    public String rtransaction;
    public Double ramount;
    public String rdate;

    public Register(int id, String rtransaction, Double ramount, String rdate) {
        this.id = id;
        this.rtransaction = rtransaction;
        this.ramount = ramount;
        this.rdate = rdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRtransaction() {
        return rtransaction;
    }

    public void setRtransaction(String rtransaction) {
        this.rtransaction = rtransaction;
    }

    public Double getRamount() {
        return ramount;
    }

    public void setRamount(Double ramount) {
        this.ramount = ramount;
    }

    public String getRdate() {
        return rdate;
    }

    public void setRdate(String rdate) {
        this.rdate = rdate;
    }
}
