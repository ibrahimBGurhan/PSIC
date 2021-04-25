package com.psic;

import java.time.LocalDateTime;

public class Treatment {

    private static int seq = 1;
    private String name;
    private String room;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Physiotherapist physiotherapist;
    private Status status;
    private final String id;

    public Treatment(String name, String room, LocalDateTime startTime, LocalDateTime endTime,
                     Physiotherapist physiotherapist) {
        this.name = name;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
        this.physiotherapist = physiotherapist;
        this.status = Status.AVAILABLE;
        this.id = "T-"+seq++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Physiotherapist getPhysiotherapist() {
        return physiotherapist;
    }

    public void setPhysiotherapist(Physiotherapist physiotherapist) {
        this.physiotherapist = physiotherapist;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Treatment{" +
                "id='" + id + '\'' +
                ",name='" + name + '\'' +
                ", room='" + room + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", physiotherapist=" + physiotherapist.getFullName() +
                ", status=" + status +
                '}';
    }
}