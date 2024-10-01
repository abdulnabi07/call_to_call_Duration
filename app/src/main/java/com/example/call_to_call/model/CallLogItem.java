package com.example.call_to_call.model;

public class CallLogItem {

    private String number;
    private String name;
    private String type;
    private String duration;
    private String callstart;
    private String callend;


    public String getCallstart() {
        return callstart;
    }

    public void setCallstart(String callstart) {
        this.callstart = callstart;
    }

    public String getCallend() {
        return callend;
    }

    public void setCallend(String callend) {
        this.callend = callend;
    }

/*
    public CallLogItem(String number, String name, String type, String duration, String callstart, String callend) {
        this.number = number;
        this.name = name;
        this.type = type;
        this.duration = duration;
        this.callstart = callstart;
        this.callend = callend;
    }
*/


    public CallLogItem(String number, String name, String type, String duration) {
        this.number = number;
        this.name = name;
        this.type = type;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
