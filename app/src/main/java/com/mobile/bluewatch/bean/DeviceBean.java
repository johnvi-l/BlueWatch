package com.mobile.bluewatch.bean;

public class DeviceBean {
    private int heartData;
    private int systolicPressure ;
    private int diastolicPressure ;
    private int respiratoryRate ;
    private int fatigueValue ;
    private String createTime;

    public int getHeartData() {
        return heartData;
    }

    public void setHeartData(int heartData) {
        this.heartData = heartData;
    }

    public int getSystolicPressure() {
        return systolicPressure;
    }

    public void setSystolicPressure(int systolicPressure) {
        this.systolicPressure = systolicPressure;
    }

    public int getDiastolicPressure() {
        return diastolicPressure;
    }

    public void setDiastolicPressure(int diastolicPressure) {
        this.diastolicPressure = diastolicPressure;
    }

    public int getRespiratoryRate() {
        return respiratoryRate;
    }

    public void setRespiratoryRate(int respiratoryRate) {
        this.respiratoryRate = respiratoryRate;
    }

    public int getFatigueValue() {
        return fatigueValue;
    }

    public void setFatigueValue(int fatigueValue) {
        this.fatigueValue = fatigueValue;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
