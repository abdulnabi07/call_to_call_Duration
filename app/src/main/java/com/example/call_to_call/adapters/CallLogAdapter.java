package com.example.call_to_call.adapters;

import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.call_to_call.R;
import com.example.call_to_call.model.CallLogItem;

import java.util.List;

public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.ViewHolder> {

    private List<CallLogItem> callLogItems;

    public CallLogAdapter(List<CallLogItem> callLogItems) {
        this.callLogItems = callLogItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calllog_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CallLogItem item = callLogItems.get(position);
        holder.numberTextView.setText(item.getNumber());
        holder.nameTextView.setText(item.getName() != null ? item.getName() : "Unknown");
       // holder.typeTextView.setText(getCallType(item.getType()));
        holder.durationTextView.setText(item.getDuration() + " sec");
    }

    @Override
    public int getItemCount() {
        return callLogItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView numberTextView, nameTextView, typeTextView, durationTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            numberTextView = itemView.findViewById(R.id.numberTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            typeTextView = itemView.findViewById(R.id.typeTextView);
            durationTextView = itemView.findViewById(R.id.durationTextView);
        }
    }

    /*private String getCallType(String type) {
        switch (Integer.parseInt(type)) {
            case CallLog.Calls.INCOMING_TYPE:
                return "Incoming";
            case CallLog.Calls.OUTGOING_TYPE:
                return "Outgoing";
            case CallLog.Calls.MISSED_TYPE:
                return "Missed";
            default:
                return "Unknown";
        }
    }*/
}
