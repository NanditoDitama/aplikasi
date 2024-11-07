package com.example.laporan2;

import java.util.Date;

public class EditRequest {
    private String id;
    private String reportId;
    private String userId;
    private String userEmail;
    private String userName;
    private Report originalReport;
    private Report updatedReport;
    private Date requestDate;
    private String status; // "pending", "approved", "rejected"

    // Default constructor for Firestore
    public EditRequest() {}

    // Constructor
    public EditRequest(String id, String reportId, String userId, String userEmail, String userName,Report originalReport, Report updatedReport, Date requestDate, String status) {
        this.id = id;
        this.reportId = reportId;
        this.userId = userId;
        this.userEmail = userEmail;
        this.userName = userName;
        this.originalReport = originalReport;
        this.updatedReport = updatedReport;
        this.requestDate = requestDate;
        this.status = status;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Report getOriginalReport() {
        return originalReport;
    }

    public void setOriginalReport(Report originalReport) {
        this.originalReport = originalReport;
    }

    public Report getUpdatedReport() {
        return updatedReport;
    }

    public void setUpdatedReport(Report updatedReport) {
        this.updatedReport = updatedReport;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}