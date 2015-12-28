package com.landenlabs.uicomponents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dlang_local on 12/27/2015.
 */
public class CheckboxLeftDemoFrag extends CheckboxDemoFrag {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.checkbox_left_demo, container, false);
        setup();
        return mRootView;
    }
}
