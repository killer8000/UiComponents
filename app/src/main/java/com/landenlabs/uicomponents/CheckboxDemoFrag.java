package com.landenlabs.uicomponents;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

/**
 * Demonstrate Checkbox ui components.
 *
 * @author Dennis Lang (LanDen Labs)
 * @see <a href="http://landenlabs.com/android/index-m.html"> author's web-site </a>
 */
public class CheckboxDemoFrag  extends Fragment implements View.OnClickListener {

    private View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.checkbox_demo, container, false);
        setup();
        return mRootView;
    }

    public void setup() {

        final Button p2Btn1 =  Ui.needViewById(mRootView, R.id.p2Button1);
        p2Btn1.setOnClickListener(this);

        final TextView p2Tv1 =  Ui.needViewById(mRootView, R.id.p2TextView1);
        p2Tv1.setOnClickListener(this);

        final CheckBox p2Cb1 =  Ui.needViewById(mRootView, R.id.p2Checkbox1);
        p2Cb1.setOnClickListener(this);
        final  CheckBox p2Cb2 =  Ui.needViewById(mRootView, R.id.p2Checkbox2);
        p2Cb2.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view  instanceof CompoundButton){
            CompoundButton cBtn = (CompoundButton)view;
            Drawable[] drawables = cBtn.getCompoundDrawables(); // left,top,right,bottom
            int[] state = new int[]{android.R.attr.state_checked};
            int[] btnState = view.getDrawableState();
            drawables[2].setState(btnState);
        }
    }
}