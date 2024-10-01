package com.example.call_to_call.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

public class CallRecordAdapter extends RecyclerView.Adapter<CallRecordAdapter.ViewHolder>{
    private List<String> recordings;
    private Context context;
    public CallRecordAdapter(Context context, List<String> recordings) {
        this.context = context;
        this.recordings = recordings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String filePath = recordings.get(position);
        holder.textView.setText(filePath);
        Log.d("api==>","Filepath==> " +filePath);
        holder.itemView.setOnClickListener(v -> playRecording(filePath));
      /*  holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);

                Log.d("api==>","Filepath==> " +filePath);
                intent.setDataAndType(Uri.parse(filePath), "audio/*");
                v.getContext().startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return recordings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }


    private void playRecording(String filePath) {
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
            Toast.makeText(context, "Playing recording...", Toast.LENGTH_SHORT).show();

           Log.d("api==>","Playing recording...");
            // Optional: Handle completion of the playback
            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release();
                Toast.makeText(context, "Playing completed...", Toast.LENGTH_SHORT).show();

                Log.d("api==>","Playing completed...");
            });

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error playing recording", Toast.LENGTH_SHORT).show();
            Log.d("api==>","Error playing recording");
        }
    }
}
