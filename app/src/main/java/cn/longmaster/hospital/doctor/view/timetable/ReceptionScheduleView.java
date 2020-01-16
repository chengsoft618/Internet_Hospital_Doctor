package cn.longmaster.hospital.doctor.view.timetable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorVisitingItem;
import cn.longmaster.utils.DisplayUtil;

/**
 * 接诊时间表
 * Created by YY on 2019-06-11.
 */
public class ReceptionScheduleView extends View {
    private final int ITEM_HEIGHT = DisplayUtil.dp2px(25);//行高
    private final int TITEL_FONT_SIZE = DisplayUtil.sp2px(10);//标题文字大小
    private final int ROW_NUM = 12;//行数
    private final int COL_NUM = 6;//列数
    private final String[] hours = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00"};
    private final String[] weekday = {"周一", "周二", "周三", "周四", "周五"};
    private final int BEGIN_WEEKDAY = 1;
    private final int END_WEEKDAY = BEGIN_WEEKDAY + weekday.length - 1;
    private final int MIN_BEGIN_HOUR = 8;
    private final int MAX_END_HOUR = MIN_BEGIN_HOUR + hours.length;

    private int screenWidth;
    private int tableHeight;
    private int leftMargin;
    private int rightMargin;

    private List<DoctorVisitingItem> receptionScheduleInfos = new ArrayList<>();

    public ReceptionScheduleView(Context context) {
        this(context, null, 0);
    }

