package com.creditpomelo.accounts.main.main.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.common.utils.AppManager;
import com.creditpomelo.accounts.R;
import com.creditpomelo.accounts.main.base.AppCompatActivity;
import com.creditpomelo.accounts.main.main.fragment.SettingsFragment;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnCheckedChanged;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.fl_content)
    FrameLayout fl_content;

    @BindView(R.id.tab_group)
    RadioGroup tab_group;

    @BindView(R.id.tab_main)
    RadioButton tab_main;//首页

    @BindView(R.id.tab_audit)
    RadioButton tab_audit;//待审核

    @BindView(R.id.tab_consult)
    RadioButton tab_consult;//查询

    @BindView(R.id.tab_settings)
    RadioButton tab_settings;//设置

    //    private MainFragment mainFragment;
//    private AuditFragment auditFragment;
//    private ConsultFragment consultFragment;
    private SettingsFragment settingsFragment;

    private Fragment currentFragment;

    @Override
    protected int layoutContentResID() {
        return R.layout.activity_main;
    }

    @Override
    protected int layoutTitleResID() {
        return 0;
    }

    /**
     * 加载布局
     */
    @Override
    protected void initViewData() {
//        mainFragment = new MainFragment();
//        auditFragment = new AuditFragment();
//        consultFragment = new ConsultFragment();
        settingsFragment = new SettingsFragment();

        tab_group.check(R.id.tab_main);

        //版本升级检测
//        VersionCheckUtils.getInstance(context).checkVersion();
    }

    @Override
    protected void requestData() {

    }

    /**
     * tab页签的焦点选择监听
     */
    @OnCheckedChanged({R.id.tab_main, R.id.tab_audit, R.id.tab_consult, R.id.tab_settings})
    void onTabCheckedChanged(RadioButton tabBtn, boolean checked) {
        if (checked) {
            switch (tabBtn.getId()) {
//                case R.id.tab_main: {
//                    switchContent(mainFragment);
//                    break;
//                }
//                case R.id.tab_audit: {
//                    switchContent(auditFragment);
//                    break;
//                }
//                case R.id.tab_consult: {
//                    switchContent(consultFragment);
//                    break;
//                }
                case R.id.tab_settings: {
                    switchContent(settingsFragment);
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

    private long exitTime = 0;

    /**
     * 按两次退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                showToast("再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                // Process.kill或者 System.exit之类的方法杀死进程前用来保存统计数据
                AppManager.getAppManager().AppExit(context);
                MobclickAgent.onKillProcess(context);
                System.exit(0);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
