package cn.longmaster.doctorlibrary.util.imageloader.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import cn.longmaster.doctorlibrary.R;
import cn.longmaster.doctorlibrary.util.imageloader.ImageScaleType;

/**
 * 简单的图片显示,支持三种图片显示内容，默认 {@link ImageScaleType#CENTER_CROP}
 * <p/>
 * <pre>
 * xmlns:zdxing="http://schemas.android.com/apk/res/应用包名"
 *
 * &#60;com.zengdexing.imageloader.view.SimpleImageView
 *        android:id="@+id/simplaimageview"
 *        android:layout_width="match_parent"
 *        android:layout_height="match_parent"
 *        zdxing:scaleType="centerCrop"
 *        zdxing:src="@drawable/meinv" /&#62;
 * </pre>
 *
 * @author zdxing 2015年1月30日
 */
public class SimpleImageView extends View {
    private boolean mHaveFrame = false;

    private Drawable drawable;
    private ImageScaleType imageScaleType;
    private Matrix matrix;

    private static final ImageScaleType[] surpotImageScaleType = {ImageScaleType.FIT_XY, ImageScaleType.CENTER_CROP,
            ImageScaleType.CENTER_INSIDE, ImageScaleType.START_CROP};

    public SimpleImageView(Context context) {
        this(context, null);
    }

    public SimpleImageView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        matrix = new Matrix();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SimpleImageView);

        Drawable drawable = typedArray.getDrawable(R.styleable.SimpleImageView_src);
        setImageDrawable(drawable);

        int scaleType = typedArray.getInt(R.styleable.SimpleImageView_scaleType, 1);
        setImageScaleType(surpotImageScaleType[scaleType]);
        typedArray.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mHaveFrame = true;
        bindDrawable();
    }

    /**
     * 设置要显示的图片
     *
     * @param drawable
     */
    public void setImageDrawable(Drawable drawable) {
        if (this.drawable != null) {
            this.drawable.setCallback(null);
        }
        this.drawable = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
        }
        bindDrawable();
        invalidate();
    }

    @Override
    public void invalidateDrawable(Drawable drawable) {
        if (drawable == this.drawable) {
            invalidate();
        } else {
            super.invalidateDrawable(drawable);
        }
    }

    /**
     * 设置图片缩放格式
     *
     * @param imageScaleType {@link ImageScaleType}
     */
    public void setImageScaleType(ImageScaleType imageScaleType) {
        this.imageScaleType = imageScaleType;
        bindDrawable();
        invalidate();
    }

    private void bindDrawable() {
        if (drawable == null || !mHaveFrame) {
            return;
        }

        matrix.reset();

        int dwidth = drawable.getIntrinsicWidth();
        int dheight = drawable.getIntrinsicHeight();
        int vwidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int vheight = getHeight() - getPaddingTop() - getPaddingRight();

        if (dwidth <= 0 || dheight <= 0 || imageScaleType == ImageScaleType.FIT_XY) {
            drawable.setBounds(0, 0, vwidth, vheight);
        } else if (imageScaleType == ImageScaleType.CENTER_CROP) {
            drawable.setBounds(0, 0, dwidth, dheight);
            float scale;
            float dx = 0, dy = 0;
            if (dwidth * vheight > vwidth * dheight) {
                scale = (float) vheight / (float) dheight;
                dx = (vwidth - dwidth * scale) * 0.5f;
            } else {
                scale = (float) vwidth / (float) dwidth;
                dy = (vheight - dheight * scale) * 0.5f;
            }

            matrix.setScale(scale, scale);
            matrix.postTranslate((int) (dx + 0.5f), (int) (dy + 0.5f));
        } else if (imageScaleType == ImageScaleType.CENTER_INSIDE) {
            drawable.setBounds(0, 0, dwidth, dheight);
            float scale;
            float dx;
            float dy;

            scale = Math.min((float) vwidth / (float) dwidth, (float) vheight / (float) dheight);
            dx = (int) ((vwidth - dwidth * scale) * 0.5f + 0.5f);
            dy = (int) ((vheight - dheight * scale) * 0.5f + 0.5f);

            matrix.setScale(scale, scale);
            matrix.postTranslate(dx, dy);
        } else if (imageScaleType == ImageScaleType.START_CROP) {
            drawable.setBounds(0, 0, dwidth, dheight);
            float scale;
            float dx = 0, dy = 0;
            if (dwidth * vheight > vwidth * dheight) {
                scale = (float) vheight / (float) dheight;
                dx = (vwidth - dwidth * scale) * 0.5f;
            } else {
                scale = (float) vwidth / (float) dwidth;
                dy = (vheight - dheight * scale) * 0.5f;
            }
            matrix.setScale(scale, scale);
            matrix.postTranslate((int) (dx + 0.5f), (int) (dy + 0.5f));
        } else {
            throw new RuntimeException("不支持的缩放格式");
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (drawable == null) {
            return;
        }
        canvas.save();

        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = getWidth() - getPaddingRight();
        int bottom = getHeight() - getPaddingBottom();
        canvas.clipRect(left, top, right, bottom);

        canvas.translate(getPaddingLeft(), getPaddingTop());
        canvas.concat(matrix);
        drawable.draw(canvas);
        canvas.restore();
    }

    /**
     * 获取图片显示缩放类型
     *
     * @return 图片缩放类型
     */
    public ImageScaleType getImageScaleType() {
        return imageScaleType;
    }
}
