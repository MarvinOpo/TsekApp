package com.example.mvopo.tsekapp.Model;

/**
 * Created by mvopo on 5/31/2018.
 */

public class DengvaxiaDetails {
    String facilityName, listNumber, doseScreen, doseDate, doseLot,
            doseBatch, doseExpiry, doseAefi, remarks, status;

    public DengvaxiaDetails(String facilityName, String listNumber, String doseScreen, String doseDate,
                            String doseLot, String doseBatch, String doseExpiry, String doseAefi, String remarks,
                            String status) {
        this.facilityName = facilityName;
        this.listNumber = listNumber;
        this.doseScreen = doseScreen;
        this.doseDate = doseDate;
        this.doseLot = doseLot;
        this.doseBatch = doseBatch;
        this.doseExpiry = doseExpiry;
        this.doseAefi = doseAefi;
        this.remarks = remarks;
        this.status = status;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public String getListNumber() {
        return listNumber;
    }

    public String getDoseScreen() {
        return doseScreen;
    }

    public String getDoseDate() {
        return doseDate;
    }

    public String getDoseLot() {
        return doseLot;
    }

    public String getDoseBatch() {
        return doseBatch;
    }

    public String getDoseExpiry() {
        return doseExpiry;
    }

    public String getDoseAefi() {
        return doseAefi;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getStatus() {
        return status;
    }
}