    public ReceptionScheduleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReceptionScheduleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ReceptionScheduleView, defStyleAttr, 0);
        initTypedArray(typedArray);
    }

    private void initTypedArray(TypedArray typedArray) {
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.ReceptionScheduleView_leftMargin:
                    leftMargin = typedArray.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0f, getResources().getDisplayMetrics()));
                    break;

                case R.styleable.ReceptionScheduleView_rightMargin:
                    rightMargin = typedArray.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0f, getResources().getDisplayMetrics()));
                    break;
                default:
                    break;
            }
        }
        typedArray.recycle();
    }

    public void setReceptionSchedules(List<DoctorVisitingItem> receptionSchedules) {
        receptionScheduleInfos = receptionSchedules;
        if (!receptionScheduleInfos.isEmpty()) {
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        screenWidth = MeasureSpec.getSize(widthMeasureSpec);
        tableHeight = ROW_NUM * ITEM_HEIGHT + ROW_NUM + DisplayUtil.dp2px(10);
        setMeasuredDimension(screenWidth, tableHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setColor(ContextCompat.getColor(getContext(), R.color.color_ededed));
        p.setAntiAlias(true);
//        //顶部外圆角
//        RectF outRoundRectF = new RectF(leftMargin, 0, screenWidth - rightMargin, 50);
//        canvas.drawRoundRect(outRoundRectF, ROUND_RADIUS, ROUND_RADIUS, p);
//        //顶部内圆角
//        p.setColor(Color.WHITE);
//        RectF innerRoundRectF = new RectF(leftMargin, 1, screenWidth - rightMargin, 49);
//        canvas.drawRoundRect(innerRoundRectF, ROUND_RADIUS + 2, ROUND_RADIUS + 2, p);
//        //外框
//        p.setColor(ContextCompat.getColor(getContext(), R.color.color_ededed));
//        RectF bgRectF = new RectF(leftMargin, ROUND_RADIUS, screenWidth - rightMargin, ITEM_HEIGHT * ROW_NUM);
//        canvas.drawRect(bgRectF, p);
//        //内框
//        RectF contentRectF = new RectF(leftMargin + 1, ROUND_RADIUS, screenWidth - rightMargin - 1, ITEM_HEIGHT * ROW_NUM - 1);
//        p.setColor(Color.WHITE);
//        canvas.drawRect(contentRectF, p);
        //竖线
        int cellWidth = (screenWidth - leftMargin - rightMargin) / COL_NUM;
//        p.setColor(ContextCompat.getColor(getContext(), R.color.color_ededed));
//        for (int i = 0; i < COL_NUM - 1; i++) {
//            canvas.drawLine(leftMargin + (i + 1) * cellWidth, 0, leftMargin + (i + 1) * cellWidth + 1, ITEM_HEIGHT * ROW_NUM, p);
//        }
        //横线
        for (int i = 0; i < ROW_NUM - 1; i++) {
            canvas.drawLine(leftMargin + cellWidth, (i + 1) * ITEM_HEIGHT, screenWidth - rightMargin, (i + 1) * ITEM_HEIGHT + 1, p);
        }
        p.setTextAlign(Paint.Align.CENTER);
        p.setColor(ContextCompat.getColor(getContext(), R.color.color_666666));
        p.setTextSize(TITEL_FONT_SIZE);
        //时间
        for (int i = 0; i < hours.length; i++) {
            canvas.drawText(hours[i], leftMargin + cellWidth / 2, (i + 1) * ITEM_HEIGHT + TITEL_FONT_SIZE / 2 + 5, p);
        }
        //星期
        for (int i = 0; i < weekday.length; i++) {
            canvas.drawText(weekday[i], leftMargin + (i + 1) * cellWidth + cellWidth / 2, (ITEM_HEIGHT - TITEL_FONT_SIZE) / 2 + TITEL_FONT_SIZE - 5, p);
        }
        for (int i = 1; i <= weekday.length; i++) {
            p.setColor(ContextCompat.getColor(getContext(), R.color.color_dce2f1));
            int bgFromX = leftMargin + cellWidth + (i - 1) * cellWidth + 1;
            int bgEndX = bgFromX + cellWidth - 2;
            //色块背景
            int bgFromY = ITEM_HEIGHT + 4;
            int bgEndY = ITEM_HEIGHT + 10 * ITEM_HEIGHT;
            RectF rectF = new RectF(bgFromX + DisplayUtil.dp2px(10), bgFromY, bgEndX - DisplayUtil.dp2px(10), bgEndY);
            canvas.drawRect(rectF, p);
        }
        //色块
        for (DoctorVisitingItem info : receptionScheduleInfos) {
            if (info.getWeekNum() < BEGIN_WEEKDAY || info.getWeekNum() > END_WEEKDAY) {
                continue;
            }
            //色块背景
            int beginHour = Integer.valueOf(info.getBeginDt().substring(0, info.getBeginDt().indexOf(".")));
            if (beginHour < MIN_BEGIN_HOUR || beginHour > MAX_END_HOUR) {
                continue;
            }
            int endHour = Integer.valueOf(info.getEndDt().substring(0, info.getEndDt().indexOf(".")));
            if (endHour < MIN_BEGIN_HOUR || endHour > MAX_END_HOUR || endHour < beginHour) {
                continue;
            }

            int bgFromY = 0;
            if (info.getBeginDt().contains(".0")) {
                bgFromY = ITEM_HEIGHT + (beginHour - MIN_BEGIN_HOUR) * ITEM_HEIGHT + 1;
            } else {
                bgFromY = ITEM_HEIGHT + (beginHour - MIN_BEGIN_HOUR) * ITEM_HEIGHT + ITEM_HEIGHT / 2 + 4;
            }

            int bgEndY = 0;
            if (info.getEndDt().contains(".0")) {
                bgEndY = ITEM_HEIGHT + (endHour - MIN_BEGIN_HOUR) * ITEM_HEIGHT - 1;
            } else {
                bgEndY = ITEM_HEIGHT + (endHour - MIN_BEGIN_HOUR) * ITEM_HEIGHT + ITEM_HEIGHT / 2 + 4;
            }

            int bgFromX = leftMargin + cellWidth + (info.getWeekNum() - 1) * cellWidth + 1;
            int bgEndX = bgFromX + cellWidth - 2;
            if (info.getType() == 1) {
                p.setColor(ContextCompat.getColor(getContext(), R.color.color_96b1ea));
            } else if (info.getType() == 2) {
                p.setColor(ContextCompat.getColor(getContext(), R.color.color_ffdcb2));
            }
            if (info.isRecommend()) {
                p.setColor(ContextCompat.getColor(getContext(), R.color.color_6191e2));
            }
            RectF rectF = new RectF(bgFromX + DisplayUtil.dp2px(10), bgFromY, bgEndX - DisplayUtil.dp2px(10), bgEndY);
            canvas.drawRect(rectF, p);
        }


        int recommendLeft = leftMargin;
        int recommendRight = recommendLeft + DisplayUtil.dp2px(10);
        int top = tableHeight - DisplayUtil.dp2px(20);
        int bottom = tableHeight - DisplayUtil.dp2px(10);
        p.setColor(ContextCompat.getColor(getContext(), R.color.color_6191e2));
        RectF rectF = new RectF(recommendLeft, top, recommendRight, bottom + 2);
        canvas.drawRoundRect(rectF, 2, 2, p);
        p.setColor(ContextCompat.getColor(getContext(), R.color.color_1a1a1a));
        p.setTextSize(DisplayUtil.sp2px(12));
        canvas.drawText("推荐预约时段", recommendRight + DisplayUtil.dp2px(38), bottom, p);

        int canLeft = recommendRight + DisplayUtil.dp2px(76);
        int canRight = canLeft + DisplayUtil.dp2px(10);
        p.setColor(ContextCompat.getColor(getContext(), R.color.color_96b1ea));
        rectF = new RectF(canLeft, top, canRight, bottom);
        canvas.drawRoundRect(rectF, 2, 2, p);
        p.setColor(ContextCompat.getColor(getContext(), R.color.color_1a1a1a));
        p.setTextSize(DisplayUtil.sp2px(12));
        canvas.drawText("可预约时段", canRight + DisplayUtil.dp2px(32), bottom, p);

        int otherLeft = canRight + DisplayUtil.dp2px(70);
        int otherRight = otherLeft + DisplayUtil.dp2px(10);
        p.setColor(ContextCompat.getColor(getContext(), R.color.color_dce2f1));
        rectF = new RectF(otherLeft, top, otherRight, bottom);
        canvas.drawRoundRect(rectF, 2, 2, p);
        p.setColor(ContextCompat.getColor(getContext(), R.color.color_1a1a1a));
        p.setTextSize(DisplayUtil.sp2px(12));
        canvas.drawText("其他时段", otherRight + DisplayUtil.dp2px(27), bottom, p);

        int canNotLeft = otherRight + DisplayUtil.dp2px(58);
        int canNotRight = canNotLeft + DisplayUtil.dp2px(10);
        p.setColor(ContextCompat.getColor(getContext(), R.color.color_ffdcb2));
        rectF = new RectF(canNotLeft, top, canNotRight, bottom);
        canvas.drawRoundRect(rectF, 2, 2, p);
        p.setColor(ContextCompat.getColor(getContext(), R.color.color_1a1a1a));
        p.setTextSize(DisplayUtil.sp2px(12));
        canvas.drawText("不可预约时段", canNotRight + DisplayUtil.dp2px(38), bottom, p);
    }
}
