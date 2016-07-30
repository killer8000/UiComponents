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

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.landenlabs.uicomponents.R;
import com.landenlabs.uicomponents.Ui;


/**
 * Demonstrate view shadows.
 *
 * @author Dennis Lang (LanDen Labs)
 * @see <a href="http://landenlabs.com/android/index-m.html"> author's web-site </a>
 */

public class ShadowsFrag  extends UiFragment
        implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    View mRootView;

    TextView textSizeLbl, textColorLbl;
    SeekBar textSizeSb, textColorSb;

    TextView radiusLbl, offsetXLbl, offsetYLbl, shadowColorLbl;
    SeekBar radiusSb, offsetXSb, offsetYSb, shadowColorSb;

    TextView shadowText1, shadowText2, shadowText3;
    ImageView shadowImage1, shadowImage2;
    View shadowView;

    int seekMax = 255;

    int textSize = 20;
    int maxTextSize = 40;
    int textColorIdx = 0;
    int textColors[] = new int[] { Color.RED, Color.GREEN, Color.BLUE, Color.WHITE, Color.GRAY };
    int maxTextColorIdx = textColors.length;

    int radius = 10;
    int maxRadius = 25;
    int offsetX = 10;
    int maxOffset = 25;
    int offsetY = 10;
    int maxShadowColor = 25;
    int shadowColor = 0;


    public  static int ArrayFind(int[] array, int find) {
        for (int idx = 0; idx < array.length; idx++){
            if (array[idx] == find)
                return idx;
        }

        return -1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.shadows_frag, container, false);

        setup();
        return mRootView;
    }

    @Override
    public int getFragId() {
        return R.id.shadows_id;
    }

    @Override
    public String getName() {
        return "Shadows";
    }

    @Override
    public String getDescription() {
        return "View (Text) Shadows";
    }


    private void setup() {
        final View titleView = Ui.viewById(mRootView, R.id.title);
        Ui.viewById(mRootView, R.id.bottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleView.animate().alpha(1.0f - titleView.getAlpha()).start();
            }
        });

        shadowText1 = Ui.viewById(mRootView, R.id.shadow_text1);
        shadowText2 = Ui.viewById(mRootView, R.id.shadow_text2);
        shadowText3 = Ui.viewById(mRootView, R.id.shadow_text3);
        shadowImage1 = Ui.viewById(mRootView, R.id.shadow_image1);
        shadowImage2 = Ui.viewById(mRootView, R.id.shadow_image2);
        shadowView = shadowText1;

        textSizeSb = Ui.viewById(mRootView, R.id.textsize_sb);
        textColorSb = Ui.viewById(mRootView, R.id.textcolor_sb);
        textSizeLbl = Ui.viewById(mRootView, R.id.textsize_lbl);
        textColorLbl = Ui.viewById(mRootView, R.id.textcolor_lbl);

        radiusSb = Ui.viewById(mRootView, R.id.radius_sb);
        radiusLbl = Ui.viewById(mRootView, R.id.radius_lbl);
        shadowColorSb = Ui.viewById(mRootView, R.id.shadowcolor_sb);
        shadowColorLbl = Ui.viewById(mRootView, R.id.shadowcolor_lbl);
        offsetXSb = Ui.viewById(mRootView, R.id.offsetx_sb);
        offsetXLbl = Ui.viewById(mRootView, R.id.offsetx_lbl);
        offsetYSb = Ui.viewById(mRootView, R.id.offsety_sb);
        offsetYLbl = Ui.viewById(mRootView, R.id.offsety_lbl);

        setPosSb(textSizeSb, textSize, maxTextSize);
        setPosSb(textColorSb, textColorIdx, maxTextColorIdx);
        setPosSb(radiusSb, radius, maxRadius);
        setNegSb(offsetXSb, offsetX, maxOffset);
        setNegSb(offsetYSb, offsetY, maxOffset);
        setPosSb(shadowColorSb, shadowColor, maxShadowColor);

        textSizeSb.setOnSeekBarChangeListener(this);
        textColorSb.setOnSeekBarChangeListener(this);
        radiusSb.setOnSeekBarChangeListener(this);
        shadowColorSb.setOnSeekBarChangeListener(this);
        offsetXSb.setOnSeekBarChangeListener(this);
        offsetYSb.setOnSeekBarChangeListener(this);

        shadowText1.setOnClickListener(this);
        shadowText2.setOnClickListener(this);
        shadowText3.setOnClickListener(this);
        shadowImage1.setOnClickListener(this);
        shadowImage2.setOnClickListener(this);


        //    grayLbl   = Ui.viewById(mRootView, R.id.gray_lbl);
    //    alphaLbl   = Ui.viewById(mRootView, R.id.alpha_lbl);

        updateView();
    }

    private void setPosSb(SeekBar seekBar, int value, int maxValue) {
        // int iVal = (int)((maxXYZ/2 - value) * seekMax/maxXYZ);
        int iVal = value * seekMax / maxValue;
        seekBar.setProgress(iVal);
    }
    private int getPosSb(SeekBar seekBar, int maxValue) {
        // return maxXYZ/2 - seekBar.getProgress() *  maxXYZ / seekMax;
        return seekBar.getProgress() * maxValue / seekMax;
    }

    private void setNegSb(SeekBar seekBar, int value, int maxValue) {
        int maxValue2 = maxValue*2;
        int iVal = (int)((maxValue + value) * seekMax/maxValue2);
        seekBar.setProgress(iVal);
    }
    private int getNegSb(SeekBar seekBar, int maxValue) {
        int maxValue2 = maxValue*2;
        return seekBar.getProgress() *  maxValue2 / seekMax - maxValue;
    }

    private void updateView() {

        textSize = getPosSb(textSizeSb, maxTextSize);
        textSizeLbl.setText(String.format("TextSize:%d", textSize));
        int textColor = textColors[textColorIdx];
        textColorLbl.setText(String.format("TextColor:%8x", textColor));

        radius = getPosSb(radiusSb, maxRadius);
        radiusLbl.setText(String.format("Radius:%d", radius));
        offsetX = getNegSb(offsetXSb, maxOffset);
        offsetXLbl.setText(String.format("OffsetX:%d", offsetX));
        offsetY = getNegSb(offsetYSb, maxOffset);
        offsetYLbl.setText(String.format("OffsetY:%d", offsetY));

        shadowColor = getPosSb(shadowColorSb, maxShadowColor);
        int shadowColorARGB = 0xff000000 + shadowColor *256*256 + shadowColor *256 + shadowColor;
        shadowColorLbl.setText(String.format("ShadowColor:%8x", shadowColorARGB));

        
        if (shadowView instanceof TextView) {
            TextView shadowText = (TextView)shadowView;
            shadowText.setTextSize(textSize);
            shadowText.setTextColor(textColor);
            shadowText.setShadowLayer(radius, offsetX, offsetY, shadowColorARGB);
        } else if (shadowView instanceof ImageView) {
            ImageView shadowImage = (ImageView)shadowView;
            shadowImage.getLayoutParams().width = textSize * 4;
            shadowImage.getLayoutParams().height = textSize * 4;
            
        }
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
            case R.id.shadow_text1:
            case R.id.shadow_text2:
            case R.id.shadow_text3:
            case R.id.shadow_image1:
            case R.id.shadow_image2:
                shadowView = view;
                getValuesFromView();
                break;
        }

        updateView();
    }


    private void getValuesFromView() {
        
        if (shadowView instanceof  TextView) {
            TextView shadowText = (TextView)shadowView; 
            textSize = (int) shadowText.getTextSize();
            int textColor = shadowText.getCurrentTextColor();
            textColorIdx = Math.max(0, ArrayFind(textColors, textColor));
            textColorLbl.setText(String.format("TextColor:%8x", textColor));


            if (Build.VERSION.SDK_INT >= 16) {
                shadowColor = shadowText.getShadowColor();
                offsetX = (int) shadowText.getShadowDx();
                offsetY = (int) shadowText.getShadowDy();
                radius = (int) shadowText.getShadowRadius();
            }
        } else if (shadowView instanceof  ImageView) {
            ImageView shadowImage = (ImageView)shadowView;
            textSize = shadowImage.getLayoutParams().width / 4;
        }
    }
}
