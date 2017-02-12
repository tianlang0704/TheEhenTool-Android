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
    ArrayList<PreviewData> m_Data;
    LayoutInflater m_Inflater;


    public PreviewRecViewAdapter(@NonNull ArrayList<PreviewData> data, @NonNull Context context) {
        this.m_Data = data;
        this.m_Inflater = LayoutInflater.from(context);
    }

    @Override
    public PreviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.m_Inflater.inflate(R.layout.preview_cell_view, parent, false);
        return new PreviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PreviewViewHolder holder, int position) {
        PreviewData item = m_Data.get(position);
        holder.SetThumbImage(item.m_ThumbBitmap);
        holder.SetTitleText(item.m_Title);
    }

    @Override
    public int getItemCount() {
        return m_Data.size();
    }
}

class PreviewViewHolder extends RecyclerView.ViewHolder {
    private TextView m_TitleLabel;
    private ImageView m_ThumbImage;

    public PreviewViewHolder(View itemView) {
        super(itemView);

        this.m_TitleLabel = (TextView) itemView.findViewById(R.id.preview_cell_title);
        this.m_ThumbImage = (ImageView) itemView.findViewById(R.id.preview_cell_thumb_image);
    }

    public void SetTitleText(String str) {
        this.m_TitleLabel.setText(str);
    }

    public void SetThumbImage(Bitmap bitmap) {
        this.m_ThumbImage.setImageBitmap(bitmap);
    }
}
