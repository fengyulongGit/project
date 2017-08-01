package com.android.common.ui.picker.picker;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.android.common.ui.picker.util.LogUtils;
import com.android.common.ui.picker.widget.WheelView;

import java.util.ArrayList;

/**
 * 地址选择器（包括省级、地级、县级）。
 * 地址数据见示例项目的“city.json”，来源于国家统计局官网（http://www.stats.gov.cn/tjsj/tjbz/xzqhdm）
 *
 * @author 李玉江[QQ :1032694760]
 * @version 2015 /12/15
 */
public class AddressPicker extends WheelPicker {
    private ArrayList<String> provinceList = new ArrayList<String>();
    private ArrayList<ArrayList<String>> cityList = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<ArrayList<String>>> countyList = new ArrayList<ArrayList<ArrayList<String>>>();
    private OnAddressPickListener onAddressPickListener;
    private String selectedProvince = "", selectedCity = "", selectedCounty = "";
    private int selectedProvinceIndex = 0, selectedCityIndex = 0, selectedCountyIndex = 0;
    private boolean hideProvince = false;
    private boolean hideCounty = false;

    /**
     * Instantiates a new Address picker.
     *
     * @param activity the activity
     * @param data     the data
     */
    public AddressPicker(Activity activity, ArrayList<Province> data) {
        super(activity);
        int provinceSize = data.size();
        //添加省
        for (int x = 0; x < provinceSize; x++) {
            Province pro = data.get(x);
            provinceList.add(pro.getName());
            ArrayList<City> cities = pro.getCitys();
            ArrayList<String> xCities = new ArrayList<String>();
            ArrayList<ArrayList<String>> xCounties = new ArrayList<ArrayList<String>>();
            int citySize = cities.size();
            //添加地市
            for (int y = 0; y < citySize; y++) {
                City cit = cities.get(y);
                xCities.add(cit.getName());
                ArrayList<County> counties = cit.getCountys();
                ArrayList<String> yCounties = new ArrayList<String>();
                int countySize = counties.size();
                //添加区县
                if (countySize == 0) {
                    yCounties.add(cit.getName());
                } else {
                    for (int z = 0; z < countySize; z++) {
                        yCounties.add(counties.get(z).getName());
                    }
                }
                xCounties.add(yCounties);
            }
            cityList.add(xCities);
            countyList.add(xCounties);
        }
    }

    /**
     * Sets selected item.
     *
     * @param province the province
     * @param city     the city
     * @param county   the county
     */
    public void setSelectedItem(String province, String city, String county) {
        for (int i = 0; i < provinceList.size(); i++) {
            String pro = provinceList.get(i);
            if (pro.contains(province)) {
                selectedProvinceIndex = i;
                LogUtils.debug("init select province: " + pro);
                break;
            }
        }
        ArrayList<String> cities = cityList.get(selectedProvinceIndex);
        for (int j = 0; j < cities.size(); j++) {
            String cit = cities.get(j);
            if (cit.contains(city)) {
                selectedCityIndex = j;
                LogUtils.debug("init select city: " + cit);
                break;
            }
        }
        ArrayList<String> counties = countyList.get(selectedProvinceIndex).get(selectedCityIndex);
        for (int k = 0; k < counties.size(); k++) {
            String cou = counties.get(k);
            if (cou.contains(county)) {
                selectedCountyIndex = k;
                LogUtils.debug("init select county: " + cou);
                break;
            }
        }
        LogUtils.debug(String.format("init select index: %s-%s-%s", selectedProvinceIndex, selectedCityIndex, selectedCountyIndex));
    }

    /**
     * 隐藏省级行政区，只显示地市级和区县级。
     * 设置为true的话，地址数据中只需要某个省份的即可
     * 参见示例中的“city2.json”
     *
     * @param hideProvince the hide province
     */
    public void setHideProvince(boolean hideProvince) {
        this.hideProvince = hideProvince;
    }

    /**
     * 隐藏省级行政区，只显示省市和地市级。
     *
     * @param hideCounty 是否隐藏区县级
     */
    public void setHideCounty(boolean hideCounty) {
        this.hideCounty = hideCounty;
    }

    /**
     * Sets on address pick listener.
     *
     * @param listener the listener
     */
    public void setOnAddressPickListener(OnAddressPickListener listener) {
        this.onAddressPickListener = listener;
    }

