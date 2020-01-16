package cn.longmaster.hospital.doctor.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import cn.longmaster.hospital.doctor.R;


public class LoadingButton extends RelativeLayout {

    //region Variables
    static final int DEFAULT_COLOR = android.R.color.white;
    public static final int IN_FROM_RIGHT = 0;
    public static final int IN_FROM_LEFT = 1;

    private int mDefaultTextSize;
    private ProgressBar mProgressBar;
    private TextSwitcher mTextSwitcher;
    private String mLoadingMessage;
    private String mButtonText;
    private float mTextSize;
    private int mTextColor;
    private boolean mIsLoadingShowing;
    private Typeface mTypeface;
    private int mCurrentInDirection;
    private boolean mTextSwitcherReady;
    //endregion

    //region Constructors
    public LoadingButton(Context context) {
        super(context);
        init(context, null);
    }

    public LoadingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LoadingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    //endregion

    public float getTextSize() {
        return mTextSize;
    }

    public Typeface getTypeface() {
        return mTypeface;
    }

    public void setProgressColor(int colorRes) {
        mProgressBar.getIndeterminateDrawable()
                .mutate()
                .setColorFilter(colorRes, PorterDuff.Mode.SRC_ATOP);
    }

    public void setTypeface(Typeface typeface) {
        this.mTypeface = typeface;
    }

    public void setAnimationInDirection(int inDirection) {
        mCurrentInDirection = inDirection;
    }

    public void setText(String text) {
        if (text != null) {
            mButtonText = text;
            if (mTextSwitcherReady) {
                mTextSwitcher.setText(mButtonText);
            }
        }
    }

    public void setLoadingText(String text) {
        if (text != null) {
            mLoadingMessage = text;
        }
    }

    public void setTextFactory(ViewSwitcherFactory factory) {
        mTextSwitcher.removeAllViewsInLayout();
        mTextSwitcher.setFactory(factory);
        mTextSwitcher.setText(mButtonText);
    }

    public void showLoading() {
        if (!mIsLoadingShowing) {
            mProgressBar.setVisibility(View.VISIBLE);
            mTextSwitcher.setText(mLoadingMessage);
            mIsLoadingShowing = true;
            setEnabled(false);
        }
    }

    public void showButtonText() {
        if (mIsLoadingShowing) {
            mProgressBar.setVisibility(View.GONE);
            mTextSwitcher.setText(mButtonText);
            mIsLoadingShowing = false;
            setEnabled(true);
        }
    }

    public boolean isLoadingShowing() {
        return mIsLoadingShowing;
    }

    private void init(Context context, AttributeSet attrs) {
        mDefaultTextSize = getResources().getDimensionPixelSize(R.dimen.font_size_16);
        mIsLoadingShowing = false;
        LayoutInflater.from(getContext()).inflate(R.layout.view_loading_button, this, true);

        mProgressBar = (ProgressBar) findViewById(R.id.pb_progress);
        mTextSwitcher = (TextSwitcher) findViewById(R.id.pb_text);


        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.LoadingButton,
                    0, 0);
            try {
                float textSize = a.getDimensionPixelSize(R.styleable.LoadingButton_pbTextSize, mDefaultTextSize);
                setTextSize(textSize);

                String text = a.getString(R.styleable.LoadingButton_pbText);
                setText(text);

                mLoadingMessage = a.getString(R.styleable.LoadingButton_pbLoadingText);

                if (mLoadingMessage == null) {
                    mLoadingMessage = "";
                }

                int progressColor = a.getColor(R.styleable.LoadingButton_pbProgressColor, ContextCompat.getColor(context, DEFAULT_COLOR));
                setProgressColor(progressColor);

                int textColor = a.getColor(R.styleable.LoadingButton_pbTextColor, ContextCompat.getColor(context, DEFAULT_COLOR));
                setTextColor(textColor);

            } finally {
                a.recycle();
            }
        } else {
            int white = ContextCompat.getColor(context, DEFAULT_COLOR);
            mLoadingMessage = "";
            setProgressColor(white);
            setTextColor(white);
            setTextSize(mDefaultTextSize);
        }
        setupTextSwitcher();
    }

    private void setupTextSwitcher() {
        ViewSwitcherFactory factory = new ViewSwitcherFactory(getContext(), mTextColor, mTextSize, mTypeface);
        mTextSwitcher.setFactory(factory);
        mTextSwitcher.setText(mButtonText);
        mTextSwitcherReady = true;
    }

    private void setTextSize(float size) {
        mTextSize = size;
    }

    private void setTextColor(int textColor) {
        this.mTextColor = textColor;
    }

    public static class ViewSwitcherFactory implements ViewSwitcher.ViewFactory {

        //region Variables
        private final int textColor;
        private final float textSize;
        private final Typeface typeFace;
        private final Context context;
        //endregion

        //region Constructor
        public ViewSwitcherFactory(Context context, int textColor, float textSize, Typeface typeface) {
            this.context = context;
            this.textColor = textColor;
            this.textSize = textSize;
            this.typeFace = typeface;
        }
        //endregion

        @Override
        public View makeView() {
            TextView tv = new TextView(context);
            tv.setTextColor(textColor);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(typeFace);

            return tv;
        }
    }
}