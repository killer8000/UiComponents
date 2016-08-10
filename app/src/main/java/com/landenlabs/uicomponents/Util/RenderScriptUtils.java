package com.landenlabs.uicomponents.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RSRuntimeException;
import android.support.v8.renderscript.RenderScript;
import android.util.Log;

import com.landenlabs.uicomponents.R;



/**
 * Bitmap pixel filters, such as blur and tint.
 *
 * @author dlang
 *
 */
public class RenderScriptUtils {
    private static final String TAG = RenderScriptUtils.class.getSimpleName();

    // ------------------------------------------------------------------------------------------
    /**
     * Box image blur using RenderScript
     *
     * Found on web - by Tobias Ritzau
     * http://stackoverflow.com/questions/13435561/android-blur-bitmap-instantly
     *
     */
    public static class Blur {

        private RenderScript mRS;

        private ScriptC_horzblur mHorizontalScript;
        private ScriptC_vertblur mVerticalScript;

        public Blur(Context context) {
            mRS = RenderScript.create(context);

            mHorizontalScript = new ScriptC_horzblur(mRS, mRS.getApplicationContext()
                    .getResources(), R.raw.horzblur);
            mVerticalScript = new ScriptC_vertblur(mRS, mRS.getApplicationContext().getResources(),
                    R.raw.vertblur);
        }

        public Bitmap blur(Bitmap src, int radius) {
            Bitmap dst = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());

            try {
                blur(src, dst, radius);
            } catch (RSRuntimeException ex) {
                Log.e(TAG, "Blur :: renderScript crashed", ex);
            } catch (Exception ex) {
                Log.e(TAG, "Blur :: renderScript crashed", ex);
            }
            return dst;
        }

        private void hblur(int radius, int width, int height, Allocation rowIndexes, Allocation in, Allocation out) {
            mHorizontalScript.set_Radius(radius);
            // mHorizontalScript.set_xDim(width);
            // mHorizontalScript.set_yDim(height);
            // mHorizontalScript.set_numSamples(2 * radius +1);
            // mHorizontalScript.set_invN(1.0f / mHorizontalScript.get_numSamples());
            mHorizontalScript.bind_inImage(in);
            mHorizontalScript.bind_outImage(out);
            mHorizontalScript.invoke_init_calc();
            mHorizontalScript.forEach_root(rowIndexes);
        }

        private void vblur(int radius, int width, int height, Allocation columnIndexes, Allocation in, Allocation out) {
            mVerticalScript.set_Radius(radius);
            // mVerticalScript.set_xDim(width);
            // mVerticalScript.set_yDim(height);
            // mVerticalScript.set_numSamples(2 * radius +1);
            // mVerticalScript.set_invN(1.0f / mVerticalScript.get_numSamples());
            mVerticalScript.bind_inImage(in);
            mVerticalScript.bind_outImage(out);
            mVerticalScript.invoke_init_calc();
            mVerticalScript.forEach_root(columnIndexes);
        }

        // Return array populated with values 0..size-1
        private Allocation createIndex(int size) {
            Element element = Element.U16(mRS);
            Allocation allocation = Allocation.createSized(mRS, element, size);
            short[] indexes = new short[size];
            for (int idx = 0; idx < indexes.length; idx++)
                indexes[idx] = (short) idx;
            allocation.copyFrom(indexes);

            return allocation;
        }

        private void blur(Bitmap src, Bitmap dst, int radius) {
            Allocation srcImage = Allocation.createFromBitmap(mRS, src);
            Allocation tmpImage = Allocation.createTyped(mRS, srcImage.getType());
            Allocation dstImage = Allocation.createFromBitmap(mRS, dst);

            int width = src.getWidth();
            int height = src.getHeight();

            Allocation rowIndexes = createIndex(height);    // srcImage.getType().getY());
            Allocation columnIndexes = createIndex(width);  // srcImage.getType().getX());

            // Add more iterations if you like or simply make a loop
            vblur(radius, width, height, columnIndexes, srcImage, tmpImage);
            hblur(radius, width, height, rowIndexes, tmpImage, dstImage);

            dstImage.copyTo(dst);
        }
    }

    // ------------------------------------------------------------------------------------------
    /**
     * Tint image using Render Script.
     *
     * @author dlang
     *
     */
    public static class Tint {

        private RenderScript mRS;
        private ScriptC_tint mTintScript;

        public Tint(Context context) {
            mRS = RenderScript.create(context);
            mTintScript = new ScriptC_tint(mRS, mRS.getApplicationContext().getResources(),
                    R.raw.tint);
        }

        public Bitmap tint(Bitmap src, int argb) {
            Bitmap dst = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());

            tint(src, dst, argb);

            return dst;
        }

        private void tint(Bitmap src, Bitmap dst, int argb) {
            Allocation inImage = Allocation.createFromBitmap(mRS, src,
                    Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
            Allocation outImage = Allocation.createTyped(mRS, inImage.getType());

            // mTintScript = new ScriptC_tint(mRS, getResources(), R.raw.mono);

            mTintScript.set_gIn(inImage);
            mTintScript.set_gOut(outImage);
            mTintScript.set_gScript(mTintScript);
            mTintScript.set_gAlpha(getA(argb));
            mTintScript.set_gRed(getR(argb));
            mTintScript.set_gGreen(getG(argb));
            mTintScript.set_gBlue(getB(argb));

            mTintScript.invoke_filter();
            outImage.copyTo(dst);
        }
    }

    // Get alpha from 32bit color
    public static float getA(int argb) {
        return ((argb >> 24) & 0xff) / 255.0f;
    }

    // Get red from 32bit color
    public static float getR(int argb) {
        return ((argb >> 16) & 0xff) / 255.0f;
    }

    // Get green from 32bit color
    public static float getG(int argb) {
        return ((argb >> 8) & 0xff) / 255.0f;
    }

    // Get blue from 32bit color
    public static float getB(int argb) {
        return ((argb >> 0) & 0xff) / 255.0f;
    }
}
