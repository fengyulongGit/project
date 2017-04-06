package com.youxin.purse.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youxin.purse.R;
import com.youxin.purse.main.base.Fragment;

/**
 * 我-视图
 * Created by fengyulong on 2017/4/6.
 */

public class MyFragment extends Fragment {
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup viewGroup) {
        return inflater.inflate(R.layout.fragment_my, null);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }
}
