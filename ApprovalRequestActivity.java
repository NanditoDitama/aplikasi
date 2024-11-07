package com.example.laporan2;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ApprovalRequestActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ApprovalRequestAdapter adapter;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_request);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recyclerViewApprovalRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadApprovalRequests();
    }

    private void loadApprovalRequests() {
        db.collection("approvalRequests")
                .whereEqualTo("status", "pending")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Error: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<ApprovalRequest> requests = new ArrayList<>();
                    if (value != null) {
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            ApprovalRequest request = doc.toObject(ApprovalRequest.class);
                            if (request != null) {
                                request.setId(doc.getId()); // Set ID dokumen
                                requests.add(request);
                            }
                        }
                    }

                    adapter = new ApprovalRequestAdapter(requests, this);
                    recyclerView.setAdapter(adapter);
                });
    }

    private void handleApproval(ApprovalRequest request, boolean isApproved) {
        String status = isApproved ? "approved" : "rejected";

        // Update status ApprovalRequest
        db.collection("approvalRequests")
                .document(request.getId())
                .update("status", status)
                .addOnSuccessListener(aVoid -> {
                    if (isApproved) {
                        // Update status Report menjadi approved
                        Report report = request.getReport();
                        report.setStatus("approved");

                        db.collection("reports")
                                .document(request.getReportId())
                                .set(report)
                                .addOnSuccessListener(aVoid2 -> {
                                    Toast.makeText(this, "Laporan disetujui",
                                            Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        // Update status Report menjadi rejected
                        Report report = request.getReport();
                        report.setStatus("rejected");

                        db.collection("reports")
                                .document(request.getReportId())
                                .set(report)
                                .addOnSuccessListener(aVoid2 -> {
                                    Toast.makeText(this, "Laporan ditolak",
                                            Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }
}