package com.medarkive.Main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.medarkive.R;

public class CircleChartView extends View {

	public CircleChartView(Context context) {
		super(context);
		init();
	}

	public CircleChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CircleChartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		paint = new Paint();
		paint.setColor(getContext().getResources().getColor(R.color.cyan));
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(15);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		bgpaint = new Paint();
		bgpaint.setColor(getContext().getResources().getColor(
				R.color.custom_gray));
		bgpaint.setAntiAlias(true);
		bgpaint.setStyle(Paint.Style.STROKE);
		bgpaint.setStrokeWidth(15);
		bgpaint.setStrokeJoin(Paint.Join.ROUND);
		bgpaint.setStrokeCap(Paint.Cap.ROUND);

		rect = new RectF();
	}

	Paint paint;
	Paint bgpaint;
	RectF rect;
	float percentage = 0;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// draw background circle anyway
		int left = 7;
		int width = getWidth()-15;
		int top =7;
		rect.set(left, top, left+width, top + width); 
        canvas.drawArc(rect, -90, 360, false, bgpaint);
        if(percentage!=0) {
            canvas.drawArc(rect, -90, (360*percentage), false, paint);
        }
	}

	public void setPercentage(float percentage) {
		this.percentage = percentage / 100;
		invalidate();
	}

}
