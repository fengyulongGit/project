package com.android.common.ui.safesoftboard;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.android.common.R;
import com.android.common.utils.LogUtils;

import java.lang.reflect.Method;

/**
 * 自定义软件键盘
 */
public class SoftKeyBoard extends PopupWindow implements
        SoftKeyStatusListener, OnClickListener, OnCheckedChangeListener {

    private static final String TAG = SoftKeyBoard.class.getSimpleName();
    private View contentView;
    private View decorView;
    private EditText edit;
    private ImageView switchType;
    private ImageView colseSoftKey;
    private RadioGroup inputTypeGroup;
    private TextView softKeyBoard_tip;
    private SoftKeyAzLayView azLayView;
    private SoftKeyNumLayView numLayView;
    private SoftKeyPunctLayView punctLayView;


    private int viewMode;
    private boolean isPendding = true;
    private int softKeyHeight;
    private int decorViewHeight;
    public OnGlobalLayoutListener contetviewLayoutListener = new OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            if (decorViewHeight == decorView.getHeight()) {
                Log.i(TAG, "updateViewDraw-->decorViewHeight=" + decorViewHeight);
                TranslateAnimation translate = new TranslateAnimation(0, 0, 0, softKeyHeight);
                translate.setDuration(200);
                translate.setFillAfter(true);
                decorView.startAnimation(translate);
            } else {
                Log.i(TAG, "updateViewDraw-->decorViewHeight=" + decorViewHeight);
                TranslateAnimation translate = new TranslateAnimation(0, 0, -softKeyHeight, 0);
                translate.setDuration(200);
                translate.setFillAfter(true);
                decorView.startAnimation(translate);
            }
        }

    };
    private StringBuilder inputStr;
    private SoftKeyStatusListener softKeyStatusListener;

    public SoftKeyBoard(Context context) {
        this(context, null);
    }

    public SoftKeyBoard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressWarnings("deprecation")
    public SoftKeyBoard(Context context, AttributeSet attrs,
                        int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        decorView = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        contentView = View.inflate(context, R.layout.layout_keyboard, null);
        colseSoftKey = (ImageView) contentView.findViewById(R.id.softkeyBoard_colse);
        switchType = (ImageView) contentView.findViewById(R.id.softkeyBoard_switchType);
        inputTypeGroup = (RadioGroup) contentView.findViewById(R.id.sofkeyBoard_Type);
        softKeyBoard_tip = (TextView) contentView.findViewById(R.id.softKeyBoard_tip);
        azLayView = (SoftKeyAzLayView) contentView.findViewById(R.id.softkeyBoard_Az);
        numLayView = (SoftKeyNumLayView) contentView.findViewById(R.id.softkeyBoard_Number);
        punctLayView = (SoftKeyPunctLayView) contentView.findViewById(R.id.softkeyBoard_Punct);
        contentView.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        azLayView.setSoftKeyListener(this);
        numLayView.setSoftKeyListener(this);
        punctLayView.setSoftKeyListener(this);
//        switchType.setOnClickListener(this);
        colseSoftKey.setOnClickListener(this);
        inputTypeGroup.setOnCheckedChangeListener(this);
        softKeyHeight = contentView.getMeasuredHeight();
        decorViewHeight = decorView.getHeight();
        setFocusable(true);
        setOutsideTouchable(false);
        setContentView(contentView);
        setBackgroundDrawable(new BitmapDrawable());
        setWidth(decorView.getWidth());
        setHeight(softKeyHeight);
        setAnimationStyle(android.R.style.Animation_InputMethod);
        setPopupWindowTouchModal(false);
        //contentView.getViewTreeObserver().addOnGlobalLayoutListener(contetviewLayoutListener);
    }

    /**
     * Set whether this window is touch modal or if outside touches will be sent
     * to
     * other windows behind it.
     */
    public void setPopupWindowTouchModal(boolean touchModal) {

        Method method;
        try {

            method = PopupWindow.class.getDeclaredMethod("setTouchModal",
                    boolean.class);
            method.setAccessible(true);
            method.invoke(this, touchModal);

        } catch (Exception e) {
            LogUtils.e(e.toString());
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.softkeyBoard_colse) {
            close();

        } else if (i == R.id.softkeyBoard_switchType) {
            if (inputTypeGroup.getVisibility() == View.VISIBLE) {
                softKeyBoard_tip.setVisibility(View.VISIBLE);
                inputTypeGroup.setVisibility(View.GONE);
            } else {
                softKeyBoard_tip.setVisibility(View.GONE);
                inputTypeGroup.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
//        softKeyBoard_tip.setVisibility(View.VISIBLE);
//        inputTypeGroup.setVisibility(View.GONE);
        inputTypeGroup.check(checkedId);
        Log.i(TAG, "11111");
        updateViewByMode(mapViewModeById(checkedId));
    }

    /**
     * 更新显示的视图
     *
     * @param viewMode
     */
    public void updateViewByMode(int viewMode) {
        azLayView.setVisibility(View.INVISIBLE);
        numLayView.setVisibility(View.INVISIBLE);
        punctLayView.setVisibility(View.INVISIBLE);
        switch (viewMode) {
            case SoftKeyView.MODE_NUMBER:
                numLayView.setVisibility(View.VISIBLE);
                break;

            case SoftKeyView.MODE_AZ:
                azLayView.setVisibility(View.VISIBLE);
                break;
            case SoftKeyView.MODE_PUNCT:
                punctLayView.setVisibility(View.VISIBLE);
                break;
            case SoftKeyView.MODE_DEFAULT:
                azLayView.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 显示输入法
     */
    public void show() {
        if (edit == null) {
            new IllegalAccessError("safeEdit is null refrence");
        }
        inputStr = new StringBuilder(edit.getText().toString());
        showAsDropDown(decorView, 0, -getHeight());
        inputTypeGroup.check(mapCheckedIdByMode(getInputType(edit)));
        //updateViewByMode(getInputType(edit));
    }

    /**
     * 释放相关的资源
     */
    public void recycle() {
        edit = null;
        decorView = null;
    }

    /**
     * 关闭输入法
     */
    public void close() {
        if (isShowing()) {
            recycle();
            dismiss();
        }
    }

    /**
     * 重新刷新页面排版
     */
    public void updateViewDraw() {
        if (isPendding) {

        }
    }

    /**
     * 获取输入文本的类型
     *
     * @param edit
     * @return
     */
    public int getInputType(EditText edit) {
        if (edit == null) {
            return SoftKeyView.MODE_DEFAULT;
        }
        if (EditorInfo.TYPE_CLASS_PHONE == edit.getInputType() || edit.getInputType() == EditorInfo.TYPE_CLASS_NUMBER) {
            contentView.findViewById(R.id.softBoard_Az).setVisibility(View.GONE);
            contentView.findViewById(R.id.softBoard_Punct).setVisibility(View.GONE);
            return SoftKeyView.MODE_NUMBER;
        } else {
            return SoftKeyView.MODE_DEFAULT;
        }
    }

    /**
     * 映射出视图模式
     *
     * @return
     */
    public int mapViewModeById(int checkedId) {
        if (checkedId == R.id.softBoard_Punct) {
            return SoftKeyView.MODE_PUNCT;
        } else if (checkedId == R.id.softBoard_Az) {
            return SoftKeyView.MODE_AZ;
        } else if (checkedId == R.id.softBoard_Number) {
            return SoftKeyView.MODE_NUMBER;
        } else {
            return SoftKeyView.MODE_AZ;
        }
    }

    /**
     * 映射出视图模式
     *
     * @return
     */
    public int mapCheckedIdByMode(int viewMode) {
        switch (viewMode) {
            case SoftKeyView.MODE_PUNCT:
                return R.id.softBoard_Punct;
            case SoftKeyView.MODE_AZ:
                return R.id.softBoard_Az;
            case SoftKeyView.MODE_NUMBER:
                return R.id.softBoard_Number;
            default:
                return R.id.softBoard_Az;
        }
    }

    @Override
    public void onPressed(SoftKey softKey) {
        if (edit != null) {
            inputStr.append(softKey.getText());
            edit.setText(inputStr.toString());
            edit.setSelection(edit.length());

            if (softKeyStatusListener != null) {
                softKeyStatusListener.onPressed(softKey);
            }
        }
    }

    @Override
    public void onDeleted() {
        if (edit != null && (!TextUtils.isEmpty(inputStr.toString()))) {
            inputStr.setLength(0);
            inputStr.append(edit.getText().toString());
            edit.setText(inputStr.deleteCharAt(inputStr.length() - 1));
            edit.setSelection(inputStr.length());

            if (softKeyStatusListener != null) {
                softKeyStatusListener.onDeleted();
            }
        }
    }

    @Override
    public void onConfirm() {
        close();
        if (softKeyStatusListener != null) {
            softKeyStatusListener.onConfirm();
        }
    }

    public void setEdit(EditText edit) {
        this.edit = edit;
    }

    public void setViewMode(int viewMode) {
        if (viewMode != this.viewMode) {
            this.viewMode = viewMode;
            updateViewByMode(viewMode);
        }
    }

    public boolean isPendding() {
        return isPendding;
    }

    public void setPendding(boolean isPendding) {
        this.isPendding = isPendding;
    }

    public void setSoftKeyStatusListener(SoftKeyStatusListener softKeyStatusListener) {
        this.softKeyStatusListener = softKeyStatusListener;
    }
}
