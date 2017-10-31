package com.gu.cheng.scallop.view;

import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gu.cheng.scallop.R;
import com.gu.cheng.scallop.api.model.WordDetail;

/**
 * Created by gc on 2017/9/8.
 * <p>
 * BottomSheet管理类
 */
public class BottomSheetManager {

    private static final String TAG = "BottomSheetManager";

    private TextView tvWord;//词
    private TextView tvWordSymbol;//音标
    private ImageView ivTrumpet;//喇叭
    private TextView tvParaph;//释义
    private View hideLayout;//隐藏
    private final BottomSheetBehavior<ViewGroup> behavior;
    private View loadingView;//加载
    private final Resources mResources;
    private MediaPlayer mediaPlayer;
    private String audio;

    public BottomSheetManager(@NonNull ViewGroup viewGroup) {
        init(viewGroup);
        mResources = viewGroup.getResources();
        int heightPixels = mResources.getDisplayMetrics().heightPixels;
        ViewGroup.LayoutParams layoutParams = viewGroup.getLayoutParams();
        layoutParams.height = heightPixels - 200;
        viewGroup.setLayoutParams(layoutParams);

        behavior = BottomSheetBehavior.from(viewGroup);
//        behavior.setPeekHeight(heightPixels);

    }

    private void init(ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt instanceof ViewGroup) {
                init((ViewGroup) childAt);
                continue;
            }
            int id = childAt.getId();
            switch (id) {
                case R.id.id_tv_word:
                    tvWord = (TextView) childAt;
                    break;
                case R.id.id_tv_word_symbol:
                    tvWordSymbol = (TextView) childAt;
                    break;
                case R.id.id_iv_trumpet:
                    ivTrumpet = (ImageView) childAt;
                    ivTrumpet.setOnClickListener((view) -> playUrl(audio));
                    break;
                case R.id.id_tv_paraphrase:
                    tvParaph = (TextView) childAt;
                    break;
                case R.id.id_layout_collapse:
                    hideLayout = childAt;
                    break;
                case R.id.id_loading_view:
                    loadingView = childAt;
                    break;
            }
        }


        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }


    /**
     * 播放
     *
     * @param url
     */
    private void playUrl(String url) {
        if (mediaPlayer != null && !TextUtils.isEmpty(url)) {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepare();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
     * Sets the state of the bottom sheet. The bottom sheet will transition to that state with
     * animation.
     *
     * @param state One of {@link android.support.design.widget.BottomSheetBehavior.State #STATE_COLLAPSED
     * }, {@link BottomSheetBehavior #STATE_EXPANDED}, or
     *              {@link BottomSheetBehavior #STATE_HIDDEN}.
     */
    public BottomSheetManager setState(final @BottomSheetBehavior.State int state) {
        if (behavior != null)
            behavior.setState(state);
        return this;
    }


    /**
     * set callback for changes
     *
     * @param callback
     */
    public BottomSheetManager setBottomSheetCallback(BottomSheetBehavior.BottomSheetCallback callback) {
        behavior.setBottomSheetCallback(callback);
        return this;
    }

    public BottomSheetManager showLoading() {
        if (loadingView != null)
            loadingView.setVisibility(View.VISIBLE);
        return this;
    }

    public BottomSheetManager hideLoading() {
        if (loadingView != null)
            loadingView.setVisibility(View.GONE);
        return this;
    }


    /**
     * 设置参数
     *
     * @param data
     * @return
     */
    public BottomSheetManager setData(WordDetail data) {
        Log.d(TAG, "setData: " + data.getContent());
        hideLoading();
        if (tvWord != null) {
            String content = data.getContent();
            if (TextUtils.isEmpty(content)) {
                content = mResources.getString(R.string.no_data);
            }
            tvWord.setText(content);
        }
        if (tvWordSymbol != null) {
            tvWordSymbol.setText(data.getPronunciation());
        }

        if (tvParaph != null) {
            tvParaph.setText(data.getDefinition());
        }

        audio = data.getAudio();


        return this;
    }


}
