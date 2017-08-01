package com.android.common.ui;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by fengyulong on 2017/4/10.
 */

public class DivisionEditText extends NoEmojiEditText {
    private final int maxLength = Integer.MAX_VALUE;
    /* 每组的长度 */
    private Integer length = 3;
    /* 分隔符 */
    private String delimiter = ",";
    private String text = "";
    private OnChangeLengthMaxListener maxListener;

    public DivisionEditText(Context context) {
        super(context);
        init();
    }

    public DivisionEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DivisionEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * 得到每组个数
     */
    public Integer getLength() {
        return length;
    }

    /**
     * 设置每组个数
     */
    public void setLength(Integer length) {
        this.length = length;
    }

    /**
     * 得到间隔符
     */
    public String getDelimiter() {
        return delimiter;
    }

    /**
     * 设置间隔符
     */
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getInputText() {
        return super.getText().toString().replace(getDelimiter(), "");
    }

    /**
     * 初始化
     */
    public void init() {

        // 内容变化监听
        this.addTextChangedListener(new DivisionTextWatcher());

        Observable.interval(50, 50, TimeUnit.MILLISECONDS)
                .onBackpressureBuffer(10000)
                .onBackpressureDrop()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        int position = getText().toString().indexOf(".");
                        if (position > -1 && getSelectionStart() > getText().toString().indexOf(".")) {
                            setSelection(position);
                        }
                    }
                });
    }

    /**
     * 若有，先去除，进行计算之后再添加
     */
    private String formatSymbol(String str) {
        str = str.replace(getDelimiter(), "");

        int position = str.indexOf('.');

        char[] chars = str.toCharArray();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < chars.length; i++) {
            if (position > -1) {
                if (i > position && (i - position - 1) % getLength() == 0 && (i - position - 1) != 0)// 每次遍历到4的倍数，就添加一个空格
                {
                    sb.append(getDelimiter());
                    sb.append(chars[i]);// 添加字符
                } else {
                    sb.append(chars[i]);// 添加字符
                }
            } else {
                if (i % getLength() == 0 && i != 0)// 每次遍历到4的倍数，就添加一个空格
                {
                    sb.append(getDelimiter());
                    sb.append(chars[i]);// 添加字符
                } else {
                    sb.append(chars[i]);// 添加字符
                }
            }
        }
        return sb.toString();
    }

    /**
     * 字符串逆序*
     *
     * @param str
     * @return
     */
    private String inversionString(String str) {
        char[] chars = str.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            sb.append(chars[chars.length - i - 1]);
        }
        return sb.toString();
    }

    public void setOnChangeLengthMaxListener(OnChangeLengthMaxListener maxListener) {
        this.maxListener = maxListener;
    }

    /**
     * EditText 长度最大化监听
     */
    public interface OnChangeLengthMaxListener {
        public void afterLengthMax();
    }

    /**
     * 文本监听
     *
     * @author Administrator
     */
    private class DivisionTextWatcher implements TextWatcher {

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String str = getText().toString().replace(" ", "").trim();

            if (str.contains(".")) {
                if (str.length() - 1 - str.toString().indexOf(".") > 2) {
                    str = str.subSequence(0, str.indexOf(".") + 3).toString();
                    setText(str);
                    setSelection(str.length());
                    return;
                }
            }
            if (str.substring(0).startsWith(".")) {
                str = "0" + str;
                setText(str);
                setSelection(1);
                return;
            }
            if (str.startsWith("0")) {
                if (str.length() > 1) {
                    if (!str.substring(1, 2).equals(".")) {
                        str = str.substring(1).toString();
                        setText(str);
                        setSelection(1);
                        return;
                    }
                } else {
                    str = "0.00";
                    str = "0";
                    setText(str);
                    setSelection(1);
                    return;
                }
            }

            int position = str.indexOf('.');
            if (position < 0) {
//                str = str + ".00";
//
//                setText(str);
//                setSelection(str.indexOf('.'));
                return;
            } else {
                if (str.length() - 1 - position == 1) {
//                    str = str + "0";
                    str = str.substring(0, position);
                    setText(str);
                    setSelection(str.indexOf('.'));
                    return;
                } else if (str.length() - 1 - position == 0) {
//                    str = str + "00";
                    str = str.substring(0, position);
                    setText(str);
                    setSelection(str.indexOf('.'));
                    return;
                }
            }

            // 统计个数
            int len = s.length();

            // 输入首字母为0之后则不显示
//            if (len > 1) {
//                if (s.toString().charAt(0) == '0') {
//                    setText("0");
//                    setSelection(1);
//                    return;
//                }
//            }

            if (len < getLength())// 长度小于要求的数
                return;
            if (count > 1) {
                return;
            }
            if (len > maxLength) {//限制输入的长度
                str = getText().toString();
                // 截取新字符串
                String newStr = str.substring(0, maxLength);
                text = inversionString(formatSymbol(inversionString(newStr)));
                maxListener.afterLengthMax();//自定义接口，实现监听回调
            } else {
                //先倒置，运算之后再倒置回来
                text = inversionString(formatSymbol(inversionString(s.toString())));//关键点
            }

            // text = inversionString(formatSymbol(inversionString(s.toString())));
            setText(text);
            setSelection(text.indexOf("."));
        }
    }
}
