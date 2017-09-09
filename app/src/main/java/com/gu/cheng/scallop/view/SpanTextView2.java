package com.gu.cheng.scallop.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.gu.cheng.scallop.R;
import com.gu.cheng.scallop.listener.CharacterClickListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gc on 2017/9/7.
 *
 * 针对英语单词  点击查询效果
 */
public class SpanTextView2 extends TextView {


    private static final String TAG = "SpanTextView2";
    /**
     * 文字点击事件
     */
    private CharacterClickListener characterClickListener;

    /**
     * 文字选中背景颜色 和 文字颜色
     */
    private int mSelectedBgColor = getResources().getColor(R.color.default_selected_bg);
    private int mSelectedWordColor = getResources().getColor(R.color.default_selected_word);
    /**
     * 文字原本颜色
     */
    private int mTextColor;
    /**
     * 上一次选中部分
     */
    private int mLastStart;
    private int mLastEnd;

    public SpanTextView2(Context context) {
        super(context);
        init(null);
    }

    public SpanTextView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SpanTextView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SpanTextView2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        final Context context = getContext();
        mTextColor = getCurrentTextColor();
        setMovementMethod(new LinkMovementMethod());
        setHighlightColor(Color.TRANSPARENT);


    }


    @Override
    public boolean performLongClick() {
        try {
            return super.performLongClick();
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
//        throw new RuntimeException("use setText2(CharSequence text) instead");
    }



    /**
     * @param text
     */
    public void setText2(CharSequence text){
        SpannableStringBuilder builder = null;
        if (text instanceof SpannableStringBuilder) {
            builder = (SpannableStringBuilder) text;
        } else {
            builder = new SpannableStringBuilder(text);
        }
        /**
         * 将单词转化为Span
         */
        Pattern pattern = Pattern.compile("[A-Za-z0-9]+[-]*[A-Za-z0-9]+");

        Matcher matcher = pattern.matcher(text.toString());

        while (matcher.find()) {
            CharacterSpan span = new CharacterSpan(matcher.group(),
                    mTextColor);
            builder.setSpan(span, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        append(builder);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final boolean result = super.onTouchEvent(event);
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();

        x -= getTotalPaddingLeft();
        y -= getTotalPaddingTop();

        x += getScrollX();
        y += getScrollY();

        Layout layout = getLayout();
        int line = layout.getLineForVertical((int) y);
        int off = layout.getOffsetForHorizontal(line, x);

        float v = layout.getPrimaryHorizontal(off);// 获取 屏幕显示点击字符 的水平x

        if (x > v){
            off++;
        }

        if (characterClickListener == null) {
            return false;
        }

        if (action == MotionEvent.ACTION_DOWN) {// 解决点击末尾空白处最后一个span响应的bug
            if (x < 0 || x > layout.getLineWidth(line) + 1) {
                removeSelection();
                characterClickListener.onClickBlank();
                return false;
            }
        }

        CharSequence text = getText();
        if (TextUtils.isEmpty(text) || !(text instanceof Spannable)) {
            return result;
        }

        Spannable buffer = (Spannable) text;
        final ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);

        if (link.length != 0) {
            if (action == MotionEvent.ACTION_DOWN){
                //1.恢复上一次的选中颜色
                //2.选中新的部分
                removeSelection();


                int spanStart = buffer.getSpanStart(link[0]);
                int spanEnd = buffer.getSpanEnd(link[0]);

                if (spanStart >=0 && spanEnd > spanStart){
                    mLastStart = spanStart;
                    mLastEnd = spanEnd;

                    Selection.setSelection(buffer,spanStart,spanEnd);
                }else {
                    characterClickListener.onClickBlank();
                }
            }else if (action == MotionEvent.ACTION_UP
                /*|| action == MotionEvent.ACTION_CANCEL*/
                    ) {
                // 1,改变选中字背景
                // 2,返回选中数据
                int start = Selection.getSelectionStart(buffer);
                int end = Selection.getSelectionEnd(buffer);
                if (start >= 0 && end > start) {
                    buffer.setSpan(new BackgroundColorSpan(mSelectedBgColor)
                            , start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    buffer.setSpan(new ForegroundColorSpan(mSelectedWordColor)
                            , start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    characterClickListener.onCharacterClick(this, buffer.subSequence(start, end).toString());
                }
            }else if (action == MotionEvent.ACTION_CANCEL){
                removeSelection();
                characterClickListener.onClickBlank();
            }
            return true;
        }else {
            removeSelection();
            characterClickListener.onClickBlank();
        }

        return result;
    }

    /**
     * 删除选中效果
     */
    public void removeSelection(){

        Spannable buffer = (Spannable) getText();
        final int start = mLastStart;
        final int end = mLastEnd;

        if (start >= 0 && end > start) {
            buffer.setSpan(new BackgroundColorSpan(Color.TRANSPARENT)
                    , start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            buffer.setSpan(new ForegroundColorSpan(mTextColor)
                    , start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            Log.d(TAG, "removeSelection: "+buffer.subSequence(start,end).toString());
        }

        Selection.removeSelection(buffer);
    }

    /**
     *
     * 设置点击事件
     * @param characterClickListener
     */
    public void setCharacterClickListener(CharacterClickListener characterClickListener) {
        this.characterClickListener = characterClickListener;
    }

}
