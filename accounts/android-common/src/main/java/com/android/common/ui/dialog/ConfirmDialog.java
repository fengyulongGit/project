package com.android.common.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.common.R;
import com.android.common.utils.ValidateUtil;

/**
 * Created by COOLWEN on 2016/6/21.
 */
public class ConfirmDialog {

    public static final int Mode_LEFT_RIGHT = 1;//左右按钮
    public static final int Mode_BOTTOM = 2;//下方单个按钮
    private Context context;
    private String title;
    private String content;
    private String leftBtnContent;
    private String rightBtnContent;
    private String bottomBtnContent;
    private OnBtnLeftClickListener onBtnLeftClickListener;
    private OnBtnRightClickListener onBtnRightClickListener;
    private OnBtnBottomClickListener onBtnBottomClickListener;
    private OnBtnCloseClickListener onBtnCloseClickListener;
    private OnDismissListener onDismissListener;
    private int mode = Mode_LEFT_RIGHT;
    private int contentGravity = Gravity.CENTER;

    private boolean isShowCloseBtn = false;

    public ConfirmDialog(Context context) {
        this.context = context;
    }

    public Dialog create() {
        final Dialog dialog = new Dialog(context, R.style.Dialog_Fullscreen);
        dialog.setCanceledOnTouchOutside(false);//点击弹窗外面   弹窗消失

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);  //此处可以设置dialog显示的位置
//        window.setWindowAnimations(R.style.mystyle);  //添加动画
//        dialog.show();
        window.setContentView(R.layout.dialog_confirm);

        TextView tvTitle = (TextView) window.findViewById(R.id.dialog_confirm_title);
        tvTitle.setText(title);
        tvTitle.setVisibility(TextUtils.isEmpty(title) ? View.GONE : View.VISIBLE);

        TextView tvContent = (TextView) window.findViewById(R.id.dialog_confirm_content);
        tvContent.setText(content);
        tvContent.setGravity(contentGravity);

        Button btnClose = (Button) window.findViewById(R.id.dialog_confirm_btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onBtnCloseClickListener != null) {
                    onBtnCloseClickListener.onClick(v);
                }
            }
        });
        btnClose.setVisibility(isShowCloseBtn ? View.VISIBLE : View.GONE);

        Button btnLeft = (Button) window.findViewById(R.id.dialog_confirm_btn_left);
        if (ValidateUtil.isNotNull(leftBtnContent)) {
            btnLeft.setText(leftBtnContent);
        }
        btnLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onBtnLeftClickListener != null) {
                    onBtnLeftClickListener.onClick(v);
                }
            }
        });

        Button btnRight = (Button) window.findViewById(R.id.dialog_confirm_btn_right);
        if (ValidateUtil.isNotNull(rightBtnContent)) {
            btnRight.setText(rightBtnContent);
        }
        btnRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onBtnRightClickListener != null) {
                    onBtnRightClickListener.onClick(v);
                }
            }
        });

        Button btnBottom = (Button) window.findViewById(R.id.dialog_confirm_btn_bottom);
        if (ValidateUtil.isNotNull(bottomBtnContent)) {
            btnBottom.setText(bottomBtnContent);
        }
        btnBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (onBtnBottomClickListener != null) {
                    onBtnBottomClickListener.onClick(view);
                }
            }
        });

        if (Mode_LEFT_RIGHT == mode) {
            window.findViewById(R.id.ll_left_right_btn_layout).setVisibility(View.VISIBLE);
        } else if (Mode_BOTTOM == mode) {
            window.findViewById(R.id.ll_bottom_btn_layout).setVisibility(View.VISIBLE);
        }


        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (onDismissListener != null) {
                    onDismissListener.onDismiss(dialogInterface);
                }
            }
        });

        return dialog;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setContentGravity(int contentGravity) {
        this.contentGravity = contentGravity;
    }

    public void setLeftBtnContent(String leftBtnContent) {
        this.leftBtnContent = leftBtnContent;
    }

    public void setRightBtnContent(String rightBtnContent) {
        this.rightBtnContent = rightBtnContent;
    }

    public void setBottomBtnContent(String bottomBtnContent) {
        this.bottomBtnContent = bottomBtnContent;
    }

    public void setShowCloseBtn(boolean showCloseBtn) {
        isShowCloseBtn = showCloseBtn;
    }

    public void setOnBtnLeftClickListener(OnBtnLeftClickListener onBtnLeftClickListener) {
        this.onBtnLeftClickListener = onBtnLeftClickListener;
    }

    public void setOnBtnRightClickListener(OnBtnRightClickListener onBtnRightClickListener) {
        this.onBtnRightClickListener = onBtnRightClickListener;
    }

    public void setOnBtnBottomClickListener(OnBtnBottomClickListener onBtnBottomClickListener) {
        this.onBtnBottomClickListener = onBtnBottomClickListener;
    }

    public void setOnBtnCloseClickListener(OnBtnCloseClickListener onBtnCloseClickListener) {
        this.onBtnCloseClickListener = onBtnCloseClickListener;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public interface OnBtnLeftClickListener {
        void onClick(View v);
    }

    public interface OnBtnRightClickListener {
        void onClick(View v);
    }

    public interface OnBtnBottomClickListener {
        void onClick(View v);
    }

    public interface OnBtnCloseClickListener {
        void onClick(View v);
    }

    public interface OnDismissListener {
        void onDismiss(DialogInterface var1);
    }
}
