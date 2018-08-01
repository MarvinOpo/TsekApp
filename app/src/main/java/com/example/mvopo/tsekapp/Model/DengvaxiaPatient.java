package com.example.mvopo.tsekapp.Model;

/**
 * Created by mvopo on 5/28/2018.
 */

public class DengvaxiaPatient {
    String id, name, address, remarks, dob, status;

    public DengvaxiaPatient(String id, String name, String address, String remarks, String dob, String status) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.remarks = remarks;
        this.dob = dob;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
