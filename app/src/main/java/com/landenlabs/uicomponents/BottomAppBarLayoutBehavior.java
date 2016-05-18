package com.landenlabs.uicomponents;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;

/**
 * Dennis Lang on 5/17/16.
 *
 * Hide AppBarLayout when at bottom of scroll and show when at top of scroll.
 * http://stackoverflow.com/questions/31830521/how-to-animate-both-top-and-bottom-toolbarsor-any-other-view-enter-exit-screen
 */
public class BottomAppBarLayoutBehavior extends  CoordinatorLayout.Behavior<AppBarLayout> {
// public class BottomAppBarLayoutBehavior extends AppBarLayout.Behavior {
    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
    private boolean mIsAnimatingOut = false;

    public BottomAppBarLayoutBehavior(Context context, AttributeSet attrs) {
        super();
    }


    @Override
    public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final AppBarLayout child,
                                       final View directTargetChild, final View target, final int nestedScrollAxes) {
        return true;

        // return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
        //        && super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }




       @Override
       public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed) {
           super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
           Log.d("foo", "onNestedPreScroll dy=" + dy + " consumed[1]=" + consumed[1]);

           if (dy > 0 && !this.mIsAnimatingOut && child.getVisibility() == View.VISIBLE) {
               animateOut(child);
           } else if (dy < 0 && child.getVisibility() != View.VISIBLE) {
               animateIn(child);
           }
       }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
        Log.d("foo", "StopNestedScroll");
    }

    /*
     int mOffset = 0;
           @Override
           public boolean setTopAndBottomOffset(int offset) {
               return super.setTopAndBottomOffset(mOffset);
           }

           @Override
           public int getTopAndBottomOffset() {
               return mOffset;
           }

    */
    @Override
    public void onNestedScroll(final CoordinatorLayout coordinatorLayout, final AppBarLayout child,
                               final View target, final int dxConsumed, final int dyConsumed,
                               final int dxUnconsumed, final int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

        Log.d("foo", "dyConsumed=" + dyConsumed + " dyUnconsumed=" +dyUnconsumed);

        /*
        if (dyConsumed > 0 && !this.mIsAnimatingOut && child.getVisibility() == View.VISIBLE) {
            animateOut(child);
        } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
            animateIn(child);
        }
        */

    }

    private void animateOut(final AppBarLayout appBarLayout) {
            ViewCompat.animate(appBarLayout).translationY(0F).alpha(0.0F).setInterpolator(INTERPOLATOR).withLayer()
                    .setListener(new ViewPropertyAnimatorListener() {
                        public void onAnimationStart(View view) {
                            BottomAppBarLayoutBehavior.this.mIsAnimatingOut = true;
                        }

                        public void onAnimationCancel(View view) {
                            BottomAppBarLayoutBehavior.this.mIsAnimatingOut = false;
                        }

                        public void onAnimationEnd(View view) {
                            BottomAppBarLayoutBehavior.this.mIsAnimatingOut = false;
                            view.setVisibility(View.GONE);
                        }
                    }).start();
    }

    private void animateIn(AppBarLayout appBarLayout) {
        appBarLayout.setVisibility(View.VISIBLE);
            ViewCompat.animate(appBarLayout).scaleX(1.0F).scaleY(1.0F).alpha(1.0F)
                    .setInterpolator(INTERPOLATOR).withLayer().setListener(null)
                    .start();

    }
}