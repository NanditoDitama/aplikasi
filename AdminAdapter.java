package com.example.laporan2;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.ViewHolder> {
    private Context context;
    private List<Report> reportList;
    private DatabaseReference reportsRef;

    public AdminAdapter(Context context, List<Report> reportList) {
        this.context = context;
        this.reportList = reportList;
        this.reportsRef = FirebaseDatabase.getInstance().getReference("reports");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_edit_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Report report = reportList.get(position);

        holder.textViewTitle.setText(report.getTitle());
        holder.textViewUser.setText("By: " + report.getUserName());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.textViewDate.setText("Date: " + sdf.format(report.getDate()));

        setStatusColor(holder.textViewStatus, report.getStatus());

        // Set button visibility based on status
        if ("pending".equals(report.getStatus().toLowerCase())) {
            holder.buttonApprove.setVisibility(View.VISIBLE);
            holder.buttonReject.setVisibility(View.VISIBLE);
        } else {
            holder.buttonApprove.setVisibility(View.GONE);
            holder.buttonReject.setVisibility(View.GONE);
        }

        // Approve button click listener
        holder.buttonApprove.setOnClickListener(v -> {
            updateReportStatus(report.getId(), "approved");
        });

        // Reject button click listener
        holder.buttonReject.setOnClickListener(v -> {
            updateReportStatus(report.getId(), "rejected");
        });

        // View details button click listener
        holder.buttonViewDetails.setOnClickListener(v -> {
            // Implement view details functionality
            showReportDetails(report);
        });
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    private void updateReportStatus(String reportId, String status) {
        reportsRef.child(reportId).child("status").setValue(status)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(context, "Status updated successfully",
                                Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(context, "Failed to update status",
                                Toast.LENGTH_SHORT).show());
    }

    private void setStatusColor(TextView textViewStatus, String status) {
        int backgroundColor;
        int textColor = Color.WHITE;

        switch (status.toLowerCase()) {
            case "pending":
                backgroundColor = ContextCompat.getColor(context, R.color.status_pending);
                textViewStatus.setText("PENDING");
                break;
            case "approved":
                backgroundColor = ContextCompat.getColor(context, R.color.status_approved);
                textViewStatus.setText("APPROVED");
                break;
            case "rejected":
                backgroundColor = ContextCompat.getColor(context, R.color.status_rejected);
                textViewStatus.setText("REJECTED");
                break;
            default:
                backgroundColor = Color.GRAY;
                textViewStatus.setText(status.toUpperCase());
                break;
        }

        GradientDrawable background = (GradientDrawable) textViewStatus.getBackground();
        background.setColor(backgroundColor);
        textViewStatus.setTextColor(textColor);
    }

    private void showReportDetails(Report report) {
        // Implement dialog or new activity to show report details
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Report Details")
                .setMessage("Title: " + report.getTitle() + "\n" +
                        "User: " + report.getUserName() + "\n" +
                        "Date: " + new SimpleDateFormat("dd/MM/yyyy",
                        Locale.getDefault()).format(report.getDate()) + "\n" +
                        "Status: " + report.getStatus())
                .setPositiveButton("OK", null)
                .show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewUser, textViewDate, textViewStatus;
        Button buttonApprove, buttonReject, buttonViewDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewUser = itemView.findViewById(R.id.textViewUser);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            buttonApprove = itemView.findViewById(R.id.buttonApprove);
            buttonReject = itemView.findViewById(R.id.buttonReject);
            buttonViewDetails = itemView.findViewById(R.id.buttonViewDetails);
        }
    }
}