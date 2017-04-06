package com.youxin.purse.main.base;

import android.view.View;

import com.android.common.app.BaseFragment;
import com.umeng.analytics.MobclickAgent;

/**
 * @author fengyulong
 * @date 创建时间：2016年7月12日 下午12:41:14
 */
public abstract class Fragment extends BaseFragment {


    @Override
    public void initTitleBar(View view) {

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(context);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(context);
    }
}
