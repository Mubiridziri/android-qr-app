package com.mubiridziri.qrscnr.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.mubiridziri.qrscnr.R;
import com.mubiridziri.qrscnr.entity.StoredData;

import java.util.List;

public class StoredDataAdapter extends RecyclerView.Adapter<StoredDataAdapter.ViewHolder> {

    private List<StoredData> storedDataList;

    public StoredDataAdapter(List<StoredData> storedDataList) {
        this.storedDataList = storedDataList;
    }

    @NonNull
    @Override
    public StoredDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.link_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StoredDataAdapter.ViewHolder holder, final int position) {
        holder.title.setText(storedDataList.get(position).title);
        holder.content.setText(storedDataList.get(position).content);
        final Context context = holder.launchButton.getContext();
        if (storedDataList.get(position).type.equals(StoredData.URL_TYPE)) {
            holder.launchButton.setVisibility(View.VISIBLE);
            holder.launchButton.setText(context.getResources().getText(R.string.launch_link));
            Drawable openNewIcon = context.getResources().getDrawable(R.drawable.open_link_icon);
            holder.launchButton.setCompoundDrawablesWithIntrinsicBounds(openNewIcon, null, null, null);
            holder.launchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String url = storedDataList.get(position).content;
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                }
            });
        } else if (storedDataList.get(position).type.equals(StoredData.TEXT_TYPE)) {
            holder.launchButton.setVisibility(View.VISIBLE);
            holder.launchButton.setText(context.getResources().getText(R.string.open_text));
            Drawable openNewIcon = context.getResources().getDrawable(R.drawable.open_text_icon);
            holder.launchButton.setCompoundDrawablesWithIntrinsicBounds(openNewIcon, null, null, null);
            holder.launchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAlertDialogBuilder(context, storedDataList.get(position).content);
                }
            });

        }

    }

    private void getAlertDialogBuilder(Context context, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.open_text);
        builder.setMessage(content);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public int getItemCount() {
        return this.storedDataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView content;
        Button launchButton;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.url_path);
            launchButton = itemView.findViewById(R.id.launch_button);

        }
    }
}
