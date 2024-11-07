package com.example.laporan2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.inappmessaging.model.Button;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditRequestDetailActivity extends AppCompatActivity {
    private TextView textViewOriginalTitle, textViewUpdatedTitle;
    private TextView textViewOriginalDescription, textViewUpdatedDescription;
    private TextView textViewOriginalAmount, textViewUpdatedAmount;
    private TextView textViewOriginalDate, textViewUpdatedDate;
    private ImageView imageViewOriginal, imageViewUpdated;
    private View buttonApprove;
    private View buttonReject;
    private FirebaseFirestore db;
    private String requestId;
    private EditRequest editRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_request_detail);

        // Inisialisasi FirebaseFirestore
        db = FirebaseFirestore.getInstance();

        // Dapatkan requestId dari intent
        requestId = getIntent().getStringExtra("requestId");
        if (requestId == null) {
            Toast.makeText(this, "Error: Request ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inisialisasi views
        initViews();

        // Setup click listeners
        setupClickListeners();

        // Load data
        loadEditRequest();
    }

    private void initViews() {
        // TextView untuk data original
        textViewOriginalTitle = findViewById(R.id.textViewOriginalTitle);
        textViewOriginalDescription = findViewById(R.id.textViewOriginalDescription);
        textViewOriginalAmount = findViewById(R.id.textViewOriginalAmount);
        textViewOriginalDate = findViewById(R.id.textViewOriginalDate);

        // TextView untuk data yang diupdate
        textViewUpdatedTitle = findViewById(R.id.textViewUpdatedTitle);
        textViewUpdatedDescription = findViewById(R.id.textViewUpdatedDescription);
        textViewUpdatedAmount = findViewById(R.id.textViewUpdatedAmount);
        textViewUpdatedDate = findViewById(R.id.textViewUpdatedDate);

        // ImageView
        imageViewOriginal = findViewById(R.id.imageViewOriginal);
        imageViewUpdated = findViewById(R.id.imageViewUpdated);

        // Buttons
        buttonApprove = findViewById(R.id.buttonApprove);
        buttonReject = findViewById(R.id.buttonReject);
    }

    private void setupClickListeners() {
        if (buttonApprove != null) {
            buttonApprove.setOnClickListener(v -> approveRequest());
        }

        if (buttonReject != null) {
            buttonReject.setOnClickListener(v -> rejectRequest());
        }
    }

    private void loadEditRequest() {
        if (requestId == null) return;

        db.collection("editRequests").document(requestId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        editRequest = documentSnapshot.toObject(EditRequest.class);
                        if (editRequest != null) {
                            displayRequestDetails();
                        } else {
                            Toast.makeText(this, "Error: Invalid request data", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        Toast.makeText(this, "Error: Request not found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading request: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void displayRequestDetails() {
        if (editRequest == null) return;

        // Display original report details
        Report originalReport = editRequest.getOriginalReport();
        if (originalReport != null) {
            textViewOriginalTitle.setText("Original Title: " + originalReport.getTitle());
            textViewOriginalDescription.setText("Original Description: " + originalReport.getDescription());
            textViewOriginalAmount.setText("Original Amount: " + originalReport.getAmount());
            textViewOriginalDate.setText("Original Date: " + formatDate(originalReport.getDate()));

            // Load original image if exists
            if (originalReport.getImageUrl() != null && !originalReport.getImageUrl().isEmpty()) {
                imageViewOriginal.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(originalReport.getImageUrl())
                        .into(imageViewOriginal);
            }
        }

        // Display updated report details
        Report updatedReport = editRequest.getUpdatedReport();
        if (updatedReport != null) {
            textViewUpdatedTitle.setText("Updated Title: " + updatedReport.getTitle());
            textViewUpdatedDescription.setText("Updated Description: " + updatedReport.getDescription());
            textViewUpdatedAmount.setText("Updated Amount: " + updatedReport.getAmount());
            textViewUpdatedDate.setText("Updated Date: " + formatDate(updatedReport.getDate()));

            // Load updated image if exists
            if (updatedReport.getImageUrl() != null && !updatedReport.getImageUrl().isEmpty()) {
                imageViewUpdated.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(updatedReport.getImageUrl())
                        .into(imageViewUpdated);
            }
        }
    }

    private String formatDate(Date date) {
        if (date == null) return "N/A";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    private void approveRequest() {
        if (editRequest == null) return;

        // Update status to approved
        editRequest.setStatus("approved");

        // Update the edit request in Firestore
        db.collection("editRequests").document(requestId)
                .set(editRequest)
                .addOnSuccessListener(aVoid -> {
                    // Update the original report with new data
                    updateOriginalReport();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to approve request: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void updateOriginalReport() {
        if (editRequest == null || editRequest.getUpdatedReport() == null) return;

        db.collection("reports").document(editRequest.getReportId())
                .set(editRequest.getUpdatedReport())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Request approved and report updated",
                            Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update report: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void rejectRequest() {
        if (editRequest == null) return;

        editRequest.setStatus("rejected");

        db.collection("editRequests").document(requestId)
                .set(editRequest)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Request rejected", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to reject request: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }
}