package com.android.common.ui.password;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.common.R;
import com.android.common.ui.safesoftboard.SoftKey;
import com.android.common.ui.safesoftboard.SoftKeyNumLayView;
import com.android.common.ui.safesoftboard.SoftKeyStatusListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by fengyulong on 2017/5/16.
 */

public class PasswordDialogActivity extends Activity implements SoftKeyStatusListener {

    PasswordLayout et_password;
    private SoftKeyNumLayView numLayView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_password_layout);
        numLayView = (SoftKeyNumLayView) findViewById(R.id.softkeyBoard_Number);
        numLayView.setSoftKeyListener(this);

        EventBus.getDefault().register(this);

//        Window window = getWindow();
//        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        window.setGravity(Gravity.CENTER);  //此处可以设置dialog显示的位置

        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参值
        p.width = d.getWidth(); // 宽度设置为屏幕的0.8
        getWindow().setAttributes(p); // 设置生效

        et_password = (PasswordLayout) findViewById(R.id.et_password);
        et_password.setInputCompleteListener(new PasswordLayout.InputCompleteListener() {
            @Override
            public void inputComplete(String password) {
                EventBus.getDefault().post(new PasswordEvent(PasswordEvent.Event.INPUT_COMPLATE, password));
                finish();
            }
        });
        et_password.getEditText().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    /**
     * 接收选中的事件
     */
    @Subscribe
    public void onEventMainThread(PasswordEvent event) {
        if (event == null) {
            return;
        }
        if (PasswordEvent.Event.CLOSE == event.getEvent()) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onPressed(SoftKey softKey) {
        if (et_password != null && et_password.getEditText() != null) {
            EditText editText = et_password.getEditText();
            editText.setText(editText.getText().toString() + softKey.getText());
            editText.setSelection(editText.length());
        }
    }

    @Override
    public void onDeleted() {
        if (et_password != null && et_password.getEditText() != null) {
            EditText editText = et_password.getEditText();
            if (!TextUtils.isEmpty(editText.getText().toString())) {
                StringBuilder sb = new StringBuilder(editText.getText().toString());
                editText.setText(sb.deleteCharAt(sb.length() - 1));
                editText.setSelection(sb.length());
            }
        }
    }

    @Override
    public void onConfirm() {
        if (et_password != null && et_password.getEditText() != null && et_password.getStrPassword().length() == 6) {
            EventBus.getDefault().post(new PasswordEvent(PasswordEvent.Event.INPUT_COMPLATE, et_password.getStrPassword()));
            finish();
        } else {
            Toast.makeText(this, "请输入6位密码", Toast.LENGTH_SHORT).show();
        }
//        close();
    }
}
