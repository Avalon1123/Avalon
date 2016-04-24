package com.librariy.utils;

import android.graphics.Paint.Align;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

public class TextSpanBuilder extends SpannableStringBuilder {

    public TextSpanBuilder() {
        super();
    }

    public TextSpanBuilder append(String text, int mTextStyle, float mRelativeTextSize, int mTextColor, int mBgColor) {
        SpannableString styleString = new SpannableString(text);
        if (mTextStyle != 0)
            styleString.setSpan(new StyleSpan(mTextStyle), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (mRelativeTextSize  != 0)
            styleString.setSpan(new RelativeSizeSpan(mRelativeTextSize), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (mTextColor  != 0)
            styleString.setSpan(new ForegroundColorSpan(mTextColor), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (mBgColor  != 0)
            styleString.setSpan(new ForegroundColorSpan(mBgColor), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        super.append(styleString);
        return this;
    }

    @Override
    public TextSpanBuilder append(CharSequence text) {
        super.append(text);
        return this;
    }

    @Override
    public TextSpanBuilder append(CharSequence text, int start, int end) {
        super.append(text);
        return this;
    }

    @Override
    public TextSpanBuilder append(char text) {
        super.append(text);
        return this;
    }

}