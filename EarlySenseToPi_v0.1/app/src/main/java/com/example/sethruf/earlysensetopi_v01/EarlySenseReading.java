package com.example.sethruf.earlysensetopi_v01;

/**
 * Created by Seth.Ruf on 27/04/2015.
 */
public class EarlySenseReading {

    private int timestamp;
    private int heartRate;
    private int respiratoryRate;
    private int movementLevel;
    private boolean inBed;

    public EarlySenseReading(int timestamp, int heartRate, int respiratoryRate, int movementLevel, boolean inBed){
        this.timestamp = timestamp;
        this.heartRate = heartRate;
        this.respiratoryRate = respiratoryRate;
        this.movementLevel = movementLevel;
        this.inBed = inBed;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public int getRespiratoryRate() {
        return respiratoryRate;
    }

    public void setRespiratoryRate(int respiratoryRate) {
        this.respiratoryRate = respiratoryRate;
    }

    public int getMovementLevel() {
        return movementLevel;
    }

    public void setMovementLevel(int movementLevel) {
        this.movementLevel = movementLevel;
    }

    public boolean isInBed() {
        return inBed;
    }

    public void setInBed(boolean inBed) {
        this.inBed = inBed;
    }
}
