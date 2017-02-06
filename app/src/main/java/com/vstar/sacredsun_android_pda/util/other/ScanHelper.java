package com.vstar.sacredsun_android_pda.util.other;

/**
 * Created by tanghuailong on 2017/2/6.
 */

import android.content.Context;
import android.text.TextUtils;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 用于扫描处理的工具类
 */
public class ScanHelper {

    //固化设备编号的规则，G开头,后面跟着1或者2，最后紧接两位编号(字母数字汉字都可)
    private static final String deviceNumberRegex = "^G[12]\\w{2}$";
    //订单编号的长度
    private static final int orderNumberLength = 16;
    //订单号长度
    private static final int orderEnd = 9;
    //流水号长度
    private static final int serialNumberBegin = 16;
    //物料编码长度
    private static final int materialNumberBegin = 10;
    //物料编号的前缀
    private static final String materialPrefix = "100";

    /**
     * 是否是有效的工号
     * @param code
     * @return
     */
    public static boolean isValidJobNumber(String code){
        if(code.substring(0,1).equals("H")) {
            return true;
        }
        return false;
    }

    /**
     * 是否是有效的订单编号
     * @param code
     * @return
     */
    public static boolean isValidOrderNumber(String code) {
        if(code.length() == orderNumberLength) {
            return true;
        }
        return false;
    }

    /**
     * 是否是有效的固化设备编号
     * @param code
     * @return
     */
    public static boolean isValidDeviceNumber(String code){
        return code.matches(deviceNumberRegex);
    }

    /**
     * 判断一个编号的类型
     * @param code
     * @return
     */
    public static CodeType judgeCodeNumber(String code) {
        CodeType result = CodeType.INVALID;
        if(!TextUtils.isEmpty(code)) {
            switch (code.substring(0, 1)) {
                case "H":
                    result = CodeType.JOB;
                    break;
                case "G":
                    if(isValidDeviceNumber(code)){
                        result = CodeType.DEVICE;
                    }
                    break;
                default:
                    if(isValidOrderNumber(code)){
                        result = CodeType.ORDER;
                    }
                    break;
            }
        }
        return result;
    }

    /**
     * 获取订单号
     * @param code
     * @return
     */
    public static String getOrder(String code) {
        return code.substring(0,orderEnd);
    }

    /**
     * 获取物料编码
     * @param code
     * @return
     */
    public static String getMaterailNumber(String code) {
        return materialPrefix+code.substring(materialNumberBegin,materialNumberBegin+6);
    }

    /**
     * 获取流水号
     * @param code
     * @return
     */
    public static String getSerialNumber(String code) {
        return code.substring(serialNumberBegin,serialNumberBegin+3);
    }
    /**
     * 扫码时，将输入切换为手动模式
     * @param context
     * @param editText
     */
    public static void changeToManualInput(Context context, EditText editText){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethod.SHOW_EXPLICIT);
    }

    /**
     * 获取edittext中的字符串
     * @param editText
     * @return
     */
    public static String getScanText(EditText editText) {
        return editText.getText().toString().trim();
    }
}
