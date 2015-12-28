package com.landenlabs.uicomponents;

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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

/**
 * Demonstrate list of checkboxes (two different types).
 * CheckBox  does not honor the single selection mode of ListView
 * CheckedTextView honors the single selection mode of ListView
 *
 * @author Dennis Lang (LanDen Labs)
 * @see <a href="http://landenlabs.com/android/index-m.html"> author's web-site </a>
 */
public class List1Frag  extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private final List<String> mListStrings = Arrays.asList("Apple", "Avocado", "Banana",
            "Blueberry", "Coconut", "Durian", "Guava", "Kiwifruit",
            "Jackfruit", "Mango", "Olive", "Pear", "Sugar-apple");

    // ---- Local Data ----
    private int mCurrentIdx = 0;
    private View mRootView;
    private TextView mTopTitle;
    private TextView mBottomTitle;
    private ListView mListView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.list1, container, false);

        setup();
        return mRootView;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        // Required to prevent duplicate id when Fragment re-created.
        Fragment fragment = (getFragmentManager().findFragmentById(R.id.list1_id));
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }

    int mRowLayoutRes = R.layout.list1_row_checkbox;

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            setRowList(buttonView.getId());
        }
    }

    void setRowList(int id) {
        switch (id) {
            case R.id.ckBoxRb:
                mTopTitle.setText("CheckBox");
                mRowLayoutRes = R.layout.list1_row_checkbox;
                mListView.setAdapter(new ArrayAdapter<String>(mRootView.getContext(), mRowLayoutRes, mListStrings));
                break;
            case R.id.ckTxBoxRb:
                mTopTitle.setText("CheckTextBox");
                mRowLayoutRes = R.layout.list1_row_checktextbox;
                mListView.setAdapter(new ArrayAdapter<String>(mRootView.getContext(), mRowLayoutRes, mListStrings));
                break;
        }
    }

    private void setup() {
        mTopTitle = Ui.viewById(mRootView, R.id.topTitle);
        Ui.<RadioButton>viewById(mRootView, R.id.ckBoxRb).setOnCheckedChangeListener(this);
        Ui.<RadioButton>viewById(mRootView, R.id.ckTxBoxRb).setOnCheckedChangeListener(this);

        mListView = Ui.viewById(mRootView, R.id.list1view);
        setRowList(R.id.ckBoxRb);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // String itemStr = listView.getItemAtPosition(position).toString();
                // title.setText(itemStr);
                mCurrentIdx = position;
            }
        });
    }
}
