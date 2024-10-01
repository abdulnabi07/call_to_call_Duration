package com.example.call_to_call;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CallLogsAdapter extends  RecyclerView.Adapter<CallLogsAdapter.ViewHolder> {

    private final List<String> callLogs;

    public CallLogsAdapter(List<String> callLogs) {
        this.callLogs = callLogs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.callLogTextView.setText(callLogs.get(position));
    }

    @Override
    public int getItemCount() {
        return callLogs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView callLogTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            callLogTextView = itemView.findViewById(android.R.id.text1);
        }
    }

}
