package com.creditpomelo.everest.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.creditpomelo.everest.R;
import com.creditpomelo.everest.main.base.Fragment;
import com.creditpomelo.everest.main.login.activity.LoginActivity;
import com.creditpomelo.everest.utils.SharedPrefsUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 贷款视图
 * Created by fengyulong on 2017/4/6.
 */

public class LoanFragment extends Fragment {
    @Bind(R.id.tv_title)
    TextView tv_title;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup viewGroup) {
        return inflater.inflate(R.layout.fragment_loan, null);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tv_title.setText("贷款");
    }

    @OnClick(R.id.ll_repayment_layout)
    void repayment() {
        if (!SharedPrefsUtils.isLogin()) {
            startActivity(new Intent(context, LoginActivity.class));
            return;
        }


    }

    @OnClick(R.id.ll_limit_manager_layout)
    void limitManager() {
        if (!SharedPrefsUtils.isLogin()) {
            startActivity(new Intent(context, LoginActivity.class));
            return;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
