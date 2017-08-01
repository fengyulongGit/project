package com.android.common.ui.picker.picker;

import android.app.Activity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.common.ui.picker.util.DateUtils;
import com.android.common.ui.picker.widget.WheelView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 日期选择器
 *
 * @author 李玉江[QQ :1032694760]
 * @version 2015 /12/14
 */
public class DatePicker extends WheelPicker {
    private ArrayList<String> years = new ArrayList<String>();
    private ArrayList<String> months = new ArrayList<String>();
    private ArrayList<String> days = new ArrayList<String>();
    private OnDatePickListener onDatePickListener;
    private OnPreDatePickListener onPreDatePickListener;
    private String yearLabel = "年", monthLabel = "月", dayLabel = "日";
    private int selectedYearIndex = 0, selectedMonthIndex = 0, selectedDayIndex = 0;
    private Mode mode = Mode.YEAR_MONTH_DAY;

    private int endYear = -1, endMonth = -1, endDay = -1;

    /**
     * Instantiates a new Date picker.
     *
     * @param activity the activity
     */
    public DatePicker(Activity activity) {
        this(activity, Mode.YEAR_MONTH_DAY);
    }

    /**
     * Instantiates a new Date picker.
     *
     * @param activity the activity
     * @param mode     the mode
     */
    public DatePicker(Activity activity, Mode mode) {
        super(activity);
        this.mode = mode;
        for (int i = 2000; i <= 2050; i++) {
            years.add(String.valueOf(i));
        }
        for (int i = 1; i <= 12; i++) {
            months.add(DateUtils.fillZero(i));
        }
        for (int i = 1; i <= 31; i++) {
            days.add(DateUtils.fillZero(i));
        }
    }

    /**
     * Sets label.
     *
     * @param yearLabel  the year label
     * @param monthLabel the month label
     * @param dayLabel   the day label
     */
    public void setLabel(String yearLabel, String monthLabel, String dayLabel) {
        this.yearLabel = yearLabel;
        this.monthLabel = monthLabel;
        this.dayLabel = dayLabel;
    }

    /**
     * Sets range.
     *
     * @param startYear the start year
     * @param endYear   the end year
     * @see Mode#YEAR_MONTH_DAY Mode#YEAR_MONTH_DAY
     * @see Mode#YEAR_MONTH Mode#YEAR_MONTH
     */
    public void setRange(int startYear, int endYear) {
        this.endYear = endYear;
        years.clear();
        for (int i = startYear; i <= endYear; i++) {
            years.add(String.valueOf(i));
        }
    }

    public void setEndMonth(int endMonth) {
        this.endMonth = endMonth;
    }

    public void setEndDay(int endDay) {
        this.endDay = endDay;
    }

    private int findItemIndex(ArrayList<String> items, int item) {
        //折半查找有序元素的索引
        int index = Collections.binarySearch(items, item, new Comparator<Object>() {
            @Override
            public int compare(Object lhs, Object rhs) {
                String lhsStr = lhs.toString();
                String rhsStr = rhs.toString();
                lhsStr = lhsStr.startsWith("0") ? lhsStr.substring(1) : lhsStr;
                rhsStr = rhsStr.startsWith("0") ? rhsStr.substring(1) : rhsStr;
                return Integer.parseInt(lhsStr) - Integer.parseInt(rhsStr);
            }
        });
        if (index < 0) {
            index = 0;
        }
        return index;
    }

    /**
     * Sets selected item.
     *
     * @param year  the year
     * @param month the month
     * @param day   the day
     */
    public void setSelectedItem(int year, int month, int day) {
        selectedYearIndex = findItemIndex(years, year);
        selectedMonthIndex = findItemIndex(months, month);
        selectedDayIndex = findItemIndex(days, day);
    }

