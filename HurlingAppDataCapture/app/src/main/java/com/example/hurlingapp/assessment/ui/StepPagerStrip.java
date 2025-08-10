package com.example.hurlingapp.assessment.ui;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

public class StepPagerStrip extends View {
    private static final int[] ATTRS = new int[]{
            android.R.attr.gravity
    };
    private int pageCount;
    private int currentPage;
    private PageSelectedListener pageSelectedListener;

    private int gravity = Gravity.LEFT | Gravity.TOP;
    private float tabbedWidth;
    private float tabbedHeight;
    private float tabbedSpacing;

    private Paint prevTabbedPaint;
    private Paint selectedTabbedPaint;
    private Paint selectedLastTabbedPaint;
    private Paint nextTabbedPaint;

    private RectF mTempRectF = new RectF();

    public StepPagerStrip(Context context) {
        this(context, null, 0);
    }

    public StepPagerStrip(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public StepPagerStrip(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);

        final TypedArray a = context.obtainStyledAttributes(attributeSet, ATTRS);
        gravity = a.getInteger(0, gravity);
        a.recycle();

        final float density = getResources().getDisplayMetrics().density;
        tabbedWidth = 32 * density;
        tabbedHeight = 4 * density;
        tabbedSpacing = 4 * density;

        prevTabbedPaint = new Paint();
        prevTabbedPaint.setColor(Color.parseColor("#FF008001"));

        selectedTabbedPaint = new Paint();
        selectedTabbedPaint.setColor(Color.parseColor("#FFFF8800"));

        selectedLastTabbedPaint = new Paint();
        selectedLastTabbedPaint.setColor(Color.parseColor("#FFFF8800"));

        nextTabbedPaint = new Paint();
        nextTabbedPaint.setColor(Color.parseColor("#FF333333"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (pageCount == 0) {
            return;
        }

        float totalWidth = pageCount * (tabbedWidth + tabbedSpacing) - tabbedSpacing;
        float totalLeft;
        boolean fillHorizontal = false;

        switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
            case Gravity.CENTER_HORIZONTAL:
                totalLeft = (getWidth() - totalWidth) / 2;
                break;
            case Gravity.RIGHT:
                totalLeft = getWidth() - getPaddingRight() - totalWidth;
                break;
            case Gravity.FILL_HORIZONTAL:
                totalLeft = getPaddingLeft();
                fillHorizontal = true;
                break;
            default:
                totalLeft = getPaddingLeft();
        }

        switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
            case Gravity.CENTER_VERTICAL:
                mTempRectF.top = (int) (getHeight() - tabbedHeight) / 2;
                break;
            case Gravity.BOTTOM:
                mTempRectF.top = getHeight() - getPaddingBottom() - tabbedHeight;
                break;
            default:
                mTempRectF.top = getPaddingTop();
        }

        mTempRectF.bottom = mTempRectF.top + tabbedHeight;

        float tabWidth = tabbedWidth;
        if (fillHorizontal) {
            tabWidth = (getWidth() - getPaddingRight() - getPaddingLeft()
                    - (pageCount - 1) * tabbedSpacing) / pageCount;
        }

        for (int i = 0; i < pageCount; i++) {
            mTempRectF.left = totalLeft + (i * (tabWidth + tabbedSpacing));
            mTempRectF.right = mTempRectF.left + tabWidth;
            canvas.drawRect(mTempRectF, i < currentPage
                    ? prevTabbedPaint
                    : (i > currentPage
                            ? nextTabbedPaint
                            : (i == pageCount - 1
                                    ? selectedLastTabbedPaint
                                    : selectedTabbedPaint)));
        }
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                View.resolveSize(
                        (int) (pageCount * (tabbedWidth + tabbedSpacing) - tabbedSpacing)
                                + getPaddingLeft() + getPaddingRight(),
                        widthMeasureSpec),
                View.resolveSize(
                        (int) tabbedHeight
                                + getPaddingTop() + getPaddingBottom(),
                        heightMeasureSpec));
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getPageSelectedListener() != null) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    int position = hitTest(event.getX());
                    if (position >= 0) {
                    	getPageSelectedListener().onPageStripSelected(position);
                    }
                    return true;
            }
        }
        return super.onTouchEvent(event);
    }
    
    private int hitTest(float x) {
        if (pageCount == 0) {
            return -1;
        }

        float totalWidth = pageCount * (tabbedWidth + tabbedSpacing) - tabbedSpacing;
        float totalLeft;
        boolean fillHorizontal = false;

        switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
            case Gravity.CENTER_HORIZONTAL:
                totalLeft = (getWidth() - totalWidth) / 2;
                break;
            case Gravity.RIGHT:
                totalLeft = getWidth() - getPaddingRight() - totalWidth;
                break;
            case Gravity.FILL_HORIZONTAL:
                totalLeft = getPaddingLeft();
                fillHorizontal = true;
                break;
            default:
                totalLeft = getPaddingLeft();
        }

        float tabWidth = tabbedWidth;
        if (fillHorizontal) {
            tabWidth = (getWidth() - getPaddingRight() - getPaddingLeft()
                    - (pageCount - 1) * tabbedSpacing) / pageCount;
        }

        float totalRight = totalLeft + (pageCount * (tabWidth + tabbedSpacing));
        if (x >= totalLeft && x <= totalRight && totalRight > totalLeft) {
            return (int) (((x - totalLeft) / (totalRight - totalLeft)) * pageCount);
        } else {
            return -1;
        }
    }

    public void setCurrentPage(int position) {
        currentPage = position;
        invalidate();
    }

    public void setPageCount(int count) {
        pageCount = count;
        invalidate();
    }

	public PageSelectedListener getPageSelectedListener() {
		return pageSelectedListener;
	}

	public void setPageSelectedListener(PageSelectedListener pageSelectedListener) {
		this.pageSelectedListener = pageSelectedListener;
	}
}
