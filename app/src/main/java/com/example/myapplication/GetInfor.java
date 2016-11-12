/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.myapplication;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author 孙宇宁
 */
public class GetInfor {

    public static String getName(JSONObject result) {
        String re = null;
        try {
            re = result.getString("Name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return re;
    }

    public static String getAcademy(JSONObject result) {
        String re = null;
        try {
            re = result.getString("Academy");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return re;
    }

    public static String getMajor(JSONObject result) {
        String re = null;
        try {
            re = result.getString("Major");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return re;
    }

    public static String getId(JSONObject result) {
        String re = null;
        try {
            re = result.getString("Id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return re;
    }

    public static String getGrade(JSONObject result) {
        String re = null;
        try {
            re = result.getString("Grade");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return re;
    }
}
