package com.hqj.universityfinance.home;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.ViewFlipper;

import com.hqj.universityfinance.utils.ConfigUtils;
import com.hqj.universityfinance.utils.MyAsyncTask;
import com.hqj.universityfinance.customview.PageIndicatorView;
import com.hqj.universityfinance.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by wang on 17-9-12.
 */

public class HomeFragment extends Fragment {



    private Banner mBanner;
    private ViewFlipper mViewFlipper;
    private ViewPager mViewPager;
    private PageIndicatorView mIndicatorView;
    private int mPageCount = 0;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContext = getActivity();
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        initView(view);
        new MyAsyncTask(ConfigUtils.TYPE_NEWS, mBanner, mContext).execute(ConfigUtils.NEWS_JSON_URL);
        new MyAsyncTask(ConfigUtils.TYPE_NOTICE, mViewFlipper, mContext).execute(ConfigUtils.NOTICE_JSON_URL);

        return view;
    }

    private void initView(View view) {
        mBanner = (Banner) view.findViewById(R.id.banner);
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        mBanner.setBannerAnimation(Transformer.DepthPage);
        mBanner.isAutoPlay(true);
        mBanner.setDelayTime(3000);
        mBanner.setIndicatorGravity(BannerConfig.CENTER);

        mViewFlipper = (ViewFlipper) view.findViewById(R.id.notice_flipper);
        mViewFlipper.setInAnimation(mContext, R.anim.slide_top_in);
        mViewFlipper.setOutAnimation(mContext, R.anim.slide_bottom_out);
        mViewFlipper.setFlipInterval(3000);
        mViewFlipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, NoticeListActivity.class);
                mContext.startActivity(intent);
            }
        });

        final List<GridView> pageViewList = createGridView(getItemsData());

        mViewPager = (ViewPager) view.findViewById(R.id.items_viewpager);
        mViewPager.setAdapter(new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(pageViewList.get(position));
                return pageViewList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(pageViewList.get(position));
            }

            @Override
            public int getCount() {
                return pageViewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });

        mIndicatorView = (PageIndicatorView) view.findViewById(R.id.indicator_view);
        mIndicatorView.setMaxNum(mPageCount);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mIndicatorView.setCurrentFocus(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private List<Map<String, Object>> getItemsData() {
        int[] iconIds = ConfigUtils.iconIds;
        String[] iconTitles = ConfigUtils.iconTitles;

        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> item = null;
        for (int i = 0; i < iconIds.length; i++) {
            item = new HashMap<>();
            item.put("icon", iconIds[i]);
            item.put("title", iconTitles[i]);
            list.add(item);
        }
        return list;
    }

    private List<GridView> createGridView(List<Map<String, Object>> data) {
        int itemsMaxNum = ConfigUtils.ITEMS_MAX_NUM;
        mPageCount = data.size() / itemsMaxNum;
        int lastPageNum = data.size() % itemsMaxNum;
        int verticalSpacing = getActivity().getResources().getDimensionPixelSize(R.dimen.icons_spacing_vertical);
        boolean lastPageNotFull = false;
        if (lastPageNum > 0) {
            lastPageNotFull = true;
            mPageCount += 1;
        }

        List<GridView> pageViewList = new ArrayList<>();
        GridView gridView = null;
        SimpleAdapter adapt = null;
        for (int i = 0; i < mPageCount; i++) {
            gridView = new GridView(mContext);
            if (lastPageNotFull && i == mPageCount - 1) {
                adapt = new SimpleAdapter(
                        mContext,
                        data.subList(i * itemsMaxNum, data.size()),
                        R.layout.finance_item,
                        new String[] {"icon", "title"},
                        new int[]{R.id.item_icon, R.id.item_title});
            } else {
                adapt = new SimpleAdapter(
                        mContext,
                        data.subList(i * itemsMaxNum, (i+1) *itemsMaxNum),
                        R.layout.finance_item,
                        new String[] {"icon", "title"},
                        new int[]{R.id.item_icon, R.id.item_title});
            }

            gridView.setAdapter(adapt);
            gridView.setNumColumns(ConfigUtils.ITEM_COUNT_X);
            gridView.setVerticalSpacing(verticalSpacing);
            pageViewList.add(gridView);
        }

        return pageViewList;
    }
}
