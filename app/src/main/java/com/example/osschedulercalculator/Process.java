package com.example.osschedulercalculator;

public class Process {
    String job;
    long arrivalTime;
    long burstTime;
    long completeTime;
    long turnArondTime;
    long waitingTime;

    public Process() {
    }

    public Process(String job, long arrivalTime, long burstTime) {
        this.job = job;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
    }

    public Process(String job, long arrivalTime, long burstTime, long completeTime) {
        this.job = job;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.completeTime = completeTime;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public long getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(long burstTime) {
        this.burstTime = burstTime;
    }

    public long getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(long completeTime) {
        this.completeTime = completeTime;
    }

    public long getTurnArondTime() {
        return turnArondTime;
    }

    public void setTurnArondTime(long turnArondTime) {
        this.turnArondTime = turnArondTime;
    }

    public long getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(long waitingTime) {
        this.waitingTime = waitingTime;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    long responseTime;
}
