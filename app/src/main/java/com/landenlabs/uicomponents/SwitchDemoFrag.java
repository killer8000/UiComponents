package com.landenlabs.uicomponents;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

/**
 * Demonstrate Toggle and Switch  ui components.
 *
 * @author Dennis Lang (LanDen Labs)
 * @see <a href="http://landenlabs.com/android/index-m.html"> author's web-site </a>
 */
public class SwitchDemoFrag  extends Fragment {

    private View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.switch_demo, container, false);
        setup();
        return mRootView;
    }

    public void setup() {
        final View tb1 = Ui.needViewById(mRootView, R.id.toggleButton1);
        tb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        final View tb2 = Ui.needViewById(mRootView, R.id.toggleButton2);
        tb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        final View sb1 = Ui.needViewById(mRootView, R.id.switchButton1);
        sb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        final View sb2 = Ui.needViewById(mRootView, R.id.switchButton2);
        sb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        final View sc1 = Ui.needViewById(mRootView, R.id.switchCompat1);
        sc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        final View sc2 = Ui.needViewById(mRootView, R.id.switchCompat2);
        sc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        ToggleButton tb = Ui.needViewById(mRootView, R.id.enabledTb);
        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tb1.setEnabled(isChecked);
                tb2.setEnabled(isChecked);
                sb1.setEnabled(isChecked);
                sb2.setEnabled(isChecked);
                sc1.setEnabled(isChecked);
                sc2.setEnabled(isChecked);
            }
        });


    }

}