    /**
     * Sets selected item.
     *
     * @param yearOrMonth the year or month
     * @param monthOrDay  the month or day
     */
    public void setSelectedItem(int yearOrMonth, int monthOrDay) {
        if (mode.equals(Mode.MONTH_DAY)) {
            selectedMonthIndex = findItemIndex(months, yearOrMonth);
            selectedDayIndex = findItemIndex(days, monthOrDay);
        } else {
            selectedYearIndex = findItemIndex(years, yearOrMonth);
            selectedMonthIndex = findItemIndex(months, monthOrDay);
        }
    }

    /**
     * Sets on date pick listener.
     *
     * @param listener the listener
     */
    public void setOnDatePickListener(OnDatePickListener listener) {
        this.onDatePickListener = listener;
    }

    /**
     * Sets on per date pick listener.
     *
     * @param listener the listener
     */
    public void setOnPreDatePickListener(OnPreDatePickListener listener) {
        this.onPreDatePickListener = listener;
    }

    @Override
    protected View initContentView() {
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        WheelView yearView = new WheelView(activity);
        yearView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        yearView.setTextSize(textSize);
        yearView.setTextColor(textColorNormal, textColorFocus);
        yearView.setLineVisible(lineVisible);
        yearView.setLineColor(lineColor);
        yearView.setOffset(offset);
        layout.addView(yearView);
        TextView yearTextView = new TextView(activity);
        yearTextView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        yearTextView.setTextSize(textSize);
        yearTextView.setTextColor(textColorFocus);
        if (!TextUtils.isEmpty(yearLabel)) {
            yearTextView.setText(yearLabel);
        }
        layout.addView(yearTextView);
        final WheelView monthView = new WheelView(activity);
        monthView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        monthView.setTextSize(textSize);
        monthView.setTextColor(textColorNormal, textColorFocus);
        monthView.setLineVisible(lineVisible);
        monthView.setLineColor(lineColor);
        monthView.setOffset(offset);
        layout.addView(monthView);
        TextView monthTextView = new TextView(activity);
        monthTextView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        monthTextView.setTextSize(textSize);
        monthTextView.setTextColor(textColorFocus);
        if (!TextUtils.isEmpty(monthLabel)) {
            monthTextView.setText(monthLabel);
        }
        layout.addView(monthTextView);
        final WheelView dayView = new WheelView(activity);
        dayView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        dayView.setTextSize(textSize);
        dayView.setTextColor(textColorNormal, textColorFocus);
        dayView.setLineVisible(lineVisible);
        dayView.setLineColor(lineColor);
        dayView.setOffset(offset);
        layout.addView(dayView);
        TextView dayTextView = new TextView(activity);
        dayTextView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        dayTextView.setTextSize(textSize);
        dayTextView.setTextColor(textColorFocus);
        if (!TextUtils.isEmpty(dayLabel)) {
            dayTextView.setText(dayLabel);
        }
        layout.addView(dayTextView);
        if (mode.equals(Mode.YEAR_MONTH)) {
            dayView.setVisibility(View.GONE);
            dayTextView.setVisibility(View.GONE);
        } else if (mode.equals(Mode.MONTH_DAY)) {
            yearView.setVisibility(View.GONE);
            yearTextView.setVisibility(View.GONE);
        }
        if (!mode.equals(Mode.MONTH_DAY)) {
            if (!TextUtils.isEmpty(yearLabel)) {
                yearTextView.setText(yearLabel);
            }
            if (selectedYearIndex == 0) {
                yearView.setItems(years);
            } else {
                yearView.setItems(years, selectedYearIndex);
            }
            yearView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                @Override
                public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                    selectedYearIndex = selectedIndex;

                    months.clear();
                    if (stringToYearMonthDay(years.get(selectedYearIndex)) == endYear && endMonth > 0) {
                        for (int i = 1; i <= endMonth; i++) {
                            months.add(DateUtils.fillZero(i));
                        }
                    } else {
                        for (int i = 1; i <= 12; i++) {
                            months.add(DateUtils.fillZero(i));
                        }
                    }
                    if (selectedMonthIndex == 0) {
                        monthView.setItems(months);
                    } else {
                        if (selectedMonthIndex >= months.size()) {
                            //年或月变动时，保持之前选择的日不动：如果之前选择的日是之前年月的最大日，则日自动为该年月的最大日
                            selectedMonthIndex = months.size() - 1;
                        }
                        monthView.setItems(months, selectedMonthIndex);
                    }

                    //需要根据年份及月份动态计算天数
                    days.clear();
                    if (Integer.parseInt(years.get(selectedYearIndex)) == endYear && stringToYearMonthDay(months.get(selectedMonthIndex)) == endMonth && endDay > 0) {
                        for (int i = 1; i <= endDay; i++) {
                            days.add(DateUtils.fillZero(i));
                        }
                    } else {
                        int maxDays = DateUtils.calculateDaysInMonth(stringToYearMonthDay(item), stringToYearMonthDay(months.get(selectedMonthIndex)));
                        for (int i = 1; i <= maxDays; i++) {
                            days.add(DateUtils.fillZero(i));
                        }
                    }
                    if (selectedDayIndex >= days.size()) {
                        //年或月变动时，保持之前选择的日不动：如果之前选择的日是之前年月的最大日，则日自动为该年月的最大日
                        selectedDayIndex = days.size() - 1;
                    }
                    dayView.setItems(days, selectedDayIndex);
                }
            });
        }
        if (!TextUtils.isEmpty(monthLabel)) {
            monthTextView.setText(monthLabel);
        }

        months.clear();
        if (stringToYearMonthDay(years.get(selectedYearIndex)) == endYear && endMonth > 0) {
            for (int i = 1; i <= endMonth; i++) {
                months.add(DateUtils.fillZero(i));
            }
        } else {
            for (int i = 1; i <= 12; i++) {
                months.add(DateUtils.fillZero(i));
            }
        }
        if (selectedMonthIndex == 0) {
            monthView.setItems(months);
        } else {
            if (selectedMonthIndex >= months.size()) {
                //年或月变动时，保持之前选择的日不动：如果之前选择的日是之前年月的最大日，则日自动为该年月的最大日
                selectedMonthIndex = months.size() - 1;
            }
            monthView.setItems(months, selectedMonthIndex);
        }
        monthView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectedMonthIndex = selectedIndex;
                if (!mode.equals(Mode.YEAR_MONTH)) {
                    //年月日或年月模式下，需要根据年份及月份动态计算天数
                    //需要根据年份及月份动态计算天数
                    days.clear();
                    if (Integer.parseInt(years.get(selectedYearIndex)) == endYear && stringToYearMonthDay(months.get(selectedMonthIndex)) == endMonth && endDay > 0) {
                        for (int i = 1; i <= endDay; i++) {
                            days.add(DateUtils.fillZero(i));
                        }
                    } else {
                        int maxDays = DateUtils.calculateDaysInMonth(stringToYearMonthDay(item), stringToYearMonthDay(months.get(selectedMonthIndex)));
                        for (int i = 1; i <= maxDays; i++) {
                            days.add(DateUtils.fillZero(i));
                        }
                    }
                    if (selectedDayIndex >= days.size()) {
                        //年或月变动时，保持之前选择的日不动：如果之前选择的日是之前年月的最大日，则日自动为该年月的最大日
                        selectedDayIndex = days.size() - 1;
                    }
                    dayView.setItems(days, selectedDayIndex);
                }
            }
        });
        if (!mode.equals(Mode.YEAR_MONTH)) {
            if (!TextUtils.isEmpty(dayLabel)) {
                dayTextView.setText(dayLabel);
            }
            if (selectedDayIndex == 0) {
                dayView.setItems(days);
            } else {
                dayView.setItems(days, selectedDayIndex);
            }
            dayView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                @Override
                public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                    selectedDayIndex = selectedIndex;
                }
            });
        }
        return layout;
    }

    private int stringToYearMonthDay(String text) {
        if (text.startsWith("0")) {
            //截取掉前缀0以便转换为整数
            text = text.substring(1);
        }
        return Integer.parseInt(text);
    }

    @Override
    protected void setContentViewAfter(View contentView) {
        super.setContentViewAfter(contentView);
        super.setOnPreConfirmListener(new OnPreConfirmListener() {
            @Override
            public boolean onPreConfirm() {
                if (onPreDatePickListener != null) {
                    String year = years.get(selectedYearIndex);
                    String month = months.get(selectedMonthIndex);
                    String day = days.get(selectedDayIndex);
                    switch (mode) {
                        case YEAR_MONTH:
                            return ((OnPreYearMonthPickListener) onPreDatePickListener).onPreDatePicked(year, month);
                        case MONTH_DAY:
                            return ((OnPreMonthDayPickListener) onPreDatePickListener).onPreDatePicked(month, day);
                        default:
                            return ((OnPreYearMonthDayPickListener) onPreDatePickListener).onPreDatePicked(year, month, day);
                    }
                }
                return true;
            }
        });
        super.setOnConfirmListener(new OnConfirmListener() {
            @Override
            public void onConfirm() {
                if (onDatePickListener != null) {
                    String year = years.get(selectedYearIndex);
                    String month = months.get(selectedMonthIndex);
                    String day = days.get(selectedDayIndex);
                    switch (mode) {
                        case YEAR_MONTH:
                            ((OnYearMonthPickListener) onDatePickListener).onDatePicked(year, month);
                            break;
                        case MONTH_DAY:
                            ((OnMonthDayPickListener) onDatePickListener).onDatePicked(month, day);
                            break;
                        default:
                            ((OnYearMonthDayPickListener) onDatePickListener).onDatePicked(year, month, day);
                            break;
                    }
                }
            }
        });
    }

    /**
     * The enum Mode.
     */
    public enum Mode {
        /**
         * 年月日
         */
        YEAR_MONTH_DAY,
        /**
         * 年月
         */
        YEAR_MONTH,
        /**
         * 月日
         */
        MONTH_DAY
    }

    /**
     * The interface On date pick listener.
     */
    protected interface OnDatePickListener {

    }

    /**
     * The interface On year month day pick listener.
     */
    public interface OnYearMonthDayPickListener extends OnDatePickListener {

        /**
         * On date picked.
         *
         * @param year  the year
         * @param month the month
         * @param day   the day
         */
        void onDatePicked(String year, String month, String day);

    }

    /**
     * The interface On year month pick listener.
     */
    public interface OnYearMonthPickListener extends OnDatePickListener {

        /**
         * On date picked.
         *
         * @param year  the year
         * @param month the month
         */
        void onDatePicked(String year, String month);

    }

    /**
     * The interface On month day pick listener.
     */
    public interface OnMonthDayPickListener extends OnDatePickListener {

        /**
         * On date picked.
         *
         * @param month the month
         * @param day   the day
         */
        void onDatePicked(String month, String day);

    }

    /**
     * The interface On Pre date pick listener.
     */
    protected interface OnPreDatePickListener {

    }

    /**
     * The interface On Pre year month day pick listener.
     */
    public interface OnPreYearMonthDayPickListener extends OnPreDatePickListener {

        /**
         * On Pre date picked.
         *
         * @param year  the year
         * @param month the month
         * @param day   the day
         */
        boolean onPreDatePicked(String year, String month, String day);

    }

    /**
     * The interface On Pre year month pick listener.
     */
    public interface OnPreYearMonthPickListener extends OnPreDatePickListener {

        /**
         * On Pre date picked.
         *
         * @param year  the year
         * @param month the month
         */
        boolean onPreDatePicked(String year, String month);

    }

    /**
     * The interface On Pre month day pick listener.
     */
    public interface OnPreMonthDayPickListener extends OnPreDatePickListener {

        /**
         * On Pre date picked.
         *
         * @param month the month
         * @param day   the day
         */
        boolean onPreDatePicked(String month, String day);

    }

}
