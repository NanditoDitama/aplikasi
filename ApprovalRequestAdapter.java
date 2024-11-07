package com.example.laporan2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ApprovalRequestAdapter extends RecyclerView.Adapter<ApprovalRequestAdapter.ViewHolder> {
    private List<ApprovalRequest> requestList;
    private OnApprovalActionListener listener;

    public ApprovalRequestAdapter(List<ApprovalRequest> requestList, ApprovalRequestActivity approvalRequestActivity) {
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_approval_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ApprovalRequest request = requestList.get(position);

        holder.textViewTitle.setText(request.getReport().getTitle());
        holder.textViewUser.setText(request.getUserName());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.textViewDate.setText(sdf.format(request.getRequestDate()));

        holder.buttonApprove.setOnClickListener(v -> {
            if (listener != null) {
                listener.onApproveClick(request);
            }
        });

        holder.buttonReject.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRejectClick(request);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public void updateData(List<ApprovalRequest> newRequestList) {
        this.requestList = newRequestList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewUser, textViewDate;
        Button buttonApprove, buttonReject;

        ViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewUser = itemView.findViewById(R.id.textViewUser);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            buttonApprove = itemView.findViewById(R.id.buttonApprove);
            buttonReject = itemView.findViewById(R.id.buttonReject);
        }
    }

    public interface OnApprovalActionListener {
        void onApproveClick(ApprovalRequest request);
        void onRejectClick(ApprovalRequest request);
    }

    public void setOnApprovalActionListener(OnApprovalActionListener listener) {
        this.listener = listener;
    }
}