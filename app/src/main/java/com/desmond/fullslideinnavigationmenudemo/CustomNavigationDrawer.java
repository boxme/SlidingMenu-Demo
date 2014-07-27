package com.desmond.fullslideinnavigationmenudemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.lang.reflect.Method;

/**
 * Created by desmond on 27/7/14.
 */
public class CustomNavigationDrawer extends LinearLayout {

    public static final String TAG = CustomNavigationDrawer.class.getSimpleName();

    private static final String KEY_DRAWER_SHOWN = "drawerWasShown";
    private static final String KEY_STATUS_BAR_HEIGHT = "statusBarHeight";
    private static final String KEY_SUPERSTATE = "superState";

    private boolean mIsDrawerShown = false;
    private boolean mWasDrawerShown = false;
    private View mDrawer;
    private ViewGroup mContent;
    private FrameLayout mParent;
    private ListView mDrawerList;
    private int mDrawerWidth;
    private int mStatusHeight = -1;
    private Activity mActivity;

    private TranslateAnimation mSlideRightAnim;
    private TranslateAnimation mSlideleftAnim;
    private TranslateAnimation mSlideContentLeftAnim;


    public CustomNavigationDrawer(Context ctx) {
        super(ctx);
    }

    public CustomNavigationDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Activity activity, ListView drawerList) {
        mActivity = activity;
        mDrawerList = drawerList;
        mDrawerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                250, mActivity.getResources().getDisplayMetrics());

        // Create animation
        mSlideRightAnim = new TranslateAnimation(-mDrawerWidth, 0, 0, 0);
        mSlideRightAnim.setFillAfter(true);
        mSlideleftAnim = new TranslateAnimation(0, -mDrawerWidth, 0, 0);
        mSlideleftAnim.setFillAfter(true);
        mSlideContentLeftAnim = new TranslateAnimation(mDrawerWidth, 0, 0, 0);
        mSlideContentLeftAnim.setFillAfter(true);
        setAnimationDuration(330);

        Log.d(TAG, "init drawer");
    }

    /**
     * Set how long slide animation should be
     */
    public void setAnimationDuration(long slideDuration) {
        mSlideRightAnim.setDuration(slideDuration);
        mSlideleftAnim.setDuration(slideDuration*3/2);
        mSlideContentLeftAnim.setDuration(slideDuration*3/2);
    }

    /**
     * Slide the menu in
     */
    public void show() {
        show(true);
    }

    /**
     * Set the menu to shown status without displaying any slide animation
     */
    public void setAsShow() {
        show(false);
    }

    private void show(boolean animate) {
        // Adopt to status bar height, if not the support actionbar
        try {
            Method getSupportActionBar =
                    mActivity.getClass().getMethod("getSupportActionBar", (Class[]) null);
            Object supportActionBar = getSupportActionBar.invoke(mActivity, (Class[]) null);

            // To check for null
            supportActionBar.toString();

            if (Build.VERSION.SDK_INT >= 11) {
                // Add the margin
                getStatusBarHeight();
            }

        } catch (Exception e) {
            // There is no support action bar
            getStatusBarHeight();
        }

        // Modify content layout params
        try {
            mContent = (LinearLayout) mActivity.findViewById(android.R.id.content).getParent();
        } catch (ClassCastException e) {
            // When there is no title bar (android:theme="@android:style/Theme.NoTitleBar").
            // the android.R.id.content FrameLayout is directly attached to the DecorView
            // without the intermediate LinearLayout that holds the title bar plus content
            if (Build.VERSION.SDK_INT < 18) {
                mContent = (ViewGroup) mActivity.findViewById(android.R.id.content);
            } else {
                // FIXME? what about the corner cases (fullscreen etc)
                mContent = (ViewGroup) mActivity.findViewById(android.R.id.content).getParent();
            }
        }

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -1, 3);  // Width, height, gravity
        params.setMargins(mDrawerWidth, 0, -mDrawerWidth, 0);
        mContent.setLayoutParams(params);

        // Animate for smooth slide-out
        if (animate)
            mContent.startAnimation(mSlideRightAnim);

        // Quirk for sony Xperia devices on ICS only, shouldn't hurt on others
        if(Build.VERSION.SDK_INT >= 11
                && Build.VERSION.SDK_INT <= 15
                && Build.MANUFACTURER.contains("Sony") && mIsDrawerShown)
            mContent.setX(mDrawerWidth);

        // Add drawer to parent
        try {
            mParent = (FrameLayout) mContent.getParent();
        } catch (ClassCastException e) {
            // Most probably a LinearLayout, at least on Galaxy S3
            LinearLayout realParent = (LinearLayout) mContent.getParent();
            mParent = new FrameLayout(mActivity);

            // Add FrameLayout to real parent of content
            realParent.addView(mParent, 0);

            // Remove content from real parent
            realParent.removeView(mContent);

            // Add content to FrameLayout
            mParent.addView(mContent);
        }

        LayoutInflater inflater = LayoutInflater.from(mActivity);
        mDrawer = inflater.inflate(R.layout.drawer, null);

        // Connect the drawer's listView
        if (mDrawerList.getParent() != null) {
            ((ViewGroup) mDrawerList.getParent()).removeView(mDrawerList);
        }
        if (mDrawerList != null) {
            ((LinearLayout) mDrawer.findViewById(R.id.drawerlist_container)).addView(mDrawerList);
        }

        FrameLayout.LayoutParams drawerLayoutParams = new FrameLayout.LayoutParams(-1, -1, 3);
        drawerLayoutParams.setMargins(0, mStatusHeight, 0, 0);
        mDrawer.setLayoutParams(drawerLayoutParams);

        mParent.addView(mDrawer);

        // Slide menu in
        if (animate)
            mDrawer.startAnimation(mSlideRightAnim);

        mDrawer.findViewById(R.id.overlay).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });

        enableDisableViewGroup(mContent, false);

        mIsDrawerShown = true;
        mWasDrawerShown = true;
    }

    /**
     * Slide the drawer out
     */
    public void hide() {
        mDrawer.startAnimation(mSlideleftAnim);
        mParent.removeView(mDrawer);

        mContent.startAnimation(mSlideContentLeftAnim);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mContent.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        mContent.setLayoutParams(params);
        enableDisableViewGroup(mContent, true);

        if(Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT <= 15 && Build.MANUFACTURER.contains("Sony"))
            mContent.setX(0);

        mIsDrawerShown = false;
    }

    /**
     * originally: http://stackoverflow.com/questions/5418510/disable-the-touch-events-for-all-the-views
     * Modified for the needs here
     */
    private void enableDisableViewGroup(ViewGroup viewGroup, boolean enabled) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; ++i) {
            View view = viewGroup.getChildAt(i);

            if (view.isFocusable()) {
                view.setEnabled(enabled);
            }

            if (view instanceof ViewGroup) {
                enableDisableViewGroup((ViewGroup) view, enabled);
            } else if (view instanceof ListView) {
                ListView listView = (ListView) view;
                int listChildCount = listView.getChildCount();
                for (int j = 0; j < listChildCount; ++j) {
                    if (view.isFocusable())
                        listView.getChildAt(j).setEnabled(false);
                }
            }

        }
    }

    private void getStatusBarHeight() {
        // Only do this if not already set
        // Especially when called from within onCreate(), this doesn't
        // return the true value
        if (mStatusHeight == -1) {
            Rect rect = new Rect();
            Window window = mActivity.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(rect);
            mStatusHeight = rect.top;
        }
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        try {

            if (state instanceof Bundle) {
                Bundle bundle = (Bundle) state;
                mStatusHeight = bundle.getInt(KEY_STATUS_BAR_HEIGHT);

                if (bundle.getBoolean(KEY_DRAWER_SHOWN)) {
                    show(false);
                }
                super.onRestoreInstanceState(bundle.getParcelable(KEY_SUPERSTATE));
                return;
            }

            super.onRestoreInstanceState(state);
        } catch (NullPointerException e) {

        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_SUPERSTATE, super.onSaveInstanceState());
        bundle.putBoolean(KEY_DRAWER_SHOWN, mIsDrawerShown);
        bundle.putInt(KEY_STATUS_BAR_HEIGHT, mStatusHeight);

        return bundle;
    }
}
