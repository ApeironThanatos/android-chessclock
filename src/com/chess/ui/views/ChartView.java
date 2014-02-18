package com.chess.ui.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import com.chess.R;
import com.chess.utilities.FontsHelper;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: roger sent2roger@gmail.com
 * Date: 07.02.13
 * Time: 19:28
 */
public class ChartView extends View {

	public static final int TIME = 0;
	public static final int VALUE = 1;

	public static final long MILLISECONDS_PER_DAY = 86400 * 1000;

	private Paint graphPaint;
	private Path graphPath;
	//	private int widthPixels;
	private float yAspect;
	private int backColor;
	private Paint borderPaint;
	private int minY;
	private int maxY;
	private float density;
	private int graphTopColor;
	private int graphBottomColor;
	private Rect clipBounds;
	private SparseArray<Long> pointsArray;
	private SparseBooleanArray pointsExistArray;
	private boolean initialized;
	private int originalMinY;
	private Paint minValueLinePaint;
	private Paint strokePaint;
	private Paint minValueTextPaint;
	private int textOffset;
	private float borderWidth;
	private Rect rect;

	public ChartView(Context context) {
		super(context);
		init(context);
	}

	public ChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		Resources resources = context.getResources();
		density = resources.getDisplayMetrics().density;

		clipBounds = new Rect();
		borderWidth = 1 * density;

		// set colors
		int borderColor = resources.getColor(R.color.graph_border);
		backColor = resources.getColor(R.color.graph_back);
		graphTopColor = resources.getColor(R.color.graph_gradient_top);
		graphBottomColor = resources.getColor(R.color.graph_gradient_bottom);

		// Border
		borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		borderPaint.setColor(borderColor);
		borderPaint.setStyle(Paint.Style.STROKE);
		borderPaint.setStrokeWidth(1.5f * density);

		// Minimal Rating line
		int minValueLineColor = resources.getColor(R.color.graph_min_value_line);
		minValueLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		minValueLinePaint.setColor(minValueLineColor);
		minValueLinePaint.setStyle(Paint.Style.STROKE);
		minValueLinePaint.setStrokeWidth(density);

		// Minimal Rating value
		int minValueTextColor = resources.getColor(R.color.graph_min_value_text);
		int textSize = (int) (13 * density);
		minValueTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		minValueTextPaint.setColor(minValueTextColor);
		minValueTextPaint.setStyle(Paint.Style.FILL);
		minValueTextPaint.setStrokeWidth(density);
		minValueTextPaint.setTextSize(textSize);  // TODO adjust for tablets
		minValueTextPaint.setTypeface(FontsHelper.getInstance().getTypeFace(getContext(), FontsHelper.DEFAULT_FONT));

		textOffset = (int) (16 * density);

