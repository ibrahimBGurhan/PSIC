package com.psic;

import java.time.LocalDateTime;

public class ConsultancyAppointment {

    private String room;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Physiotherapist physiotherapist;
    private String id;
    private static int seq = 1;
    private Status status;

    public ConsultancyAppointment(String room, LocalDateTime startTime, LocalDateTime endTime, Physiotherapist physiotherapist) {
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
        this.physiotherapist = physiotherapist;
        this.id = "Cons-"+seq++;
        this.status = Status.AVAILABLE;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ConsultancyAppointment{" +
                "id='" + id + '\'' +
                ", physiotherapist=" + physiotherapist +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status=" + status +
                '}';
    }
}
