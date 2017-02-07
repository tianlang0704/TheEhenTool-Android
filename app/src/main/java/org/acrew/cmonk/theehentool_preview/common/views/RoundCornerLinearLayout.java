package org.acrew.cmonk.theehentool_preview.common.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import org.acrew.cmonk.theehentool_preview.R;

import java.util.ArrayList;


/**
 * Created by CMonk on 2/5/2017.
 */

public class RoundCornerLinearLayout extends LinearLayout {

    int cornerRadius = 0;
    int shadowLeft = 0;
    int shadowRight = 0;
    int shadowTop = 0;
    int shadowBottom = 0;
    int shadowColor = 0;

    public RoundCornerLinearLayout(Context context) {
        super(context);
    }

    public RoundCornerLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.SetAttributes(context, attrs);
        this.SetBackground();
    }

    public RoundCornerLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Awaring of the performance impact
        Path clipPath = new Path();
        Rect clipBounds = new Rect();
        canvas.getClipBounds(clipBounds);
        clipBounds.inset(this.shadowRight, this.shadowBottom);
        clipBounds.left = this.shadowLeft;
        clipBounds.top = this.shadowTop;
        clipPath.addRoundRect(new RectF(clipBounds), this.cornerRadius, this.cornerRadius, Path.Direction.CW);

        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }

    private void SetAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundCornerLinearLayout, 0, 0);
        try {
            this.cornerRadius =  a.getDimensionPixelSize(R.styleable.RoundCornerLinearLayout_CornerRadius, 0);
            this.shadowLeft =  a.getDimensionPixelSize(R.styleable.RoundCornerLinearLayout_ShadowLeft, 0);
            this.shadowTop =  a.getDimensionPixelSize(R.styleable.RoundCornerLinearLayout_ShadowTop, 0);
            this.shadowRight =  a.getDimensionPixelSize(R.styleable.RoundCornerLinearLayout_ShadowRight, 0);
            this.shadowBottom =  a.getDimensionPixelSize(R.styleable.RoundCornerLinearLayout_ShadowBottom, 0);
            this.shadowColor =  a.getColor(R.styleable.RoundCornerLinearLayout_ShadowColor, 0);
        } finally {
            a.recycle();
        }
    }

    private void SetBackground() {
        int color;
        Drawable bg = this.getBackground();
        LayerDrawable layers;
        if (bg instanceof ColorDrawable) {
            color = ((ColorDrawable) bg).getColor();
            GradientDrawable shape = new GradientDrawable();
            shape.setColor(color);
            shape.setCornerRadius(this.cornerRadius);

            GradientDrawable shadow = new GradientDrawable();
            shadow.setColor(this.shadowColor);
            shadow.setCornerRadius(this.cornerRadius + 6);

            layers = new LayerDrawable(new Drawable[]{shadow, shape});
            layers.setLayerInset(1, this.shadowLeft, this.shadowTop, this.shadowRight, this.shadowBottom);
            this.setPadding(this.shadowLeft, this.shadowTop, this.shadowRight, this.shadowBottom);
        }else if (bg instanceof LayerDrawable){
            layers = (LayerDrawable)bg;
        }else{
            layers = new LayerDrawable(new Drawable[]{bg});
        }
        this.setBackground(layers);
    }
}
