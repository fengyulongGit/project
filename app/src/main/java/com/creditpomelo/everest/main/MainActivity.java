package com.creditpomelo.everest.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.creditpomelo.everest.R;
import com.creditpomelo.everest.main.base.AppCompatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

/**
 * 首页框架视图
 * Created by fengyulong on 2017/4/6.
 */

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.fl_content)
    FrameLayout fl_content;

    @Bind(R.id.tab_group)
    RadioGroup tab_group;

    @Bind(R.id.tabBtn_loan)
    RadioButton tabBtn_loan;

    @Bind(R.id.tabBtn_my)
    RadioButton tabBtn_my;

    private LoanFragment loanFragment;
    private MyFragment myFragment;
    private Fragment currentFragment;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initData() {
        loanFragment = new LoanFragment();
        myFragment = new MyFragment();

        tab_group.check(R.id.tabBtn_loan);
    }

    /**
     * tab页签的焦点选择监听
     *
     * @param tabBtn  选中的页签
     * @param checked 是否选中
     */
    @OnCheckedChanged({R.id.tabBtn_loan, R.id.tabBtn_my})
    void onTabCheckedChanged(RadioButton tabBtn, boolean checked) {
        if (checked) {

            switch (tabBtn.getId()) {
                case R.id.tabBtn_loan: {
                    switchContent(loanFragment);
                    break;
                }
                case R.id.tabBtn_my: {
                    switchContent(myFragment);
                    break;
                }
                default:
                    break;
            }
        }
    }

    /**
     * 修改显示的内容 不会重新加载
     **/
    private void switchContent(android.support.v4.app.Fragment to) {
        if (currentFragment != to) {
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            if (!to.isAdded()) { // 先判断是否被add过
                if (currentFragment != null) {
                    transaction.hide(currentFragment);
                }
                transaction.add(R.id.fl_content, to).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                if (currentFragment != null) {
                    transaction.hide(currentFragment);
                }
                transaction.show(to).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
            }
            currentFragment = to;
        }
    }
}
