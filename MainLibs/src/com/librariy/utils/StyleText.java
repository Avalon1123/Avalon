package com.librariy.utils;
import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
/**
 * 在textView显示带样式的文字
 * @author luchyu
 *
 */
public class StyleText extends SpannableStringBuilder{
    public static StyleText newInstance() {
       return new StyleText();
    }
    public StyleText addText(String text) {
        if (!TextUtils.isEmpty(text)){
            super.append(text);
        }
        return this;
    }
    /**
     * 
     * @param style An integer constant describing the style for this span. Examples
     * include bold, italic, and normal. Values are constants defined 
     * in {@link android.graphics.Typeface}.
     */
    public StyleText addStyleText(String text, int mTextStyle,float mTextSize, int mTextColor) {
        if (TextUtils.isEmpty(text)){
            return this;
        }
        SpannableString styleString = new SpannableString(text);
        styleString.setSpan(new AbsoluteSizeSpan((int) mTextSize), 0,
        styleString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styleString.setSpan(new ForegroundColorSpan(mTextColor), 0,
        styleString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styleString.setSpan(new StyleSpan(mTextStyle), 0, styleString.length(),
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        super.append(styleString);
        return this;
    }
    /**
     * 
     * @param style An integer constant describing the style for this span. Examples
     * include bold, italic, and normal. Values are constants defined 
     * in {@link android.graphics.Typeface}.
     */
    public StyleText addImage(Context mContext,int resid) {
        if (resid<0){
            return this;
        }
        SpannableString styleString = new SpannableString("#");
        styleString.setSpan(new ImageSpan(mContext,resid,DynamicDrawableSpan.ALIGN_BASELINE),0,styleString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        super.append(styleString);
        return this;
    }
}
