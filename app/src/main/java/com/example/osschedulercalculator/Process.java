package com.example.osschedulercalculator;

public class Process {
    String job;
    long arrivalTime;
    long burstTime;
    long responseTime = -1;
    long completeTime;
    long turnArondTime;
    long rank = 0;
    long waitingTime;
    public long remainingBurstTime = 0;

    public Process() {
    }

    public Process(String jobname) {
        this.job = jobname;
    }

    public Process(String job, long arrivalTime, long burstTime) {
        this.job = job;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        remainingBurstTime = burstTime;
    }

    public Process(String job, long arrivalTime, long burstTime, long completeTime) {
        this.job = job;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.completeTime = completeTime;
        remainingBurstTime = burstTime;

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


}
