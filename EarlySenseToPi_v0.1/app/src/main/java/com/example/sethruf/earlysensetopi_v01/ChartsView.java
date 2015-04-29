package com.example.sethruf.earlysensetopi_v01;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Seth.Ruf on 29/04/2015.
 */
public class ChartsView extends LinearLayout {

    private ScaleGestureDetector pinchZoom;

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            Log.d("Scale Zoom", "" + detector.getCurrentSpan());
            return true;
        }
    }

    public ChartsView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        pinchZoom = new ScaleGestureDetector(this.getContext(), new ScaleListener());

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.charts_view, this, true);
    }

    public ChartsView(Context context) {
        this(context, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        pinchZoom.onTouchEvent(event);
        return false;
    }
}
