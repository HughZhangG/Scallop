package com.gu.cheng.scallop.view;


import android.content.res.Resources;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.gu.cheng.scallop.listener.CharacterClickListener;



/**
 * Created by Guangcheng.Zhang
 * email:guangcheng.zhang@archermind.com
 * 可点击字的实体类
 */
public class CharacterSpan extends ClickableSpan {
    private static final String TAG = "CharacterSpan";
    private final int textColor;


    private String text;
    private Resources resources;
    private CharacterClickListener characterClickListener;

    public CharacterSpan(String text,int currentTextColor) {
        this.text = text;
//        this.resources = resources;
        this.textColor = currentTextColor;
    }


    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
//        ds.setTypeface(Typeface.DEFAULT_BOLD);
        ds.setColor(textColor);
        ds.setUnderlineText(false);
//        ds.setColor();
        //设置特殊颜色
    }

    public String getText() {
        return text;
    }

    @Override
    public void onClick(View widget) {
    }
}
