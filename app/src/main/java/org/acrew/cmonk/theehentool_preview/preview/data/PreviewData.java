package org.acrew.cmonk.theehentool_preview.preview.data;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by CMonk on 2/5/2017.
 */

public class PreviewData {
    public int order;
    public String title;
    public String thumbURL;
    public Bitmap thumbBitmap;

    public PreviewData(
            int order,
            String title,
            String thumbURL,
            Bitmap thumbBitmap
    ) {
        this.order = order;
        this.title = title;
        this.thumbURL = thumbURL;
        this.thumbBitmap = thumbBitmap;
    }
}
