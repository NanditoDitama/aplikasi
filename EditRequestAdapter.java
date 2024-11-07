package com.example.laporan2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EditRequestAdapter extends RecyclerView.Adapter<EditRequestAdapter.ViewHolder> {
    private List<EditRequest> editRequests;
    private OnEditRequestClickListener listener;

    public interface OnEditRequestClickListener {
        void onEditRequestClick(EditRequest request);
    }

    public EditRequestAdapter(List<EditRequest> editRequests, OnEditRequestClickListener listener) {
        this.editRequests = editRequests;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EditRequest request = editRequests.get(position);
        holder.textViewTitle.setText(request.getUpdatedReport().getTitle());
        holder.textViewDate.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(request.getRequestDate()));
        holder.textViewUser.setText(request.getUserName());

        holder.itemView.setOnClickListener(v -> listener.onEditRequestClick(request));
    }

    @Override
    public int getItemCount() {
        return editRequests.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewDate, textViewUser;

        ViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewUser = itemView.findViewById(R.id.textViewUser);
        }
    }
}