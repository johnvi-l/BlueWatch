package com.mobile.bluewatch.bean;

public class DeviceBean {
    private String heartData;
    private String systolicPressure ;
    private String diastolicPressure ;
    private String respiratoryRate ;
    private String fatigueValue ;
    private String time;


    public String getHeartData() {
        return heartData;
    }

    public void setHeartData(String heartData) {
        this.heartData = heartData;
    }

    public String getSystolicPressure() {
        return systolicPressure;
    }

    public void setSystolicPressure(String systolicPressure) {
        this.systolicPressure = systolicPressure;
    }

    public String getDiastolicPressure() {
        return diastolicPressure;
    }

    public void setDiastolicPressure(String diastolicPressure) {
        this.diastolicPressure = diastolicPressure;
    }

    public String getRespiratoryRate() {
        return respiratoryRate;
    }

    public void setRespiratoryRate(String respiratoryRate) {
        this.respiratoryRate = respiratoryRate;
    }

    public String getFatigueValue() {
        return fatigueValue;
    }

    public void setFatigueValue(String fatigueValue) {
        this.fatigueValue = fatigueValue;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
