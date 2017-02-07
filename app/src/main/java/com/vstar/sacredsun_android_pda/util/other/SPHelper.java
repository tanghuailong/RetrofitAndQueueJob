package com.vstar.sacredsun_android_pda.util.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Map;

/**
 * Created by tanghuailong on 2017/2/7.
 */

/**
 * 处理 SharedPreferences 的工具类
 */
public class SPHelper {

    public static void putAndApply(Context context,String key,Object object) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        if(object instanceof String) {
            editor.putString(key,(String)object);
        }else if(object instanceof Integer) {
            editor.putInt(key,(Integer)object);
        }else if(object instanceof Boolean) {
            editor.putBoolean(key,(Boolean)object);
        }else if(object instanceof Float) {
            editor.putFloat(key,(Float)object);
        }else if(object instanceof Long) {
            editor.putLong(key,(Long)object);
        }else {
            editor.putString(key,object.toString());
        }
        editor.apply();
    }

    public static Object get(Context context,String key,Object defaultObject) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        if(defaultObject instanceof String) {
            return sp.getString(key,(String)defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key,(Integer)defaultObject);
        } else if(defaultObject instanceof Boolean) {
            return sp.getBoolean(key,(Boolean)defaultObject);
        }else if(defaultObject instanceof Float){
            return sp.getFloat(key,(Float)defaultObject);
        }else if(defaultObject instanceof Long) {
            return sp.getLong(key,(Long)defaultObject);
        }else {
            return null;
        }
    }

    public static void remove(Context context,String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }

    public static void removeAll(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    public static boolean contains(Context context,String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.contains(key);
    }

    public static Map<String,?> getAll(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getAll();
    }
}
