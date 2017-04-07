package com.android.common.ui.gesturelock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 手势密码控件
 *
 * @author fengyulong
 */
public class GestureLockView extends View {
    private GestureLockCallBack callBack;
    private int endX = -1;
    private int endY = -1;
    private int startX;
    private int startY;
    private Paint linePaint;//连线画笔
    private Paint pointPaint;//圆心画笔
    private Paint circlePaint;//手势圆圈画笔
    private int viewWidth; // 控件的宽
    private int viewHeight; // 控件的高
    private int radius;//半径
    private Context context;
    private PointF[][] centerCxCy; // 圆心矩阵
    private int[][] data; // 密码数据矩阵
    private boolean[][] selected; // 已选矩阵 选中之后不能重选
    private List<PointF> selPointList; // 选中的圆中点集合

    private boolean isPressedDown = false;

    private String lockPin = ""; // 结果锁屏密码字符串
    private boolean isLock = false;// 是否锁定
    private boolean isFirst = true;//htc手机上会执行两次onLayout()，界面偏，避免此问题

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean isLock) {
        this.isLock = isLock;
    }

    public GestureLockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    public GestureLockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public GestureLockView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        linePaint = new Paint();//#006686
        linePaint.setColor(Color.rgb(0x21, 0xa1, 0xea));
        linePaint.setStyle(Style.FILL); // 画笔样式
        linePaint.setAntiAlias(true);
        //
        pointPaint = new Paint();//#ffffff
        pointPaint.setColor(Color.rgb(0x21, 0xa1, 0xea));
        pointPaint.setStyle(Style.FILL); // 画笔样式
        pointPaint.setAntiAlias(true);

        circlePaint = new Paint();//未选中时#5C787D
        circlePaint.setColor(Color.rgb(0xff, 0xff, 0xff));
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Style.STROKE); // 画笔样式空心

        centerCxCy = new PointF[3][3];
        data = new int[3][3];
        selected = new boolean[3][3];
        selPointList = new ArrayList<PointF>();
        initData();
    }

    private void clearSelected() {
        for (int i = 0; i < selected.length; i++) {
            for (int j = 0; j < selected.length; j++) {
                selected[i][j] = false;
            }
        }
    }

    private void initData() {
        /**
         * 1 2 3 4 5 6 7 8 9
         */
        int num = 1;
        for (int i = 0; i < data[0].length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                data[j][i] = num;
                num++;
            }
        }
    }

    private boolean isInCircle(PointF p, int x, int y) {
        int distance = (int) Math.sqrt((p.x - x) * (p.x - x) + (p.y - y) * (p.y - y));
        if (distance <= radius) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isLineInCircle(PointF targetPointF, PointF lastSelectedPointF, int currentX, int currentY) {
        double a = Math.pow((targetPointF.x - lastSelectedPointF.x), 2) + Math.pow((targetPointF.y - lastSelectedPointF.y), 2);
        double b = Math.pow((targetPointF.x - currentX), 2) + Math.pow((targetPointF.y - currentY), 2);
        double c = Math.pow((currentX - lastSelectedPointF.x), 2) + Math.pow((currentY - lastSelectedPointF.y), 2);
        double cosB = (a + c - b) / 2 / Math.sqrt(a) / Math.sqrt(c);
        if (c > a && Math.acos(cosB) <= (Math.PI / 2)) {
            double cosA = (b + c - a) / 2 / Math.sqrt(b) / Math.sqrt(c);
            double sinA = Math.sqrt(1 - Math.pow(cosA, 2));
            //
            double distance = sinA * Math.sqrt(b);
            if (distance <= radius) {//
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //
        //Bitmap bm_select = BitmapFactory.decodeResource(getResources(), R.drawable.gesture_selected);//选中的圆的图片
        //bm_select = Bitmap.createScaledBitmap(bm_select, radius * 2, radius * 2, true);
        //Bitmap bm_unselect = BitmapFactory.decodeResource(getResources(), R.drawable.gesture_unselected);//未选中的圆的图片
        //bm_unselect = Bitmap.createScaledBitmap(bm_unselect, radius * 2, radius * 2, true);
        // 画线条路径
        if (isPressedDown) {
            linePaint.setStrokeWidth(radius / 5); // 画笔宽度（连线宽度）
            for (int i = 0; i < selPointList.size() - 1; i++) { // 画已选中圆之间的路径
                PointF preCenter = selPointList.get(i); // 前一个圆中点
                PointF curCenter = selPointList.get(i + 1); // 现在圆中点
                canvas.drawLine(preCenter.x, preCenter.y, curCenter.x, curCenter.y, linePaint);
            }

            if (selPointList.size() > 0 && endX != -1 && endY != -1) {
                PointF center = selPointList.get(selPointList.size() - 1); // 最后一个选中圆中点
                canvas.drawLine(center.x, center.y, endX, endY, linePaint);
            }

        }
        //画手势圆圈
        circlePaint.setStrokeWidth(radius / 20);
        for (int i = 0; i < selected[0].length; i++) {
            for (int j = 0; j < selected[0].length; j++) {
                PointF center = centerCxCy[i][j];
                if (selected[i][j]) {
                    // 是否选中
                    //画选中的圆圈
                    //选中时#008CB4
                    circlePaint.setColor(Color.rgb(0xff, 0xff, 0xff));
                    canvas.drawCircle(center.x, center.y, radius, circlePaint);
                    //#006683
                    circlePaint.setColor(Color.rgb(0x21, 0xa1, 0xea));
                    canvas.drawCircle(center.x, center.y, 0.9f * radius, circlePaint);
                    //可画图片
                    //canvas.drawBitmap(bm_select, center.x - radius, center.y - radius, circlePaint);
                    //选中点圆心画圆（白色圆）
                    canvas.drawCircle(center.x, center.y, 2 * radius / 6, pointPaint);
                } else {
                    //画未选中的圆圈
                    //选中时#5C787D
                    circlePaint.setColor(Color.rgb(0xff, 0xff, 0xff));
                    canvas.drawCircle(center.x, center.y, radius, circlePaint);
                    //可画图片
                    //canvas.drawBitmap(bm_unselect, center.x - radius, center.y - radius, circlePaint);
                }
            }
        }

        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed && isFirst) {
            viewWidth = getWidth() - radius;
            viewHeight = getHeight();
            setRadius();
            isFirst = false;
        }

        super.onLayout(changed, left, top, right, bottom);
    }

    private void setRadius() {//设置圆圈半径
        radius = (Math.min(viewWidth, viewHeight) - 20) / 20;//半径(2个手势点间距为2个半径)
        float viewX = this.getScrollX() + radius;
        float viewY = this.getScrollY() + 30;
        if (viewWidth > viewHeight) {
            for (int i = 0; i < centerCxCy[0].length; i++) {
                for (int j = 0; j < centerCxCy[0].length; j++) {
                    PointF p = new PointF();
//					p.x = viewX + 10 + (viewWidth - viewHeight) / 2 + radius * j * 4 + radius;
//					p.y = viewY + 10 + radius * i * 4 + radius;
                    p.x = viewX + 10 + (viewWidth - viewHeight) / 2 + radius * j * 3 * 2 + radius;
                    p.y = viewY + 10 + radius * i * 3 * 2 + radius;
                    centerCxCy[i][j] = p;
                }
            }
        } else {
            for (int i = 0; i < centerCxCy[0].length; i++) {
                for (int j = 0; j < centerCxCy[0].length; j++) {
                    PointF p = new PointF();
                    if (j == 0) {

                    }
                    p.y = viewY + 10 + (viewHeight - viewWidth) / 2 + radius * i * 3 * 2 + radius;
                    p.x = viewX + 10 + radius * j * 3 * 2 + radius * 3;
                    centerCxCy[i][j] = p;
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isLock) {
            int pin = 0;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (callBack != null) {
                        callBack.onStartGesture();
                    }
                    lockPin = "";
                    selPointList.clear();
                    if (callBack != null) {
                        callBack.onDrawGesture(selected);
                    }
                    isPressedDown = true;
                    startX = (int) event.getX();
                    startY = (int) event.getY();
                    pin = getLockPinData(startX, startY);
                    if (pin > 0) {
                        lockPin += pin;
                        invalidate();
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    endX = (int) event.getX();
                    endY = (int) event.getY();
                    pin = getLockPinData(endX, endY);
                    if (pin > 0) {
                        lockPin += pin;
                    }
                    if (callBack != null) {
                        callBack.onDrawGesture(selected);
                    }
                    invalidate();
                    break;

                case MotionEvent.ACTION_UP:
                    //endX = (int) event.getX();
                    //endY = (int) event.getY();
                    endX = -1;
                    endY = -1;
                    isPressedDown = false;
                    if (callBack != null) {
                        callBack.onDrawGesture(selected);
                    }
                    if (callBack != null) {
                        callBack.onGesturecomplete(lockPin);
                    }
                    clearSelected();
                    invalidate();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    break;
            }

        }
        return true;
    }

    private int getLockPinData(int x, int y) {
        for (int i = 0; i < data[0].length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                PointF center = centerCxCy[i][j];
                if (isInCircle(center, x, y)) {
                    if (!selected[i][j]) {
                        selected[i][j] = true;
                        selPointList.add(center);
                        return data[i][j];
                    }
                } else {
                    if (selPointList.size() > 0 && isLineInCircle(center, selPointList.get(selPointList.size() - 1), x, y)) {//当前位置与上个选中点的连线是经过该圆的位置
                        if (!selected[i][j]) {
                            selected[i][j] = true;
                            selPointList.add(center);
                            return data[i][j];
                        }
                    }
                }
            }
        }
        return 0;
    }

    public GestureLockCallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(GestureLockCallBack callBack) {
        this.callBack = callBack;
    }

    public interface GestureLockCallBack {
        /**
         * 绘制密码结束
         *
         * @param pwd
         */
        void onGesturecomplete(String pwd);

        /**
         * 绘制中
         *
         * @param selected 选中的点
         */
        void onDrawGesture(boolean[][] selected);

        /**
         * 开始绘制密码
         */
        void onStartGesture();
    }

}
