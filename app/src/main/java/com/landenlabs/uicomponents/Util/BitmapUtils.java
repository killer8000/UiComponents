package com.landenlabs.uicomponents.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RSRuntimeException;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;

/**
 * Created by ldennis on 8/8/16.
 */
public class BitmapUtils {

    /**
     * Create bitmap with blur effect.
     *
     * @param context
     *            application context
     * @param resId
     *            bitmap resource id
     * @param radius
     *            the radius of the blur. Supported range 0 < radius <= 25
     * @return bitmap that has been added blur effect
     */
    public static Bitmap createBlurBitmap(Context context, int resId, int radius) {
        return addBlurEffect(context, BitmapFactory.decodeResource(context.getResources(), resId), radius);
    }

    /**
     * Create bitmap with blur effect.
     *
     * @param context
     *            application context
     * @param bitmap
     *            PNG bitmap to convert
     * @param radius
     *            the radius of the blur. Supported range 0 < radius <= 25
     * @return bitmap that has been added blur effect
     */
    public static Bitmap createBlurBitmap(Context context, Bitmap bitmap, int radius) {
        try {
            if (radius <= 25) {
                return addBlurEffect(context, bitmap, radius);
            } else {
                Bitmap blurred = bitmap;
                int pass = radius / 8;
                for (int idx = 0; idx < pass; idx++) {
                    blurred = addBlurEffect(context, blurred, 8);
                }
                return blurred;
            }
        } catch (RSRuntimeException rsex) {
            rsex.printStackTrace();
            return bitmap;
        }
    }

    /**
     * Convert bitmap format to <code>Bitmap.Config.ARGB_8888</code>.
     *
     * @param img
     *            JPEG bitmap to convert
     * @return {@link Bitmap} in ARGB8888 format
     */
    public static Bitmap convertBitmapFormatToARGB888(Bitmap img) {
        int numPixels = img.getWidth() * img.getHeight();
        int[] pixels = new int[numPixels];

        // Get JPEG pixels. Each int is the color values for one pixel.
        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        // Create a Bitmap of the appropriate format.
        Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(),
                Bitmap.Config.ARGB_8888);

        // Set RGB pixels.
        result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
        return result;
    }

    /**
     * Create bitmap with blur effect. Use support render script.
     *
     * @param context
     *            application context
     * @param bitmap
     *            PNG bitmap to convert
     * @param radius
     *            the radius of the blur. Supported range 0 < radius <= 25
     * @return bitmap that has been added blur effect
     */
    private static Bitmap addBlurEffect(Context context, Bitmap bitmap, int radius) {
        if ((radius < 0) || (radius > 25)) {
            throw new IllegalArgumentException("Blur radius must be in range [0,25]");
        }

        if (null == bitmap) {
            return null;
        }

        /**
         * Support render script library has bug with blur effect on down
         * versions than <code>Build.VERSION_CODES.JELLY_BEAN</code>. To fix
         * this bug need convert bitmap format to
         * <code>Bitmap.Config.ARGB_8888</code>.
         */
        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            bitmap = convertBitmapFormatToARGB888(bitmap);
        }

        Bitmap blurBitmap = bitmap.copy(bitmap.getConfig(), true);

        final RenderScript rs = RenderScript.create(context);
        final Allocation input = Allocation.createFromBitmap(rs, bitmap,
                Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(radius);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(blurBitmap);

        return blurBitmap;
    }

    /**
     * Adds tint effect to target bitmap.
     *
     * @param src
     *            PNG bitmap to apply tint affect
     * @param argb
     *            tint color
     * @return bitmap with tint affect
     */
    public static Bitmap addTintEffect(Bitmap src, int argb) {
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(argb, PorterDuff.Mode.SRC_OVER));

        Canvas canvas = new Canvas(src);
        canvas.drawBitmap(src, 0, 0, paint);

        return src;
    }


}
