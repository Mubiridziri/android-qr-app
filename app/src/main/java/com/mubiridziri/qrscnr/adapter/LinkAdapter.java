package com.mubiridziri.qrscnr.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mubiridziri.qrscnr.R;
import com.mubiridziri.qrscnr.entity.StoredData;

import java.util.List;

public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.ViewHolder>{

    private List<StoredData> storedDataList;

    public LinkAdapter(List<StoredData> storedDataList) {
        this.storedDataList = storedDataList;
    }

    @NonNull
    @Override
    public LinkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.link_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LinkAdapter.ViewHolder holder, int position) {
        holder.title.setText(storedDataList.get(position).title);
        holder.url_path.setText(storedDataList.get(position).content);
    }

    @Override
    public int getItemCount() {
        return this.storedDataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView url_path;
        Button linkButton;
        ViewHolder(@NonNull final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            url_path = itemView.findViewById(R.id.url_path);
            linkButton = itemView.findViewById(R.id.launch_link_button);
            linkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String url = url_path.getText().toString();
                    itemView.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                }
            });

        }
    }
}
