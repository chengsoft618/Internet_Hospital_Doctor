package cn.longmaster.doctorlibrary.util.imageloader.view;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * 圆形图片
 *
 * @author zdxing 2015年2月4日
 */
public class CircledDrawable extends Drawable {
    protected final Paint paint;
    private Paint strokePaint;
    private float radius;
    private Bitmap bitmap;
    private int strokeWidth;
    private Paint transparentPaint;

    public CircledDrawable(Bitmap bitmap, boolean isMaskLayer) {
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        radius = Math.min(bitmap.getWidth() / 2, bitmap.getHeight() / 2);

        this.bitmap = bitmap;

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setShader(bitmapShader);

        if (isMaskLayer) {
            transparentPaint = new Paint();
            transparentPaint.setAntiAlias(true);
            transparentPaint.setStyle(Paint.Style.FILL);
            transparentPaint.setColor(0x88000000);
        }
    }

    public CircledDrawable(Bitmap bitmap, boolean isMaskLayer, int strokeWidth, int strokeColor) {
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        radius = Math.min(bitmap.getWidth() / 2, bitmap.getHeight() / 2);

        this.bitmap = bitmap;

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setShader(bitmapShader);

        if (isMaskLayer) {
            transparentPaint = new Paint();
            transparentPaint.setAntiAlias(true);
            transparentPaint.setStyle(Paint.Style.FILL);
            transparentPaint.setColor(0x88000000);
        }

        this.strokeWidth = strokeWidth;
        strokePaint = new Paint();
        strokePaint.setAntiAlias(true);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(strokeColor);
        strokePaint.setStrokeWidth(strokeWidth);
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) (radius * 2);
    }

    @Override
    public int getIntrinsicWidth() {
        return (int) (radius * 2);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.translate(radius - bitmap.getWidth() / 2, radius - bitmap.getHeight() / 2);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, radius, paint);
        if (transparentPaint != null) {
            canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, radius, transparentPaint);
        }
        if (strokePaint != null) {
            canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, radius - strokeWidth / 1.99f, strokePaint);
        }
        canvas.restore();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }
}
