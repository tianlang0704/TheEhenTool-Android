package org.acrew.cmonk.theehentool_preview.common.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import org.acrew.cmonk.theehentool_preview.R;


/**
 * Created by CMonk on 2/5/2017.
 */

public class RoundCornerLinearLayout extends LinearLayout {

    Path clipPath = new Path();

    int m_ClipWidth = 0;
    int m_ClipHeight = 0;
    int m_CornerRadius = 0;
    int m_ShadowLeft = 0;
    int m_ShadowRight = 0;
    int m_ShadowTop = 0;
    int m_ShadowBottom = 0;
    int m_ShadowColor = 0;

    public RoundCornerLinearLayout(Context context) {
        super(context);
    }

    public RoundCornerLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        SetAttributes(context, attrs);
        SetBackground();
    }

    public RoundCornerLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        int newH = b - t;
        int newW = r - l;
        if (m_ClipHeight == newH && m_ClipWidth == newW) { return; }
        m_ClipHeight = newH;
        m_ClipWidth = newW;
        //Aware of the performance impact
        clipPath = new Path();
        RectF clipBounds = new RectF(0, 0, m_ClipWidth, m_ClipHeight);
        clipBounds.inset(m_ShadowRight, m_ShadowBottom);
        clipBounds.left = m_ShadowLeft;
        clipBounds.top = m_ShadowTop;
        clipPath.addRoundRect(new RectF(clipBounds), m_CornerRadius, m_CornerRadius, Path.Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }

    private void SetAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundCornerLinearLayout, 0, 0);
        try {
            m_CornerRadius =  a.getDimensionPixelSize(R.styleable.RoundCornerLinearLayout_CornerRadius, 0);
            m_ShadowLeft =  a.getDimensionPixelSize(R.styleable.RoundCornerLinearLayout_ShadowLeft, 0);
            m_ShadowTop =  a.getDimensionPixelSize(R.styleable.RoundCornerLinearLayout_ShadowTop, 0);
            m_ShadowRight =  a.getDimensionPixelSize(R.styleable.RoundCornerLinearLayout_ShadowRight, 0);
            m_ShadowBottom =  a.getDimensionPixelSize(R.styleable.RoundCornerLinearLayout_ShadowBottom, 0);
            m_ShadowColor =  a.getColor(R.styleable.RoundCornerLinearLayout_ShadowColor, 0);
        } finally {
            a.recycle();
        }
    }

    private void SetBackground() {
        int color;
        Drawable bg = getBackground();
        LayerDrawable layers;
        if (bg instanceof ColorDrawable) {
            color = ((ColorDrawable) bg).getColor();
            GradientDrawable shape = new GradientDrawable();
            shape.setColor(color);
            shape.setCornerRadius(m_CornerRadius);

            GradientDrawable shadow = new GradientDrawable();
            shadow.setColor(m_ShadowColor);
            shadow.setCornerRadius(m_CornerRadius + 6);

            layers = new LayerDrawable(new Drawable[]{shadow, shape});
            layers.setLayerInset(1, m_ShadowLeft, m_ShadowTop, m_ShadowRight, m_ShadowBottom);
            setPadding(m_ShadowLeft, m_ShadowTop, m_ShadowRight, m_ShadowBottom);
        }else if (bg instanceof LayerDrawable){
            layers = (LayerDrawable)bg;
        }else{
            layers = new LayerDrawable(new Drawable[]{bg});
        }
        setBackground(layers);
    }
}
