package com.example.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


/**
 * @author Luki
 */
public class UIUtils {

    private static final String DEFAULT_FORMAT_TEXT = "<FONT COLOR='#EB413E'>%s</FONT>s 后重新获取";

    private static final int VALIDATE_SEC = 60;
    private static Toast sCustomToast;
    private static Toast sShortToast;
    private static Toast sLongToast;
    private static InputMethodManager imm;

    private static Timer timer = new Timer(true);
    private static Handler handler = new Handler();

    private static DecimalFormat df2 = new DecimalFormat("#,###.#####");

    /**
     * 获取带星号的手机号
     *
     * @return
     */
    public static String getProguardPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return "";
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    /**
     * 手机号码格式验证
     *
     * @param phone
     * @return
     */
    public static boolean checkPhoneNumber(String phone) {
        String regExp = "^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(18[0-9])|(17[0-9])|(19[0-9]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    /**
     * 文本内容颜色处理
     *
     * @param textView
     * @param input
     */
    public static void setSpanColor(TextView textView, String input, String color) {
        if (TextUtils.isEmpty(input)) {
            return;
        }
        Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?%?|[,]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        SpannableString spannableStringInterset = new SpannableString(input);
        int lastIndex = 0;
        while (matcher.find()) {
            String group = matcher.group();
            int start = input.indexOf(group, lastIndex);
            int end = start + group.length();
            lastIndex = end;
            spannableStringInterset.setSpan(new ForegroundColorSpan(Color.parseColor(color)), start, end, Spanned
                    .SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setText(spannableStringInterset);
    }


    /**
     * 文本中的数字和百分比变为红色spannableString输出
     *
     * @param input 输入字符串
     * @return spannableString
     */
    public static SpannableString getColoredPercentAndNumber(String input) {
        if (TextUtils.isEmpty(input)) {
            return null;
        }
        Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?%?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        SpannableString spannableStringInterest = new SpannableString(input);
        int lastIndex = 0;
        while (matcher.find()) {
            String group = matcher.group();
            int start = input.indexOf(group, lastIndex);
            int end = start + group.length();
            lastIndex = end;
            spannableStringInterest.setSpan(new ForegroundColorSpan(Color.parseColor("#E53935")), start, end, Spanned
                    .SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableStringInterest;
    }

    /**
     * 用args中String替换format中的%s部分,并设置为红色span,返回SpannableString
     *
     * @param format 带%s作为替换标志的字符串
     * @param args   替换用字符串数组,返回时设为红色#E53935
     */
    public static SpannableString getColoredSpannableString(String format, String... args) {
        int color = Color.parseColor("#E53935");
        return getSpannableString(format, color, args);
    }

    /**
     * 用args中String替换format中的%s部分,并设置为红色span,返回SpannableString
     *
     * @param format 带%s作为替换标志的字符串
     * @param args   替换用字符串数组,返回时设为color
     */
    private static SpannableString getSpannableString(String format, int color, String[] args) {
        try {
            Pattern pattern = Pattern.compile("%s", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(format);

            int lastIndex = 0;
            int argsCount = 0;
            ArrayList<Pair<Integer, Integer>> indices = new ArrayList<>();
            while (matcher.find()) {
                String group = matcher.group();
                int start = format.indexOf(group, lastIndex);
                int end = start + args[argsCount].length();
                lastIndex = end;
                format = format.substring(0, start) + format.substring(start).replaceFirst("%s", args[argsCount]);
                indices.add(new Pair<>(start, end));
                argsCount++;
            }
            SpannableString spannableString = new SpannableString(format);
            for (int i = 0; i < indices.size(); i++) {
                spannableString.setSpan(new ForegroundColorSpan(
                                color),
                        indices.get(i).first,
                        indices.get(i).second,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }
            return spannableString;
        } catch (PatternSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 文本中的百分号sp相对大小0.6输出
     *
     * @param input 输入字符串
     * @return spannableString
     */
    public static SpannableString getScaledPercentSyntax(String input, float scale) {
        if (TextUtils.isEmpty(input)) {
            return null;
        }
        Pattern pattern = Pattern.compile("%", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        SpannableString spannableStringInterest = new SpannableString(input);
        int lastIndex = 0;
        while (matcher.find()) {
            String group = matcher.group();
            int start = input.indexOf(group, lastIndex);
            int end = start + group.length();
            lastIndex = end;
            spannableStringInterest.setSpan(new RelativeSizeSpan(scale), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableStringInterest;
    }

    /**
     * 从字符串获取百分比字符
     *
     * @param source
     * @return
     */
    public static String getPercentString(String source) {
        Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?%", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(source);
        if (matcher.find()) {
            return matcher.group();
        }
        return " %";
    }

    /**
     * 获取带星号的名字
     *
     * @return
     */
    public static String getProguardName(String name) {
        if (name == null || name.length() == 0) {
            return "";
        }
        switch (name.length()) {
            case 1:
                return "*";
            case 2:
                return name.substring(0, 1) + "*";
            default:
                String star = "";
                for (int i = 0; i < name.length() - 2; i++) {
                    star += "*";
                }
                return name.substring(0, 1) + star + name.substring(name.length() - 1, name.length());
        }
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获取带星号的数字
     *
     * @return
     */
    public static String getProguardCard(String card) {
        if (card == null || card.length() <= 7) {
            return "";
        }
        String star = "";
        for (int i = 0; i < card.length() - 3 - 4; i++) {
            star += "*";
        }
        return card.substring(0, 3) + star + card.substring(card.length() - 4, card.length());
    }

    public static int dp2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int px2dp(Context context, int px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }


    public static int screenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int screenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 是否在前台运行
     * registrationID=06018c8615f
     *
     * @param context context
     * @return boolean
     */
    public static boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName())) {
            return true;
        }
        return false;
    }

    /**
     * 隐藏键盘
     *
     * @param view
     */
    public static void hideInputMethodManager(View view) {
        if (imm == null) {
            imm = (InputMethodManager) view.getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    // 显示键盘
    public static void showInputMethod(View view) {
        if (imm == null) {
            imm = (InputMethodManager) view.getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static String formatNumber(double number) {
        return df2.format(number);
    }

    public static String formatNumber(double number, int count) {
        String temp = "";
        for (int i = 0; i < count; i++) {
            temp += "#";
        }
        return new DecimalFormat("#,###." + temp).format(number);
    }

    public static String formatDouble2(double number) {
        return new DecimalFormat("#,##0.00").format(number);
    }

    public static String formatDouble3(double number) {
        return new DecimalFormat("#,###").format(number);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static String formatDouble(double number) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(number);
    }

    public static String formatMoney(int number) {
        if (number >= 10000) {
            return String.valueOf(number / 10000) + "万";
        } else {
            return String.valueOf(number);
        }
    }

    public static String formatDouble(double number, int count) {
        if (count <= 0) {
            return String.valueOf(number);
        }

        StringBuffer sbFormat = new StringBuffer("0.");
        for (int i = 0; i < count; i++) {
            sbFormat.append("0");
        }
        DecimalFormat df = new DecimalFormat(sbFormat.toString());
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(number);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static String formatDoubleZero(double number) {
        DecimalFormat df = new DecimalFormat("0");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(number);
    }


    private static String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
    }


    /*
     * strColor: 表示颜色,如"ffbb88",
     * strText: 需要颜色的文字
     */
    public static String getColorString(String strColor, String strText) {
        strColor = strColor.trim();
        if (!strColor.startsWith("#")) {
            strColor = "#" + strColor;
        }
        return String.format("<FONT COLOR='%s'>%s</FONT>", strColor, strText);
    }


    public static int getScreenWidthPixels(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                .getMetrics(dm);
        return dm.widthPixels;
    }

    public static int dipToPx(Context context, int dip) {
        return (int) (dip * getScreenDensity(context) + 0.5f);
    }

    public static float getScreenDensity(Context context) {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                    .getMetrics(dm);
            return dm.density;
        } catch (Exception e) {
            return DisplayMetrics.DENSITY_DEFAULT;
        }
    }

    //限制EditView首次输入0
    public static void limitEnterZero(Editable s) {
        String text = s.toString();
        int len = s.toString().length();
        if (len == 1 && text.equals("0")) {
            s.clear();
        }
    }

    //周周升金额补零
    public static String amountAddZero(String s) {
        String amtFormat;
        switch (s.length()) {
            case 1:
                amtFormat = "00000" + s;
                break;
            case 2:
                amtFormat = "0000" + s;
                break;
            case 3:
                amtFormat = "000" + s;
                break;
            case 4:
                amtFormat = "00" + s;
                break;
            case 5:
                amtFormat = "0" + s;
                break;
            default:
                amtFormat = s;

        }
        return amtFormat;
    }

    public static <T> boolean notEmpty(List<T> list) {
        return !isEmpty(list);
    }

    public static <T> boolean isEmpty(List<T> list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }

    // 将px值转换为dip或dp值
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    // 将dip或dp值转换为px值
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    // 将px值转换为sp值
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    // 将sp值转换为px值
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    // 屏幕宽度（像素）
    public static int getWindowWidth(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    // 屏幕高度（像素）
    public static int getWindowHeight(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }

    // 根据Unicode编码判断中文汉字和符号
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    // 判断中文汉字和符号
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 版本号比较
     *
     * @param version1
     * @param version2
     * @return
     */
    public static int compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0;
        }
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        int index = 0;
        // 获取最小长度值
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        // 循环判断每位的大小
        while (index < minLen
                && (diff = Integer.parseInt(version1Array[index])
                - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            // 如果位数不一致，比较多余位数
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }

    //获取当前时间
    public static String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }
}
