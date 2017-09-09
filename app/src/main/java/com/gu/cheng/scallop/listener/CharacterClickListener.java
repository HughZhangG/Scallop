package com.gu.cheng.scallop.listener;


import android.view.View;

/**
 * Created by gc
 * 文字点击监听
 */
public interface CharacterClickListener {
    void onCharacterClick(View view, String character);
    void onClickBlank();
}
