package com.psic;

public abstract class Member {

    private String Id;
    private String fullName;
    private String address;
    private String telephoneNumber;

    public Member(String id, String fullName, String address, String telephoneNumber) {
        Id = id;
        this.fullName = fullName;
        this.address = address;
        this.telephoneNumber = telephoneNumber;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }
}
