package net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.model.PositionData;

import java.util.List;


/**
 * 包裹住内容区域的指示器，类似天天快报的切换效果，需要和IMeasurablePagerTitleView配合使用
 * 博客: http://hackware.lucode.net
 * Created by hackware on 2016/6/26.
 */
public class WrapPagerIndicator extends View implements IPagerIndicator {
    public static final int MODE_MATCH_EDGE = 0;   // 直线宽度 == title宽度 - 2 * mXOffset
    public static final int MODE_WRAP_CONTENT = 1;    // 直线宽度 == title内容宽度 - 2 * mXOffset
    public static final int MODE_EXACTLY = 2;  // 直线宽度 == mLineWidth

    private int mMode;  // 默认为MODE_MATCH_EDGE模式

    private int mVerticalPadding;
    private int mHorizontalPadding;
    private int mFillColor;
    private float mRoundRadius;
    private Interpolator mStartInterpolator = new LinearInterpolator();
    private Interpolator mEndInterpolator = new LinearInterpolator();

    private List<PositionData> mPositionDataList;
    private Paint mPaint;

    private RectF mRect = new RectF();
    private boolean mRoundRadiusSet;

    public WrapPagerIndicator(Context context) {
        this(context, null);
    }

    public WrapPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WrapPagerIndicator(Context context, AttributeSet attrs, int defStyleAtt) {
        super(context, attrs, defStyleAtt);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mVerticalPadding = UIUtil.dip2px(context, 6);
        mHorizontalPadding = UIUtil.dip2px(context, 10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(mFillColor);
        canvas.drawRoundRect(mRect, mRoundRadius, mRoundRadius, mPaint);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mPositionDataList == null || mPositionDataList.isEmpty()) {
            return;
        }

        // 计算锚点位置
        PositionData current = FragmentContainerHelper.getImitativePositionData(mPositionDataList, position);
        PositionData next = FragmentContainerHelper.getImitativePositionData(mPositionDataList, position + 1);

        if (mMode == MODE_MATCH_EDGE) {
            mRect.left = current.mLeft - mHorizontalPadding + (next.mLeft - current.mLeft) * mEndInterpolator.getInterpolation(positionOffset);
            mRect.top = current.mTop - mVerticalPadding;
            mRect.right = current.mRight + mHorizontalPadding + (next.mRight - current.mRight) * mStartInterpolator.getInterpolation(positionOffset);
            mRect.bottom = current.mBottom + mVerticalPadding;
        } else if (mMode == MODE_WRAP_CONTENT) {
            mRect.left = current.mContentLeft - mHorizontalPadding + (next.mContentLeft - current.mContentLeft) * mEndInterpolator.getInterpolation(positionOffset);
            mRect.top = current.mContentTop - mVerticalPadding;
            mRect.right = current.mContentRight + mHorizontalPadding + (next.mContentRight - current.mContentRight) * mStartInterpolator.getInterpolation(positionOffset);
            mRect.bottom = current.mContentBottom + mVerticalPadding;
        } else {
            mRect.left = current.mLeft + (current.width() - mHorizontalPadding) / 2;
            mRect.top = current.mTop + (current.height() - mVerticalPadding) / 2;
            mRect.right = current.mRight + (current.width() + mHorizontalPadding) / 2;
            mRect.bottom = current.mBottom + (current.height() + mVerticalPadding) / 2;
        }

        if (!mRoundRadiusSet) {
            mRoundRadius = mRect.height() / 2;
        }

        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPositionDataProvide(List<PositionData> dataList) {
        mPositionDataList = dataList;
    }

    public Paint getPaint() {
        return mPaint;
    }

    public int getMode() {
        return mMode;
    }

    public void setMode(int mode) {
        if (mode == MODE_EXACTLY || mode == MODE_MATCH_EDGE || mode == MODE_WRAP_CONTENT) {
            mMode = mode;
        } else {
            throw new IllegalArgumentException("mode " + mode + " not supported.");
        }
    }

    public int getVerticalPadding() {
        return mVerticalPadding;
    }

    public void setVerticalPadding(int verticalPadding) {
        mVerticalPadding = verticalPadding;
    }

    public int getHorizontalPadding() {
        return mHorizontalPadding;
    }

    public void setHorizontalPadding(int horizontalPadding) {
        mHorizontalPadding = horizontalPadding;
    }

    public int getFillColor() {
        return mFillColor;
    }

    public void setFillColor(int fillColor) {
        mFillColor = fillColor;
    }

    public float getRoundRadius() {
        return mRoundRadius;
    }

    public void setRoundRadius(float roundRadius) {
        mRoundRadius = roundRadius;
        mRoundRadiusSet = true;
    }

    public Interpolator getStartInterpolator() {
        return mStartInterpolator;
    }

    public void setStartInterpolator(Interpolator startInterpolator) {
        mStartInterpolator = startInterpolator;
        if (mStartInterpolator == null) {
            mStartInterpolator = new LinearInterpolator();
        }
    }

    public Interpolator getEndInterpolator() {
        return mEndInterpolator;
    }

    public void setEndInterpolator(Interpolator endInterpolator) {
        mEndInterpolator = endInterpolator;
        if (mEndInterpolator == null) {
            mEndInterpolator = new LinearInterpolator();
        }
    }
}
