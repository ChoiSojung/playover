package appdev.playover.models;

import android.os.Parcel;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class MisuseReport {
    private String report;
    private String reportedUser;
    private String reportingUser;

    public MisuseReport() {}

    public MisuseReport(String report, String reportedUser, String reportingUser) {
        this.reportedUser = reportedUser;
        this.reportingUser = reportingUser;
        this.report = report;
    }

    public void setreportedUser(String reportedUser) { this.reportedUser = reportedUser; }

    public void setreportingUser(String reportingUser) { this.reportingUser = reportingUser; }

    public void setReport(String report) {
        this.report = report;
    }

    public String getReportedUser() { return reportedUser; }

    public String getReportingUser() { return reportingUser; }

    public String getReport() {
        return report;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("report", report);
        result.put("reportedUser", reportedUser);
        result.put("reportingUser", reportingUser);
        return result;
    }
}
