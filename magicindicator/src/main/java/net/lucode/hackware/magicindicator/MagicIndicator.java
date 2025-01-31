package net.lucode.hackware.magicindicator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import net.lucode.hackware.magicindicator.abs.IPagerNavigator;

/**
 * 整个框架的入口，核心
 * 博客: http://hackware.lucode.net
 * Created by hackware on 2016/6/26.
 */
public class MagicIndicator extends FrameLayout {
    private IPagerNavigator mNavigator;

    public MagicIndicator(Context context) {
        this(context, null);
    }

    public MagicIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MagicIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mNavigator != null) {
            mNavigator.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    public void onPageSelected(int position) {
        if (mNavigator != null) {
            mNavigator.onPageSelected(position);
        }
    }

    public void onPageScrollStateChanged(int state) {
        if (mNavigator != null) {
            mNavigator.onPageScrollStateChanged(state);
        }
    }

    public IPagerNavigator getNavigator() {
        return mNavigator;
    }

    public void setNavigator(IPagerNavigator navigator) {
        if (mNavigator == navigator) {
            return;
        }
        if (mNavigator != null) {
            mNavigator.onDetachFromMagicIndicator();
        }
        mNavigator = navigator;
        if (mNavigator instanceof View) {
            removeAllViews();
            ViewGroup.LayoutParams lp = ((View) mNavigator).getLayoutParams();
            if (lp == null)
                lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            addView((View) mNavigator, lp);
        }
        mNavigator.onAttachToMagicIndicator();
    }
}
