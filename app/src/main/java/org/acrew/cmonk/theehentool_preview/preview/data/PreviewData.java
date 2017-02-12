package org.acrew.cmonk.theehentool_preview.preview.data;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by CMonk on 2/5/2017.
 */

public class PreviewData {
    public int m_Order;
    public String m_Title;
    public String m_ThumbURL;
    public Bitmap m_ThumbBitmap;

    public PreviewData(
            int order,
            String title,
            String thumbURL,
            Bitmap thumbBitmap
    ) {
        m_Order = order;
        m_Title = title;
        m_ThumbURL = thumbURL;
        m_ThumbBitmap = thumbBitmap;
    }
}
