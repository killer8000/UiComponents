package com.landenlabs.uicomponents.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.landenlabs.uicomponents.R;

/**
 * Demonstrate Checkbox ui components with checkmark on right margin.
 *
 * @author Dennis Lang (LanDen Labs)
 * @see <a href="http://landenlabs.com/android/index-m.html"> author's web-site </a>
 */
public class CheckboxRightDemoFrag extends CheckboxDemoFrag {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.checkbox_right_demo, container, false);
        setup();
        return mRootView;
    }
}