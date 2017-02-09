package com.vstar.sacredsun_android_pda.util.other;

/**
 * Created by tanghuailong on 2017/2/6.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.vstar.sacredsun_android_pda.R;

/**
 * 一些功能的工具类
 */
public class FunctionUtil {

    //固化设备编号的规则，G开头,后面跟着1或者2，最后紧接两位编号(字母数字汉字都可)
    private static final String deviceNumberRegex = "^G[12]\\w{2}$";
    //订单编号的长度
    private static final int orderNumberLength = 19;
    //订单号结束位置
    private static final int orderEnd = 9;
    //流水号开始位置
    private static final int serialNumberBegin = 16;
    //物料编码开始位置
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
     * 检测只剩最后一个待扫描
     * @return
     */
    public static boolean isLastOneToScan(Context context,String... keys){
        boolean isLastOne = false;
        int notScanCount = keys.length;
        for(String key:keys){
            if(SPHelper.contains(context,key)) {
                notScanCount--;
            }
        }
        if(notScanCount == 1) {
            isLastOne = true;
        }
        return isLastOne;
    }
    /**
     * 获取edittext中的字符串
     * @param editText
     * @return
     */
    public static String getScanText(EditText editText) {
        return editText.getText().toString().trim();
    }

    /**
     * 显示Dialog
     * @param title dialog的标题
     * @param icon dialog的图标
     * @param msg dialog的内容
     * @param positiveListener 点击确认后操作
     */
    public static void showDialog(Context context,String title,int icon,String msg,DialogInterface.OnClickListener positiveListener) {
        AlertDialog.Builder builder  = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setIcon(icon)
                .setMessage(msg)
                .setNegativeButton("取消",null)
                .setPositiveButton("确定",positiveListener)
                .show();
    }

    /**
     * 修改页面上组件的状态
     * @param context
     * @param imageView
     * @param textView
     * @param status
     */
    public static void changeStateCompoment(Context context,ImageView imageView, TextView textView,StatusCompoment status) {
        switch (status) {
            case ORDER_COMPLEMENT:
                imageView.setImageResource(R.drawable.complete);
                textView.setText(context.getString(R.string.order_scan_complete));
                textView.setTextColor(ContextCompat.getColor(context,R.color.state_complete));
                break;
            case ORDER_INCOMPLEMENT:
                imageView.setImageResource(R.drawable.incomplete);
                textView.setText(context.getString(R.string.order_scan_incomplete));
                textView.setTextColor(ContextCompat.getColor(context,R.color.state_incomplete));
                break;
            case DEVICE_COMPLEMENT:
                imageView.setImageResource(R.drawable.complete);
                textView.setText(context.getString(R.string.device_scan_complete));
                textView.setTextColor(ContextCompat.getColor(context,R.color.state_complete));
                break;
            case DEVICE_INCOMPLEMENT:
                imageView.setImageResource(R.drawable.incomplete);
                textView.setText(context.getString(R.string.device_scan_incomplete));
                textView.setTextColor(ContextCompat.getColor(context,R.color.state_incomplete));
                break;
        }
    }

    /**
     * 获取启动模式
     * @param context
     * @return
     */
    public static StartMode getStartMode(Context context) {

        if((Boolean)SPHelper.get(context,context.getString(R.string.FIRST_USE),false).equals(false)) {
            return StartMode.NO_SETTING;
        }else if(SPHelper.get(context,context.getString(R.string.IS_LOGIN),false).equals(false)){
            return StartMode.NO_LOGIN;
        } else  {
            return StartMode.NORMAL;
        }
    }
}