		rect = new Rect();
	}


	@Override
	protected void onDraw(Canvas canvas) {
		canvas.getClipBounds(clipBounds);
		int bottom = clipBounds.bottom;
		int width = clipBounds.right;
		canvas.drawColor(backColor);

		if (initialized) {
			createGraphPath(canvas);
			canvas.getClipBounds(rect);

			int height = rect.bottom;

			float yValue = height - (originalMinY - minY) / yAspect + 1; // 1px offset below line
			canvas.drawLine(0, yValue, width, yValue, minValueLinePaint);
			canvas.drawText(String.valueOf(originalMinY), 0, yValue + textOffset, minValueTextPaint);
		} else {
			canvas.drawText("No Data :(", 0, 0, borderPaint);
		}

		// draw grey borders
		// Top Line
		canvas.drawLine(0, 0, width, 0, borderPaint);
		// Left line
		canvas.drawLine(0, 0, 0, bottom, borderPaint);
		// Right Line
		canvas.drawLine(width, 0, width, bottom, borderPaint);
		// Top Line
		canvas.drawLine(0, bottom, width, bottom, borderPaint);

	}

	private void logTest(String string) {
		Log.d("TEST", string);
	}

	public void setGraphData(List<long[]> series, int width) {
		setPoints(series, width);
		invalidate(0, 0, getWidth(), getHeight());
	}

	private void setPoints(List<long[]> dataArray, int widthPixels) {
		{ // get min and max of X values
			graphPath = null;
			// truncate hours and minutes and seconds from date
			long firstPoint = dataArray.get(0)[TIME] - dataArray.get(0)[TIME] % MILLISECONDS_PER_DAY;

			long lastPoint = System.currentTimeMillis();
			lastPoint -= lastPoint % MILLISECONDS_PER_DAY;

			// distribute timestamps at whole width
			long xDiff = lastPoint - firstPoint;
			long xPointStep = xDiff / widthPixels;
//			logTest("firstPoint = " + firstPoint + " lastPoint = " + lastPoint + " xDiff = " + xDiff + " xPointStep = " + xPointStep);

			// convert xPointStep to optimal day{time} difference
			pointsArray = new SparseArray<Long>();
			pointsExistArray = new SparseBooleanArray();

			for (int i = 0; i < widthPixels; i++) {
				long timestampValue = firstPoint + i * xPointStep;
				timestampValue -= timestampValue % MILLISECONDS_PER_DAY;

				boolean found = false;
				long graphTimestamp;
				for (long[] aDataArray : dataArray) {
					graphTimestamp = aDataArray[TIME] - aDataArray[TIME] % MILLISECONDS_PER_DAY;

					long rating = aDataArray[VALUE];
//					logTest(" point at i = " + i + " added = "
//							+ ((timestampValue - graphTimestamp) >= 0 && (timestampValue - graphTimestamp) < (xPointStep * 2)));
//					logTest(" graphTimestamp = " + graphTimestamp);
					if ((timestampValue - graphTimestamp) >= 0 && (timestampValue - graphTimestamp) < (xPointStep * 2)) {
						pointsArray.put(i, rating);
						found = true;
						break;
					}
				}

				pointsExistArray.put(i, found);
			}
		}

		{// get min and max of Y values
			minY = Integer.MAX_VALUE;
			maxY = Integer.MIN_VALUE;

			for (long[] longs : dataArray) {
				int yValue = (int) longs[VALUE];
				minY = Math.min(minY, yValue);
				maxY = Math.max(maxY, yValue);
			}
			originalMinY = minY;
		}
//		logTest("minY = " + minY + " maxY = " + maxY + " originalMinY = " + originalMinY);

		initialized = true;
	}

	private void createGraphPath(Canvas canvas) {
		if (graphPath == null) {
			int height = canvas.getClipBounds().bottom;
			int width = canvas.getClipBounds().right;
			int originalMaxY = maxY;
			minY -= 200;
			maxY += 400;

			int diff = maxY - minY;

			yAspect = (float) (diff / height);

			graphPath = createGraphPath(height, width);

			graphPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			graphPaint.setAntiAlias(true);
			int gradientYStart = (int) (height - (originalMaxY - minY) / yAspect);
			LinearGradient shader = new LinearGradient(0, gradientYStart, 0, height, graphTopColor, graphBottomColor, Shader.TileMode.CLAMP);
			graphPaint.setShader(shader);

			strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			strokePaint.setAntiAlias(true);
			strokePaint.setStyle(Paint.Style.STROKE);
			strokePaint.setStrokeWidth(1.5f * density);
			strokePaint.setColor(0xFF88b2cc);
		}

		// draw Graph
		canvas.drawPath(graphPath, graphPaint);
		canvas.drawPath(graphPath, strokePaint);
	}

	private Path createGraphPath(int height, int widthPixels) {
		Path path = new Path();

		Long startPoint = pointsArray.get(0);
		if (startPoint == null) {// this might be caused by empty data array on the input
			startPoint = 1200L;
		}
		long yValue = startPoint - minY;
		path.moveTo(0, height - yValue / yAspect);
		path.lineTo(0, height - yValue / yAspect);
		for (int i = 0; i < widthPixels; i++) {
			if (pointsExistArray.get(i)) {
				yValue = pointsArray.get(i) - minY;
//				logTest("yValue = " + yValue + " minY = " + minY + " height = " + height + " yAspect = " + yAspect);
			}
			path.lineTo(i, height - yValue / yAspect);
		}

		path.lineTo(widthPixels, height);
		path.lineTo(-10, height);
		path.close();

		return path;
	}
}
