package com.mosio.myapplication2.views;


import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mosio.myapplication2.R;


public class DuoOptionView extends RelativeLayout {
    private OptionViewHolder mOptionViewHolder;

    private static final float ALPHA_CHECKED = 1f;
    private static final float ALPHA_UNCHECKED = 0.5f;

    private boolean mIsSideSelectorEnabled = false;
    private boolean mIsSelectorEnabled = false;

    public DuoOptionView(Context context) {
        this(context, null);
    }

    public DuoOptionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DuoOptionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        ViewGroup rootView = (ViewGroup) inflate(getContext(), R.layout.duo_view_option, this);

        mOptionViewHolder = new OptionViewHolder(rootView);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        setSelected(isSelected());
    }

    public boolean isSideSelectorEnabled() {
        return mIsSideSelectorEnabled;
    }

    public boolean isSelectorEnabled() {
        return mIsSelectorEnabled;
    }


    public void setSideSelectorEnabled(boolean sideSelectorEnabled) {
        mIsSideSelectorEnabled = sideSelectorEnabled;
        invalidate();
        requestLayout();
    }


    public void setSelectorEnabled(boolean selectorEnabled) {
        mIsSelectorEnabled = selectorEnabled;
        invalidate();
        requestLayout();
    }


    public void setSelected(boolean selected) {
        if (selected) {
            mOptionViewHolder.mTextViewOption.setAlpha(ALPHA_CHECKED);
            if (mIsSelectorEnabled) {
                mOptionViewHolder.mImageViewSelector.setVisibility(VISIBLE);
                mOptionViewHolder.mImageViewSelector.setAlpha(ALPHA_CHECKED);
            } else {
                mOptionViewHolder.mImageViewSelector.setVisibility(GONE);
            }
            if (mIsSideSelectorEnabled) {
                mOptionViewHolder.mImageViewSelectorSide.setVisibility(VISIBLE);
            }
        } else {
            mOptionViewHolder.mTextViewOption.setAlpha(ALPHA_UNCHECKED);
            if (mIsSelectorEnabled) {
                mOptionViewHolder.mImageViewSelector.setVisibility(VISIBLE);
                mOptionViewHolder.mImageViewSelector.setAlpha(ALPHA_UNCHECKED);
            } else {
                mOptionViewHolder.mImageViewSelector.setVisibility(GONE);
            }
            if (mIsSideSelectorEnabled) {
                mOptionViewHolder.mImageViewSelectorSide.setVisibility(GONE);
            }
        }
    }


    public boolean isSelected() {
        return mOptionViewHolder.mTextViewOption.getAlpha() == ALPHA_CHECKED;
    }

    public void bind(String optionText) {
        mOptionViewHolder.mTextViewOption.setText(optionText);
        mOptionViewHolder.mTextViewOption.setAlpha(ALPHA_UNCHECKED);
        mOptionViewHolder.mImageViewSelector.setVisibility(GONE);
    }


    public void bind(String optionText, @Nullable Drawable selectorDrawable) {
        mOptionViewHolder.mTextViewOption.setText(optionText);
        mOptionViewHolder.mTextViewOption.setAlpha(ALPHA_UNCHECKED);
        if (selectorDrawable != null) {
            mOptionViewHolder.mImageViewSelector.setImageDrawable(selectorDrawable);
        }
        mOptionViewHolder.mImageViewSelector.setAlpha(ALPHA_UNCHECKED);
        setSelectorEnabled(true);
    }

    public void bind(String optionText, @Nullable Drawable selectorDrawable, @Nullable Drawable selectorSideDrawable) {
        mOptionViewHolder.mTextViewOption.setText(optionText);
        mOptionViewHolder.mTextViewOption.setAlpha(ALPHA_UNCHECKED);
        if (selectorDrawable != null) {
            mOptionViewHolder.mImageViewSelector.setImageDrawable(selectorDrawable);
        }
        mOptionViewHolder.mImageViewSelector.setAlpha(ALPHA_UNCHECKED);
        if (selectorSideDrawable != null) {
            mOptionViewHolder.mImageViewSelectorSide.setImageDrawable(selectorSideDrawable);
        }
        setSelectorEnabled(true);
        setSideSelectorEnabled(true);
    }

    private static class OptionViewHolder {
        private final TextView mTextViewOption;
        private final ImageView mImageViewSelector;
        private final ImageView mImageViewSelectorSide;

        OptionViewHolder(ViewGroup rootView) {
            mTextViewOption = (TextView) rootView.findViewById(R.id.duo_view_option_text);
            mImageViewSelector = (ImageView) rootView.findViewById(R.id.duo_view_option_selector);
            mImageViewSelectorSide = (ImageView) rootView.findViewById(R.id.duo_view_option_selector_side);

            hideSelectorsByDefault();
        }


        private void hideSelectorsByDefault() {
            mImageViewSelector.setVisibility(INVISIBLE);
            mImageViewSelectorSide.setVisibility(GONE);
        }
    }
}

