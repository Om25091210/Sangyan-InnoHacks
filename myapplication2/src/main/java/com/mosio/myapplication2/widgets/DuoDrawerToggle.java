package com.mosio.myapplication2.widgets;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.core.view.GravityCompat;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;



import static androidx.drawerlayout.widget.DrawerLayout.DrawerListener;
import static androidx.drawerlayout.widget.DrawerLayout.OnClickListener;

import com.mosio.myapplication2.views.DuoDrawerLayout;


public class DuoDrawerToggle implements DrawerListener {


    public interface DelegateProvider {

        @Nullable
        Delegate getDrawerToggleDelegate();
    }

    public interface Delegate {

        void setActionBarUpIndicator(Drawable upDrawable, @StringRes int contentDescRes);

        void setActionBarDescription(@StringRes int contentDescRes);

        Drawable getThemeUpIndicator();

        /**
         * Returns the context of ActionBar
         */
        Context getActionBarThemedContext();

        /**
         * Returns whether navigation icon is visible or not.
         * Used to print warning messages in case developer forgets to set displayHomeAsUp to true
         */
        boolean isNavigationVisible();
    }

    private final Delegate mActivityImpl;
    private final DuoDrawerLayout mDuoDrawerLayout;

    private DrawerToggle mSlider;
    private Drawable mHomeAsUpIndicator;
    private boolean mDrawerIndicatorEnabled = true;
    private boolean mHasCustomUpIndicator;
    private final int mOpenDrawerContentDescRes;
    private final int mCloseDrawerContentDescRes;
    // used in toolbar mode when DrawerToggle is disabled
    private OnClickListener mToolbarNavigationClickListener;
    // If developer does not set displayHomeAsUp, DrawerToggle won't show up.
    // DrawerToggle logs a warning if this case is detected
    private boolean mWarnedForDisplayHomeAsUp = false;

    public DuoDrawerToggle(Activity activity, DuoDrawerLayout duoDrawerLayout,
                           @StringRes int openDrawerContentDescRes,
                           @StringRes int closeDrawerContentDescRes) {
        this(activity, null, duoDrawerLayout, null, openDrawerContentDescRes,
                closeDrawerContentDescRes);
    }

    public DuoDrawerToggle(Activity activity, DuoDrawerLayout duoDrawerLayout,
                           ImageView toolbar, @StringRes int openDrawerContentDescRes,
                           @StringRes int closeDrawerContentDescRes) {
        this(activity, toolbar, duoDrawerLayout, null, openDrawerContentDescRes,
                closeDrawerContentDescRes);
    }

