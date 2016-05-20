package com.landenlabs.uicomponents;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Extended TextView, has additional properties:
 * <pre>
 *    underline true|false  default false
 *    decenders true|false  default true
 * </pre>
 * underline enabled painting underline
 * <br>
 * decenders disabled shrinks height to baseline
 *
 * Created by Dennis Lang on 5/19/16.
 */

public class ExTextView extends TextView {

    boolean mHasDecenders = true;
    int mOrgHeight = 0;
    public ExTextView(Context context) {
        this(context, null);
    }

    public ExTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

        if (null != attrs) {
            TypedArray attributeArray = context.obtainStyledAttributes(
                    attrs,
                    R.styleable.TextAppearanceSwitch);

            if (attributeArray.getBoolean(R.styleable.TextAppearanceSwitch_underline, false)) {
                setPaintFlags(getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            }

            mHasDecenders = attributeArray.getBoolean(R.styleable.TextAppearanceSwitch_descenders, true);
            if (!mHasDecenders) {
                // setPaintFlags(getPaintFlags() |Paint.STRIKE_THRU_TEXT_FLAG);
            }
            attributeArray.recycle();
        }
    }

    public ExTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public int getLineHeight() {
        return super.getLineHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!mHasDecenders) {
            int hgt = getHeight();
            int bl = getBaseline();
            if (hgt > bl) {
                mOrgHeight = hgt;
                setHeight(bl);
            }
        }
        super.onDraw(canvas);
    }
}
