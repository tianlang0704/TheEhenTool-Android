package org.acrew.cmonk.theehentool_preview.preview.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import org.acrew.cmonk.theehentool_preview.R;
import org.acrew.cmonk.theehentool_preview.preview.data.PreviewData;
import java.util.ArrayList;

/**
 * Created by CMonk on 2/6/2017.
 */

public class PreviewRecViewAdapter extends RecyclerView.Adapter<PreviewViewHolder> {
    ArrayList<PreviewData> data;
    LayoutInflater inflater;


    public PreviewRecViewAdapter(@NonNull ArrayList<PreviewData> data, @NonNull Context context) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public PreviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.inflater.inflate(R.layout.preview_cell_view, parent, false);
        return new PreviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PreviewViewHolder holder, int position) {
        PreviewData item = data.get(position);
        holder.SetThumbImage(item.thumbBitmap);
        holder.SetTitleText(item.title);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

class PreviewViewHolder extends RecyclerView.ViewHolder {
    private TextView titleLabel;
    private ImageView thumbImage;

    public PreviewViewHolder(View itemView) {
        super(itemView);

        this.titleLabel = (TextView) itemView.findViewById(R.id.preview_cell_title);
        this.thumbImage = (ImageView) itemView.findViewById(R.id.preview_cell_thumb_image);
    }

    public void SetTitleText(String str) {
        this.titleLabel.setText(str);
    }

    public void SetThumbImage(Bitmap bitmap) {
        this.thumbImage.setImageBitmap(bitmap);
    }
}
