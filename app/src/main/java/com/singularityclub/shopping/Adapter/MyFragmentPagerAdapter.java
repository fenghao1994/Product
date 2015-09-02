package com.singularityclub.shopping.Adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.singularityclub.shopping.Fragment.ProIntroductionFragment;
import com.singularityclub.shopping.Fragment.ProIntroductionFragment_;
import com.singularityclub.shopping.Fragment.ProductionDataFragment;
import com.singularityclub.shopping.Fragment.ProductionDataFragment_;
import com.singularityclub.shopping.Model.ProductionItem;

import java.util.ArrayList;

/**
 * viewpager适配器
 * Created by Howe on 2015/8/21.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragments;
    private ArrayList<String> mTitles;

    public MyFragmentPagerAdapter(FragmentManager fm, ProductionItem product) {
        super(fm);
        mFragments = new ArrayList<>();
        mTitles = new ArrayList<>();

        mTitles.add("商品介绍");
        mTitles.add("规格参数");

        ProductionDataFragment dataFragment = new ProductionDataFragment_();
        ProIntroductionFragment introFragment = new ProIntroductionFragment_();
        mFragments.add(introFragment);
        mFragments.add(dataFragment);

//        Bundle data = new Bundle();
//        data.putSerializable("data", (Serializable) product);
//        dataFragment.setArguments(data);
//        introFragment.setArguments(data);
    }

    @Override
    public CharSequence getPageTitle(int position){
        return mTitles.get(position);
    }


    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

}
