
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
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.landenlabs.uicomponents.R;
import com.landenlabs.uicomponents.Ui;
import com.landenlabs.uicomponents.Util.BitmapUtils;
import com.landenlabs.uicomponents.Util.RenderScriptUtils;


/**
 * Demonstrate view shadows.
 *
 * @author Dennis Lang (LanDen Labs)
 * @see <a href="http://landenlabs.com/android/index-m.html"> author's web-site </a>
 */

public class RenderScriptFrag  extends UiFragment
        implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    View mRootView;
    ViewGroup mImageScroller;
    
    TextView mRadiusTv, mScaleTv;
    SeekBar mRadiusSb, mScaleSb;

    ImageView mImage1, mImage2, mImage3;
    TextView mTime1Tv, mTime2Tv;

    final int mSeekMax = 255;
    final int mMaxRadius = 100;
    int mRadius = 10;

    final int mMaxScale = 100;
    int mScale = 10;

    RenderScriptUtils.Blur mBlur;
    Bitmap  mSrcBitmap;

    public  static int ArrayFind(int[] array, int find) {
        for (int idx = 0; idx < array.length; idx++){
            if (array[idx] == find)
                return idx;
        }

        return -1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.renderscript_frag, container, false);

        setup();
        return mRootView;
    }

    @Override
    public int getFragId() {
        return R.id.renderscript_id;
    }

    @Override
    public String getName() {
        return "RenderScript";
    }

    @Override
    public String getDescription() {
        return "RS Blur";
    }


    private void setup() {
        final View titleView = Ui.viewById(mRootView, R.id.title);
        
        mImageScroller = Ui.viewById(mRootView, R.id.rs_scroller);
        mTime1Tv = Ui.viewById(mRootView, R.id.rs_blur1Time);
        mTime2Tv = Ui.viewById(mRootView, R.id.rs_blur2Time);
        
        Ui.viewById(mRootView, R.id.rs_image1rb).setOnClickListener(this);
        Ui.viewById(mRootView, R.id.rs_image2rb).setOnClickListener(this);
        Ui.viewById(mRootView, R.id.rs_image3rb).setOnClickListener(this);
        Ui.viewById(mRootView, R.id.rs_image4rb).setOnClickListener(this);
        Ui.viewById(mRootView, R.id.rs_image5rb).setOnClickListener(this);
        Ui.viewById(mRootView, R.id.rs_image6rb).setOnClickListener(this);

        mImage1 = Ui.viewById(mRootView, R.id.rs_image1);
        mImage2 = Ui.viewById(mRootView, R.id.rs_image2);
        mImage3 = Ui.viewById(mRootView, R.id.rs_image3);

        mRadiusSb = Ui.viewById(mRootView, R.id.rs_radius_sb);
        mRadiusTv = Ui.viewById(mRootView, R.id.rs_radius_tv);

        mScaleSb = Ui.viewById(mRootView, R.id.rs_scale_sb);
        mScaleTv = Ui.viewById(mRootView, R.id.rs_scale_tv);

        setPosSb(mRadiusSb, mRadius, mMaxRadius);
        mRadiusSb.setOnSeekBarChangeListener(this);

        setPosSb(mScaleSb, mScale, mMaxScale);
        mScaleSb.setOnSeekBarChangeListener(this);

        updateView();
    }

    private void setPosSb(SeekBar seekBar, int value, int maxValue) {
        // int iVal = (int)((maxXYZ/2 - value) * mSeekMax/maxXYZ);
        int iVal = value * mSeekMax / maxValue;
        seekBar.setProgress(iVal);
    }
    private int getPosSb(SeekBar seekBar, int maxValue) {
        // return maxXYZ/2 - seekBar.getProgress() *  maxXYZ / mSeekMax;
        return seekBar.getProgress() * maxValue / mSeekMax;
    }




    private void updateView() {

        mRadius = getPosSb(mRadiusSb, mMaxRadius);
        mRadiusTv.setText(String.format("Radius:%d", mRadius));

        mScale = getPosSb(mScaleSb, mMaxScale);
        mScaleTv.setText(String.format("Scale:%d %%", mScale));

        WindowManager wm = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int displayWidth = size.x;
        int displayHeight = size.y;

        ViewGroup.LayoutParams lp =  mImageScroller.getLayoutParams();
        lp.width = displayWidth * mScale / 100;
        mImageScroller.setLayoutParams(lp);

        if (mBlur == null) {
            mBlur = new RenderScriptUtils.Blur(this.getContext());
            mSrcBitmap = ((BitmapDrawable)mImage1.getDrawable()).getBitmap();
        }

        long startNano = System.nanoTime();
        Bitmap blurred2 = mBlur.blur(mSrcBitmap, mRadius);
        mImage2.setImageBitmap(blurred2);
        long blur1Nano = System.nanoTime() - startNano;
        mTime1Tv.setText(String.format("Blur1: %,.2f Msec", blur1Nano / 1000.0f));

        try {
            startNano = System.nanoTime();
            Bitmap blurred3 = BitmapUtils.createBlurBitmap(this.getContext(), mSrcBitmap, mRadius);
            mImage3.setImageBitmap(blurred3);
        } catch (Exception ex) {
            mImage3.setImageDrawable(new ColorDrawable(0xffff0000));
        }
        long blur2Nano = System.nanoTime() - startNano;
        mTime2Tv.setText(String.format("Blur2: %,.2f Msec", blur2Nano / 1000.0f));
    }


    // =============================================================================================
    // Seekbar onProgressChanged

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        updateView();
    }
    public void onStartTrackingTouch(SeekBar seekBar) {
    }
    public void onStopTrackingTouch(SeekBar seekBar)  {
    }

    // =============================================================================================
    // View  onClick


    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.rs_image1rb:
                mImage1.setImageResource(R.drawable.blur1);
                break;

            case R.id.rs_image2rb:
                mImage1.setImageResource(R.drawable.blur2);
                break;

            case R.id.rs_image3rb:
                mImage1.setImageResource(R.drawable.blur3);
                break;

            case R.id.rs_image4rb:
                mImage1.setImageResource(R.drawable.blur4);
                break;

            case R.id.rs_image5rb:
                mImage1.setImageResource(R.drawable.blur5);
                break;

            case R.id.rs_image6rb:
                mImage1.setImageResource(R.drawable.blur6);
                break;
        }

        mSrcBitmap = ((BitmapDrawable)mImage1.getDrawable()).getBitmap();

        updateView();
    }
}


