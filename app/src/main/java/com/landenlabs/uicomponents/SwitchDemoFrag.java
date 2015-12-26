package com.landenlabs.uicomponents;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrate Toggle and Switch  ui components.
 *
 * @author Dennis Lang (LanDen Labs)
 * @see <a href="http://landenlabs.com/android/index-m.html"> author's web-site </a>
 */
public class SwitchDemoFrag  extends Fragment implements View.OnClickListener  {

    private View mRootView;
    private List<View> mViewList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.switch_demo, container, false);
        setup();
        return mRootView;
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        // Required to prevent duplicate id when Fragment re-created.
        Fragment fragment = (getFragmentManager().findFragmentById(R.id.switch_demo_id));
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // -- global  controls --
            case R.id.enabledTb:
                for (View viewItem : mViewList) {
                    viewItem.setEnabled(isChecked(view));
                }
                break;
            case R.id.checkedTb:
                for (View viewItem : mViewList) {
                    setChecked(viewItem, isChecked(view));
                }
                break;
            case R.id.backgroundTb:
                if (isChecked(view)) {
                    Ui.needViewById(mRootView, R.id.switch_holder).setBackgroundResource(R.drawable.bg);
                } else {
                    Ui.needViewById(mRootView, R.id.switch_holder).setBackgroundResource(R.drawable.bg_dark);
                }
                break;
        }
    }

    private void setup() {
        addView(Ui.needViewById(mRootView, R.id.toggleButton1));
        addView(Ui.needViewById(mRootView, R.id.toggleButton2));
        addView(Ui.needViewById(mRootView, R.id.switchButton1));
        addView(Ui.needViewById(mRootView, R.id.switchButton2));
        addView(Ui.needViewById(mRootView, R.id.switchCompat1));
        addView(Ui.needViewById(mRootView, R.id.switchCompat2));
        addView(Ui.needViewById(mRootView, R.id.switchCompat3));

        Ui.needViewById(mRootView, R.id.enabledTb).setOnClickListener(this);
        Ui.needViewById(mRootView, R.id.checkedTb).setOnClickListener(this);
        Ui.needViewById(mRootView, R.id.backgroundTb).setOnClickListener(this);
    }

    private void addView(View view) {
        if (view != null) {
            mViewList.add(view);
            view.setOnClickListener(this);
        }
    }

    private boolean isChecked(View view) {
        if (view instanceof CompoundButton)
            return ((CompoundButton)view).isChecked();
        else
            return view.isSelected();
    }

    private void setChecked(View view, boolean checked) {
        if (view instanceof CompoundButton)
            ((CompoundButton)view).setChecked(checked);
        else
            view.setSelected(checked);
    }
}
