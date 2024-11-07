package com.example.laporan2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdminAdapter adapter;
    private FirebaseDatabase database;
    private DatabaseReference reportsRef;
    private Button btnLogout;
    private List<Report> reportList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        recyclerView = findViewById(R.id.recycler_view_edit_requests);
        btnLogout = findViewById(R.id.btnLogout);
        reportList = new ArrayList<>();

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        reportsRef = database.getReference("reports");

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminAdapter(this, reportList);
        recyclerView.setAdapter(adapter);

        // Load reports
        loadReports();

        // Logout button click listener
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(AdminActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void loadReports() {
        reportsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reportList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Report report = dataSnapshot.getValue(Report.class);
                    if (report != null) {
                        report.setId(dataSnapshot.getKey());
                        reportList.add(report);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminActivity.this, "Error: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}