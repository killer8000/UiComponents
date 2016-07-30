
/**
 * Copyright (c) 2015 Dennis Lang (LanDen Labs) landenlabs@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 * NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Dennis Lang  (3/21/2015)
 * @see http://landenlabs.com
 *
 */
package com.landenlabs.uicomponents.frag;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.landenlabs.uicomponents.R;
import com.landenlabs.uicomponents.Ui;

import java.util.ArrayList;


// import javax.microedition.khronos.opengles.GL10;


/**
 * Demonstrate Scrollview resizing with views above and below.
 *
 * @author Dennis Lang (LanDen Labs)
 * @see <a href="http://landenlabs.com/android/index-m.html"> author's web-site </a>
 */

public class GraphLineFrag  extends UiFragment implements View.OnClickListener {

    View mRootView;
    ViewGroup mGraphView;

    // =============================================================================================
    public static class LineView extends View {

        float mXScale = 5;
        float mLineWidth = 2;

        ArrayList<Float> mPoints = new ArrayList<>();

        public LineView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStrokeWidth(mLineWidth);
            paint.setStyle(Paint.Style.STROKE);

            if (!mPoints.isEmpty()) {
                Path path = new Path();
                path.moveTo(0, mPoints.get(0));

                for (int idx = 1; idx < mPoints.size(); idx++) {
                    path.lineTo(idx * mXScale, mPoints.get(idx));
                }

                canvas.drawPath(path, paint);
            }
        }
    }

    // =============================================================================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.graphline_frag, container, false);

        setup();
        return mRootView;
    }

    @Override
    public int getFragId() {
        return R.id.graphline_id;
    }

    @Override
    public String getName() {
        return "GraphLine";
    }

    @Override
    public String getDescription() {
        return "??";
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            default:
                break;
        }
    }

    LineView mLineView;
    Runnable mAddPoints;

    private void setup() {
        mGraphView = Ui.viewById(mRootView, R.id.graph_line_surfaceview);

        LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);

        mGraphView.removeAllViews();

        TextView tv = new TextView(mGraphView.getContext());
        tv.setGravity(Gravity.CENTER);
        tv.setText("Line Graph");
        tv.setTextColor(0xffff0000);
        tv.setTextSize(20.0f);
        ViewParent p1 = tv.getParent();
        mGraphView.addView(tv);

        final int updateMill = 50;
        final int maxPoints = 100;
        final int maxRange = 400;
        try {
            mLineView = new LineView(mGraphView.getContext());
            mLineView.setBackgroundColor(0xffd0d0d0);
            mGraphView.addView(mLineView, lp);

            mAddPoints = new Runnable() {
                @Override
                public void run() {
                    if (mLineView.mPoints.size() > maxPoints) {
                        mLineView.mPoints.remove(0);
                    }
                    mLineView.mPoints.add((float)(Math.random()*maxRange));
                    mLineView.invalidate();
                    mLineView.postDelayed(this, updateMill);
                }
            };
            mLineView.postDelayed(mAddPoints, updateMill);

        } catch (Exception ex) {
            Log.e("GraphLineFrag", ex.getLocalizedMessage(), ex);
        }

        // Ui.viewById(mRootView, R.id.scroll_add).setOnClickListener(this);
    }
}