    @Override
    protected View initContentView() {
        if (provinceList.size() == 0) {
            throw new IllegalArgumentException("please initial options at first, can't be empty");
        }
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        int screenWidth = screen.widthPixels;
        final WheelView provinceView = new WheelView(activity);
        provinceView.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 3, WRAP_CONTENT));
        provinceView.setTextSize(textSize);
        provinceView.setTextColor(textColorNormal, textColorFocus);
        provinceView.setLineVisible(lineVisible);
        provinceView.setLineColor(lineColor);
        provinceView.setOffset(offset);
        layout.addView(provinceView);
        if (hideProvince) {
            provinceView.setVisibility(View.GONE);
        }
        final WheelView cityView = new WheelView(activity);
        cityView.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 3, WRAP_CONTENT));
        cityView.setTextSize(textSize);
        cityView.setTextColor(textColorNormal, textColorFocus);
        cityView.setLineVisible(lineVisible);
        cityView.setLineColor(lineColor);
        cityView.setOffset(offset);
        layout.addView(cityView);
        final WheelView countyView = new WheelView(activity);
        countyView.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 3, WRAP_CONTENT));
        countyView.setTextSize(textSize);
        countyView.setTextColor(textColorNormal, textColorFocus);
        countyView.setLineVisible(lineVisible);
        countyView.setLineColor(lineColor);
        countyView.setOffset(offset);
        layout.addView(countyView);
        if (hideCounty) {
            countyView.setVisibility(View.GONE);
        }
        provinceView.setItems(provinceList, selectedProvinceIndex);
        provinceView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectedProvince = item;
                selectedProvinceIndex = selectedIndex;
                selectedCountyIndex = 0;
                //根据省份获取地市
                cityView.setItems(cityList.get(selectedProvinceIndex), isUserScroll ? 0 : selectedCityIndex);
                //根据地市获取区县
                countyView.setItems(countyList.get(selectedProvinceIndex).get(0), isUserScroll ? 0 : selectedCountyIndex);
            }
        });
        cityView.setItems(cityList.get(selectedProvinceIndex), selectedCityIndex);
        cityView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectedCity = item;
                selectedCityIndex = selectedIndex;
                //根据地市获取区县
                countyView.setItems(countyList.get(selectedProvinceIndex).get(selectedCityIndex), isUserScroll ? 0 : selectedCountyIndex);
            }
        });
        countyView.setItems(countyList.get(selectedProvinceIndex).get(selectedCityIndex), selectedCountyIndex);
        countyView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectedCounty = item;
                selectedCountyIndex = selectedIndex;
            }
        });
        return layout;
    }

    @Override
    protected void setContentViewAfter(View contentView) {
        super.setContentViewAfter(contentView);
        super.setOnConfirmListener(new OnConfirmListener() {
            @Override
            public void onConfirm() {
                if (onAddressPickListener != null) {
                    onAddressPickListener.onAddressPicked(selectedProvince, selectedCity, selectedCounty);
                }
            }
        });
    }

    /**
     * The interface On address pick listener.
     */
    public interface OnAddressPickListener {

        /**
         * On address picked.
         *
         * @param province the province
         * @param city     the city
         * @param county   the county
         */
        void onAddressPicked(String province, String city, String county);

    }

    /**
     * The type Area.
     */
    public abstract static class Area {
        /**
         * The Area id.
         */
        String id;
        /**
         * The Area name.
         */
        String name;

        String code;

        /**
         * Gets area id.
         *
         * @return the area id
         */
        public String getId() {
            return id;
        }

        /**
         * Sets area id.
         *
         * @param id the area id
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * Gets area name.
         *
         * @return the area name
         */
        public String getName() {
            return name;
        }

        /**
         * Sets area name.
         *
         * @param name the area name
         */
        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "areaId=" + id + ",areaName=" + name + ",areaCode=" + code;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    /**
     * The type Province.
     */
    public static class Province extends Area {
        /**
         * The Cities.
         */
        ArrayList<City> citys = new ArrayList<City>();

        /**
         * Gets cities.
         *
         * @return the cities
         */
        public ArrayList<City> getCitys() {
            return citys;
        }

        /**
         * Sets cities.
         *
         * @param citys the cities
         */
        public void setCitys(ArrayList<City> citys) {
            this.citys = citys;
        }

    }

    /**
     * The type City.
     */
    public static class City extends Area {
        private ArrayList<County> countys = new ArrayList<County>();

        /**
         * Gets counties.
         *
         * @return the counties
         */
        public ArrayList<County> getCountys() {
            return countys;
        }

        /**
         * Sets counties.
         *
         * @param countys the counties
         */
        public void setCountys(ArrayList<County> countys) {
            this.countys = countys;
        }

    }

    /**
     * The type County.
     */
    public static class County extends Area {
        public String phoneCode; //地区电话区号
        public String postcode; //地区邮政编码
    }

}
