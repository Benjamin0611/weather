package com.example.coolweather.util;

import android.text.TextUtils;

import com.example.coolweather.ChooseAreaFragment;
import com.example.coolweather.db.City;
import com.example.coolweather.db.County;
import com.example.coolweather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Administrator on 2017/4/23 0023.
 */

public class Utility {
    /*解析服务器返回省级数据*/

    public static boolean handleCityResponse(String response, int provinceId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCities = new JSONArray(response);
                for (int i=0; i < allCities.length();i++){
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    //city.setCityCode(cityObject.getInt("id"));
                   // city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleCountyResponse(String response, int cityId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCounties = new JSONArray(response);
                for (int i=0; i < allCounties.length();i++){
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                   // county.setCityId(cityId);
                    county.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleAllResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allLocateData = new JSONArray(response);
                for (int i=0; i < allLocateData.length();i++){
                    JSONObject allLocateObject = allLocateData.getJSONObject(i);
                    Province province = new Province();
                    City city = new City();
                    County county = new County();

                    String provinceName = allLocateObject.getString("provinceZh");
                    String cityName = allLocateObject.getString("leaderZh");
                    String countyName = allLocateObject.getString("cityZh");

                    if(checkIfProvinceRepeat(provinceName) == false) {
                        province.setProvinceName(provinceName);
                        province.setProvinceEn(allLocateObject.getString("provinceEn"));
                        province.save();
                    }

                    if(checkIfCityRepeat(provinceName, cityName) == false) {
                        city.setProvinceName(provinceName);
                        city.setProvinceEn(allLocateObject.getString("provinceEn"));
                        city.setCityName(cityName);
                        city.setCityEn(allLocateObject.getString("leaderEn"));
                        city.save();
                    }

                    if(checkIfCountyRepeat(provinceName, cityName, countyName) == false) {
                        county.setProvinceName(provinceName);
                        county.setProvinceEn(allLocateObject.getString("provinceEn"));
                        county.setCityName(cityName);
                        county.setCityEn(allLocateObject.getString("leaderEn"));
                        county.setCountyName(countyName);
                        county.setCountyEn(allLocateObject.getString("cityEn"));
                        county.setWeatherId(allLocateObject.getString("id"));
                        county.save();
                    }

                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean checkIfProvinceRepeat(String nameProvinceToCheck){

        List<Province> provinces = DataSupport.where("provinceName = ?", nameProvinceToCheck).find(Province.class);
        if(provinces.size() > 0) {
            return true;
        } else{
            return false;
        }
    }


    public static boolean checkIfCityRepeat(String nameProvinceToCheck, String nameCityToCheck){

        List<City> cities = DataSupport.where("provinceName = ? and cityName = ?", nameProvinceToCheck, nameCityToCheck).find(City.class);
        if(cities.size() > 0) {
            return true;
        } else{
            return false;
        }
    }


    public static boolean checkIfCountyRepeat(String nameProvinceToCheck, String nameCityToCheck, String nameCountyToCheck){

        List<County> counties = DataSupport.where("provinceName = ? and cityName = ? and countyName = ?", nameProvinceToCheck, nameCityToCheck, nameCountyToCheck).find(County.class);
        if(counties.size() > 0) {
            return true;
        } else{
            return false;
        }
    }
}
