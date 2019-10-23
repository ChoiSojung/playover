package com.playover.models;

import android.os.Parcel;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class MisuseReport {
    private String report;

    public MisuseReport() {}

    public MisuseReport(String report) {
        this.report = report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getReport() {
        return report;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("report", report);
        return result;
    }
}