    <T extends Drawable & DrawerToggle> DuoDrawerToggle(Activity activity, ImageView toolbar,
                                                        DuoDrawerLayout duoDrawerLayout, T slider,
                                                        @StringRes int openDrawerContentDescRes,
                                                        @StringRes int closeDrawerContentDescRes) {
        if (toolbar != null) {
            mActivityImpl = new ToolbarCompatDelegate(toolbar);
            toolbar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDrawerIndicatorEnabled) {
                        toggle();
                    } else if (mToolbarNavigationClickListener != null) {
                        mToolbarNavigationClickListener.onClick(v);
                    }
                }
            });
        } else if (activity instanceof DelegateProvider) { // Allow the Activity to provide an impl
            mActivityImpl = ((DelegateProvider) activity).getDrawerToggleDelegate();
        } else {
            mActivityImpl = new JellybeanMr2Delegate(activity);
        }

        mDuoDrawerLayout = duoDrawerLayout;
        mOpenDrawerContentDescRes = openDrawerContentDescRes;
        mCloseDrawerContentDescRes = closeDrawerContentDescRes;
        if (slider == null) {
            assert mActivityImpl != null;
            mSlider = new DrawerArrowDrawableToggle(activity,
                    mActivityImpl.getActionBarThemedContext());
        } else {
            mSlider = slider;
        }

        mHomeAsUpIndicator = getThemeUpIndicator();
    }

    public void syncState() {
        if (mDuoDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mSlider.setPosition(1);
        } else {
            mSlider.setPosition(0);
        }
        if (mDrawerIndicatorEnabled) {
            setActionBarUpIndicator((Drawable) mSlider,
                    mDuoDrawerLayout.isDrawerOpen(GravityCompat.START) ?
                            mCloseDrawerContentDescRes : mOpenDrawerContentDescRes);
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        // Reload drawables that can change with configuration
        if (!mHasCustomUpIndicator) {
            mHomeAsUpIndicator = getThemeUpIndicator();
        }
        syncState();
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null && item.getItemId() == android.R.id.home && mDrawerIndicatorEnabled) {
            toggle();
            return true;
        }
        return false;
    }

    private void toggle() {
        if (mDuoDrawerLayout.isDrawerVisible(GravityCompat.START)) {
            mDuoDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            mDuoDrawerLayout.openDrawer(GravityCompat.START);
        }
    }


    public void setHomeAsUpIndicator(Drawable indicator) {
        if (indicator == null) {
            mHomeAsUpIndicator = getThemeUpIndicator();
            mHasCustomUpIndicator = false;
        } else {
            mHomeAsUpIndicator = indicator;
            mHasCustomUpIndicator = true;
        }

        if (!mDrawerIndicatorEnabled) {
            setActionBarUpIndicator(mHomeAsUpIndicator, 0);
        }
    }


    public void setHomeAsUpIndicator(int resId) {
        Drawable indicator = null;
        if (resId != 0) {
            indicator = mDuoDrawerLayout.getResources().getDrawable(resId);
        }
        setHomeAsUpIndicator(indicator);
    }


    public boolean isDrawerIndicatorEnabled() {
        return mDrawerIndicatorEnabled;
    }

    public void setDrawerIndicatorEnabled(boolean enable) {
        if (enable != mDrawerIndicatorEnabled) {
            if (enable) {
                setActionBarUpIndicator((Drawable) mSlider,
                        mDuoDrawerLayout.isDrawerOpen(GravityCompat.START) ?
                                mCloseDrawerContentDescRes : mOpenDrawerContentDescRes);
            } else {
                setActionBarUpIndicator(mHomeAsUpIndicator, 0);
            }
            mDrawerIndicatorEnabled = enable;
        }
    }



    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        mSlider.setPosition(Math.min(1f, Math.max(0, slideOffset)));
    }


    @Override
    public void onDrawerOpened(View drawerView) {
        mSlider.setPosition(1);
        if (mDrawerIndicatorEnabled) {
            setActionBarDescription(mCloseDrawerContentDescRes);
        }
    }


    @Override
    public void onDrawerClosed(View drawerView) {
        mSlider.setPosition(0);
        if (mDrawerIndicatorEnabled) {
            setActionBarDescription(mOpenDrawerContentDescRes);
        }
    }


    @Override
    public void onDrawerStateChanged(int newState) {
    }

    public OnClickListener getToolbarNavigationClickListener() {
        return mToolbarNavigationClickListener;
    }


    public void setToolbarNavigationClickListener(
            OnClickListener onToolbarNavigationClickListener) {
        mToolbarNavigationClickListener = onToolbarNavigationClickListener;
    }

    void setActionBarUpIndicator(Drawable upDrawable, int contentDescRes) {
        if (!mWarnedForDisplayHomeAsUp && !mActivityImpl.isNavigationVisible()) {
            Log.w("DuoDrawerToggle", "DrawerToggle may not show up because NavigationIcon"
                    + " is not visible. You may need to call "
                    + "actionbar.setDisplayHomeAsUpEnabled(true);");
            mWarnedForDisplayHomeAsUp = true;
        }
        mActivityImpl.setActionBarUpIndicator(upDrawable, contentDescRes);
    }

    void setActionBarDescription(int contentDescRes) {
        mActivityImpl.setActionBarDescription(contentDescRes);
    }

    Drawable getThemeUpIndicator() {
        return mActivityImpl.getThemeUpIndicator();
    }

    static class DrawerArrowDrawableToggle extends DrawerArrowDrawable implements DrawerToggle {
        private final Activity mActivity;

        public DrawerArrowDrawableToggle(Activity activity, Context themedContext) {
            super(themedContext);
            mActivity = activity;
        }

        public void setPosition(float position) {
            if (position == 1f) {
                setVerticalMirror(true);
            } else if (position == 0f) {
                setVerticalMirror(false);
            }
            setProgress(position);
        }

        public float getPosition() {
            return getProgress();
        }
    }


    static interface DrawerToggle {

        public void setPosition(float position);

        public float getPosition();
    }


    private static class HoneycombDelegate implements Delegate {

        final Activity mActivity;
        DuoDrawerToggleHoneycomb.SetIndicatorInfo mSetIndicatorInfo;

        private HoneycombDelegate(Activity activity) {
            mActivity = activity;
        }

        @Override
        public Drawable getThemeUpIndicator() {
            return DuoDrawerToggleHoneycomb.getThemeUpIndicator(mActivity);
        }

        @Override
        public Context getActionBarThemedContext() {
            final ActionBar actionBar = mActivity.getActionBar();
            final Context context;
            if (actionBar != null) {
                context = actionBar.getThemedContext();
            } else {
                context = mActivity;
            }
            return context;
        }

        @Override
        public boolean isNavigationVisible() {
            final ActionBar actionBar = mActivity.getActionBar();
            return actionBar != null
                    && (actionBar.getDisplayOptions() & ActionBar.DISPLAY_HOME_AS_UP) != 0;
        }

        @Override
        public void setActionBarUpIndicator(Drawable themeImage, int contentDescRes) {
            mActivity.getActionBar().setDisplayShowHomeEnabled(true);
            mSetIndicatorInfo = DuoDrawerToggleHoneycomb.setActionBarUpIndicator(
                    mActivity, themeImage, contentDescRes);
            mActivity.getActionBar().setDisplayShowHomeEnabled(false);
        }

        @Override
        public void setActionBarDescription(int contentDescRes) {
            mSetIndicatorInfo = DuoDrawerToggleHoneycomb.setActionBarDescription(
                    mSetIndicatorInfo, mActivity, contentDescRes);
        }
    }

    /**
     * Delegate if SDK version is JB MR2 or newer
     */
    private static class JellybeanMr2Delegate implements Delegate {

        final Activity mActivity;

        private JellybeanMr2Delegate(Activity activity) {
            mActivity = activity;
        }

        @Override
        public Drawable getThemeUpIndicator() {
            final TypedArray a = getActionBarThemedContext().obtainStyledAttributes(null,
                    new int[]{android.R.attr.homeAsUpIndicator}, android.R.attr.actionBarStyle, 0);
            final Drawable result = a.getDrawable(0);
            a.recycle();
            return result;
        }

        @Override
        public Context getActionBarThemedContext() {
            final ActionBar actionBar = mActivity.getActionBar();
            final Context context;
            if (actionBar != null) {
                context = actionBar.getThemedContext();
            } else {
                context = mActivity;
            }
            return context;
        }

        @Override
        public boolean isNavigationVisible() {
            final ActionBar actionBar = mActivity.getActionBar();
            return actionBar != null &&
                    (actionBar.getDisplayOptions() & ActionBar.DISPLAY_HOME_AS_UP) != 0;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void setActionBarUpIndicator(Drawable drawable, int contentDescRes) {
            final ActionBar actionBar = mActivity.getActionBar();
            if (actionBar != null) {
                actionBar.setHomeAsUpIndicator(drawable);
                actionBar.setHomeActionContentDescription(contentDescRes);
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void setActionBarDescription(int contentDescRes) {
            final ActionBar actionBar = mActivity.getActionBar();
            if (actionBar != null) {
                actionBar.setHomeActionContentDescription(contentDescRes);
            }
        }
    }

    /**
     * Used when DrawerToggle is initialized with a Toolbar
     */
    static class ToolbarCompatDelegate implements Delegate {

        final ImageView mToolbar;
        final Drawable mDefaultUpIndicator;
        final CharSequence mDefaultContentDescription;

        ToolbarCompatDelegate(ImageView toolbar) {
            mToolbar = toolbar;
            mDefaultUpIndicator = toolbar.getDrawable();
            mDefaultContentDescription = "Dhiti Foundation";
        }

        @Override
        public void setActionBarUpIndicator(Drawable upDrawable, @StringRes int contentDescRes) {
            /*mToolbar.setNavigationIcon(upDrawable);
            setActionBarDescription(contentDescRes);*/
        }

        @Override
        public void setActionBarDescription(@StringRes int contentDescRes) {
            /*if (contentDescRes == 0) {
                mToolbar.setNavigationContentDescription(mDefaultContentDescription);
            } else {
                mToolbar.setNavigationContentDescription(contentDescRes);
            }*/
        }

        @Override
        public Drawable getThemeUpIndicator() {
            return mDefaultUpIndicator;
        }

        @Override
        public Context getActionBarThemedContext() {
            return mToolbar.getContext();
        }

        @Override
        public boolean isNavigationVisible() {
            return true;
        }
    }

    /**
     * Fallback delegate
     */
    static class DummyDelegate implements Delegate {
        final Activity mActivity;

        DummyDelegate(Activity activity) {
            mActivity = activity;
        }

        @Override
        public void setActionBarUpIndicator(Drawable upDrawable, @StringRes int contentDescRes) {

        }

        @Override
        public void setActionBarDescription(@StringRes int contentDescRes) {

        }

        @Override
        public Drawable getThemeUpIndicator() {
            return null;
        }

        @Override
        public Context getActionBarThemedContext() {
            return mActivity;
        }

        @Override
        public boolean isNavigationVisible() {
            return true;
        }
    }
}


