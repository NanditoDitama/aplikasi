package com.example.laporan2;

import java.util.Date;

public class ApprovalRequest {
    private String id;
    private String reportId;
    private String userId;
    private String userEmail;
    private String userName;
    private Report report;
    private Date requestDate;
    private String status; // "pending", "approved", "rejected"
    private String adminComment;

    // Empty constructor for Firestore
    public ApprovalRequest() {}

    public ApprovalRequest(String id, String reportId, String userId, String userEmail,
                           String userName, Report report, Date requestDate, String status) {
        this.id = id;
        this.reportId = reportId;
        this.userId = userId;
        this.userEmail = userEmail;
        this.userName = userName;
        this.report = report;
        this.requestDate = requestDate;
        this.status = status;
    }

    // Getters and Setters
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

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
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

    public String getAdminComment() {
        return adminComment;
    }

    public void setAdminComment(String adminComment) {
        this.adminComment = adminComment;
    }
}