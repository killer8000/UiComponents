package com.landenlabs.uicomponents.frag;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
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

/**
 * Demonstrate overlapping images with notch cut out of top image.
 *
 * @author Dennis Lang (LanDen Labs)
 * @see <a href="http://landenlabs.com/android/index-m.html"> author's web-site </a>
 */

public class ImageOverFrag  extends Fragment {

    View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.image_over, container, false);

        setup();
        return mRootView;
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

        if (! getRetainInstance()) {
            // Required to prevent duplicate id when Fragment re-created.
            Fragment fragment = (getFragmentManager().findFragmentById(R.id.images_over_id));
            if (fragment != null) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.remove(fragment);
                ft.commit();
            }
        }
    }

    private void setup() {

        // http://stackoverflow.com/questions/1705239/how-should-i-give-images-rounded-corners-in-android
        BitmapDrawable bmDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.paper_pink);
        Bitmap myCoolBitmap = bmDrawable.getBitmap();
        int w = myCoolBitmap.getWidth(), h = myCoolBitmap.getHeight();

        Bitmap rounder = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(rounder);

        // We're going to apply this paint eventually using a porter-duff xfer mode.
        // This will allow us to only overwrite certain pixels. RED is arbitrary. This
        // could be any color that was fully opaque (alpha = 255)
        Paint xferPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        xferPaint.setColor(Color.RED);

        boolean drawRoundedCorners = false;
        if (drawRoundedCorners) {
            // We're just reusing xferPaint to paint a normal looking rounded box, the 20.f
            // is the amount we're rounding by.
            canvas.drawRoundRect(new RectF(0, 0, w, h), 80.0f, 80.0f, xferPaint);
        }

        boolean drawNotch = true;


        float notchCenter = w * 0.50f;  // Notch center 50% from left edge.
        float notchWidth = notchCenter / 2;
        if (drawNotch) {
            Path notchPath = new Path();
            notchPath.moveTo(0, 0);
            notchPath.lineTo(notchCenter - notchWidth, 0);
            notchPath.lineTo(notchCenter, notchWidth*2);
            notchPath.lineTo(notchCenter + notchWidth, 0);
            notchPath.lineTo(w, 0);
            notchPath.lineTo(w, h);
            notchPath.lineTo(0, h);
            notchPath.close();
            canvas.drawPath(notchPath, xferPaint);
        }

        // Now we apply the 'magic sauce' to the paint
        xferPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        Bitmap result = Bitmap.createBitmap(myCoolBitmap.getWidth(), myCoolBitmap.getHeight() ,Bitmap.Config.ARGB_8888);
        Canvas resultCanvas = new Canvas(result);
        resultCanvas.drawBitmap(myCoolBitmap, 0, 0, null);

        int shadowWidth = (int)(notchWidth*2);
        Shader rshader = new LinearGradient(0, 0, 0, shadowWidth, Color.BLACK, Color.WHITE, Shader.TileMode.CLAMP);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
        paint.setShader(rshader);
        resultCanvas.drawRect(0, 0, w, shadowWidth, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
        int top = shadowWidth + 20;
        paint.setShader(new LinearGradient(0, top, 0, top+shadowWidth, Color.BLACK, Color.WHITE, Shader.TileMode.CLAMP));
        resultCanvas.drawRect(0, top, w, top + shadowWidth, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
        top += shadowWidth + 20;
        paint.setShader(new LinearGradient(0, top, 0, top+shadowWidth, Color.BLACK, Color.WHITE, Shader.TileMode.CLAMP));
        resultCanvas.drawRect(0, top, w, top + shadowWidth, paint);

        boolean drawNotchShadow = false;
        if (drawNotchShadow) {
            // This does not work, gradient does not adjust for notch.
            Path notchPath = new Path();
            notchPath.moveTo(0, 0);
            notchPath.lineTo(notchCenter - notchWidth, 0);
            notchPath.lineTo(notchCenter, notchWidth*2);
            notchPath.lineTo(notchCenter + notchWidth, 0);
            notchPath.lineTo(w, 0);
            notchPath.lineTo(w, shadowWidth);
            notchPath.lineTo(notchCenter + notchWidth, shadowWidth);
            notchPath.lineTo(notchCenter, shadowWidth + notchWidth*2);
            notchPath.lineTo(notchCenter - notchWidth, shadowWidth);
            notchPath.lineTo(0, shadowWidth);
            notchPath.close();
            resultCanvas.drawPath(notchPath, paint);
        }

        resultCanvas.drawBitmap(rounder, 0, 0, xferPaint);

        ImageView topView = Ui.viewById(mRootView, R.id.imageBot);
        topView.setImageBitmap(result);

        final View titleView = Ui.viewById(mRootView, R.id.title);
        Ui.viewById(mRootView, R.id.bottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleView.animate().alpha(1.0f - titleView.getAlpha()).start();
            }
        });
    }

}
