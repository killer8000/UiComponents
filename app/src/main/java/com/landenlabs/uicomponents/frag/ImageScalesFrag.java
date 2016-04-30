package com.landenlabs.uicomponents.frag;

import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.landenlabs.uicomponents.R;
import com.landenlabs.uicomponents.Ui;

/**
 * Copyright (c) 2015 Dennis Lang (LanDen Labs) landenlabs@gmail.com
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 * NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Dennis Lang  (3/21/2015)
 * @see http://landenlabs.com
 */

/**
 * Demonstrate Image Scale modes.
 *
 * @author Dennis Lang (LanDen Labs)
 * @see <a href="http://landenlabs.com/android/index-m.html"> author's web-site </a>
 */

public class ImageScalesFrag  extends Fragment implements View.OnClickListener  {

    View mRootView;
    ViewGroup mImageHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.image_scales, container, false);

        setup();
        return mRootView;
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

        if (! getRetainInstance()) {
            // Required to prevent duplicate id when Fragment re-created.
            Fragment fragment = (getFragmentManager().findFragmentById(R.id.image_scales_id));
            if (fragment != null) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.remove(fragment);
                ft.commit();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_size_50:
                setImage(R.drawable.image50);
                break;
            case R.id.image_size_200:
                setImage(R.drawable.image200);
                break;
            case R.id.image_size_400:
                setImage(R.drawable.image400);
                break;
        }
    }

    ImageView mUpperLeftFill;
    ImageView mLowerLeftFill;

    private void setup() {
        mImageHolder = Ui.viewById(mRootView, R.id.image_holder);
        mUpperLeftFill = Ui.viewById(mRootView, R.id.image_upperLeftFill);
        mLowerLeftFill = Ui.viewById(mRootView, R.id.image_lowerLeftFill);

        Ui.viewById(mRootView, R.id.image_size_50).setOnClickListener(this);
        Ui.viewById(mRootView, R.id.image_size_200).setOnClickListener(this);
        Ui.viewById(mRootView, R.id.image_size_400).setOnClickListener(this);

        mImageHolder.post(new Runnable() {
            @Override
            public void run() {
                setImage(R.drawable.image200);
            }
        });

    }

    private void setImage(int imageRes) {
        for (int idx = 0; idx < mImageHolder.getChildCount(); idx++) {
            View view = mImageHolder.getChildAt(idx);
            if (view instanceof ImageView) {
                ImageView imageView = (ImageView)view;
                if (imageView.getScaleType() != ImageView.ScaleType.MATRIX) {
                    ((ImageView) view).setImageResource(imageRes);
                }
            }
        }

        fillUpperLeft(getResources().getDrawable(imageRes), mUpperLeftFill);
        fillLowerLeft(getResources().getDrawable(imageRes), mLowerLeftFill);
    }

    private void fillUpperLeft(final Drawable drawable, ImageView imageView) {

        // Compute  matrix to fill viewer with drawable starting with upper left
        // Stretching till 2nd edge is crossed (filling screen) with correct aspect ratio.
        Matrix m = new Matrix();
        m.reset();
        int imgWidth = drawable.getIntrinsicWidth();
        int imgHeight = drawable.getIntrinsicHeight();
        int viewWidth = imageView.getWidth();
        int viewHeight = imageView.getHeight();
        float xScale = (float)viewWidth / imgWidth ;
        float yScale = (float)viewHeight / imgHeight;
        float maxScale = Math.max(xScale,  yScale);

        m.postScale(maxScale, maxScale);

        imageView.setScaleType(ImageView.ScaleType.MATRIX);
        imageView.setImageMatrix(m);
        imageView.setImageDrawable(drawable);
    }

    private void fillLowerLeft(final Drawable drawable, ImageView imageView) {

        // Compute  matrix to fill viewer with drawable starting with upper left
        // Stretching till 2nd edge is crossed (filling screen) with correct aspect ratio.
        Matrix m = new Matrix();
        m.reset();
        int imgWidth = drawable.getIntrinsicWidth();
        int imgHeight = drawable.getIntrinsicHeight();
        int viewWidth = imageView.getWidth();
        int viewHeight = imageView.getHeight();
        float xScale = (float)viewWidth / imgWidth ;
        float yScale = (float)viewHeight / imgHeight;
        float maxScale = Math.max(xScale,  yScale);

        float dy = (imgHeight * maxScale - viewHeight) / maxScale;
        // dy = imgHeight / 2;
        m.preTranslate(0, -dy);
        m.postScale(maxScale, maxScale);

        imageView.setScaleType(ImageView.ScaleType.MATRIX);
        imageView.setImageMatrix(m);
        imageView.setImageDrawable(drawable);
    }


}
