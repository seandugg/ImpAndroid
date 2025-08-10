package com.example.hurlingapp.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class PitchView extends View {

    private List<PointF> tapLocations = new ArrayList<PointF>();
    private Paint tapPaint;

    public PitchView(Context context) {
        super(context);
        init();
    }

    public PitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        tapPaint = new Paint();
        tapPaint.setColor(Color.RED);
        tapPaint.setStyle(Paint.Style.FILL);
    }

    public void addTap(PointF tap) {
        tapLocations.add(tap);
        invalidate(); // Redraw the view
    }

    public void clearTaps() {
	tapLocations.clear();
	invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw a green background as a placeholder for the pitch
        canvas.drawColor(Color.parseColor("#008001"));

        for (PointF tap : tapLocations) {
            canvas.drawCircle(tap.x, tap.y, 10, tapPaint);
        }
    }
}
