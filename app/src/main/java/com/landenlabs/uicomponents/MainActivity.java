package com.landenlabs.uicomponents;

/*
 * Copyright (c) 2015 Dennis Lang (LanDen Labs) landenlabs@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 *  following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial
 *  portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 * NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *  @author Dennis Lang  (3/21/2015)
 *  @see http://landenlabs.com
 *
 */

import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


/**
 * Main Activity to WebTester app.
 *
 * @author Dennis Lang (LanDen Labs)
 * @see <a href="http://landenlabs.com/android/index-m.html"> author's web-site </a>
 */
public class MainActivity extends ActionBarActivity    {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    ActionBar mActionBar;

    static final String STATE_ADAPTER = "secPageAdapter";
    Parcelable mAdapterParcelable;

    // ---------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
        }
        setContentView(R.layout.activity_main);

        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setTitle("UiComponents v" + BuildConfig.VERSION_NAME + " API" + Build.VERSION.SDK_INT +
                    (BuildConfig.DEBUG ? " Debug" : ""));

            mActionBar.setSubtitle(BuildConfig.VERSION_NAME);
            mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME);
            mActionBar.setIcon(R.drawable.uicomponents_sm);
        }

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (mActionBar != null && mSectionsPagerAdapter != null) {
                    mActionBar.setSubtitle(mSectionsPagerAdapter.getPageTitle(position));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mViewPager != null && mViewPager.getAdapter() == null) {
            Log.d("foo", "onResume");
            // Create the adapter that will return a fragment for each page.
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
            mViewPager.setAdapter(mSectionsPagerAdapter);
            if (false) {
                // Optionally set limit of pages to keep.
                mViewPager.setOffscreenPageLimit(mSectionsPagerAdapter.getCount());
            }
            if (mAdapterParcelable != null) {
                mSectionsPagerAdapter.restoreState(mAdapterParcelable, null);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("foo", "onPause");
        if (mSectionsPagerAdapter != null)
            mAdapterParcelable = mSectionsPagerAdapter.saveState();
        mSectionsPagerAdapter = null;
        if (mViewPager != null)
             mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // =============================================================================================
    static class Item {
        String mTitle;
        int mLayout;
        public Item(String title, int layout) {
            mTitle = title; mLayout = layout;
        }
    }

    static final Item[] mItems = new Item[] {
            new Item( "FullScreen",  R.layout.page_fullscreen ),

            new Item( "Assorted", R.layout.page0frag),
            new Item( "Text", R.layout.page_text),

            new Item( "Grid Images", R.layout.page_grid_image),
            new Item( "Images", R.layout.page_image_scales),
            new Item( "ImageOverlap",  R.layout.page_image_over ),

            new Item( "RadioBtn List", R.layout.page_radio_list),
            new Item( "CkBox List", R.layout.page_list1),       // min api 21
            new Item( "Custom List",  R.layout.page_anim_list ),

            new Item( "Toggle/Switch",  R.layout.page_switches),
            new Item( "CheckboxRight",  R.layout.page_checkbox_right ),
            new Item( "CheckboxLeft",  R.layout.page_checkbox_left ),

            new Item( "RelLayout",  R.layout.page_rellayout ),
            new Item( "LayoutAnim",  R.layout.page_layout_anim ),
    };

    // =============================================================================================
    // SectionsPagerAdapter - implement page swipes
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

            // ActionBar actionBar = getActionBar();
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PageFragment (defined as a static inner class below).
            return PageFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return mItems.length;  // View swipe page count.
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mItems[position].mTitle;
        }
    }

    /**
     * A page fragment with selectable layouts per page number.
     */
    public static class PageFragment extends Fragment {
        // The fragment argument representing the section number for this  fragment.
        private static final String ARG_page_number = "page_number";
        int m_pageNum = 0;

        // Returns a new instance of this fragment for the given section  number.
        public static PageFragment newInstance(int sectionNumber) {
            PageFragment fragment = new PageFragment();

            Bundle args = new Bundle();
            args.putInt(ARG_page_number, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            m_pageNum = getArguments().getInt(ARG_page_number);
            int layout =  mItems[m_pageNum].mLayout;
            View rootView = inflater.inflate(layout, container, false);
            return rootView;
        }
    }
}
