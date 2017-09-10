package com.gu.cheng.scallop;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.gu.cheng.scallop.api.Api;
import com.gu.cheng.scallop.api.HttpManager;
import com.gu.cheng.scallop.api.model.BaseRes;
import com.gu.cheng.scallop.api.model.WordDetail;
import com.gu.cheng.scallop.listener.CharacterClickListener;
import com.gu.cheng.scallop.view.BottomSheetManager;
import com.gu.cheng.scallop.view.SpanTextView2;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {


    private BottomSheetManager bottomSheetManager;



    private SpanTextView2 spanTextView;
    private String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        date = getString(R.string.content);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        initView();
    }




    private void initView() {
        spanTextView = (SpanTextView2) findViewById(R.id.tv);

        /**
         * 1，必须在调用setText2()方法前设置监听事件
         * 2，如果setText2()有多次调用的可能必须在调用前先清除spanTextView2文本
         *    即：spanTextView2.setText("");
         */

        spanTextView.setCharacterClickListener(characterClickListener);
        spanTextView.setText("");
        spanTextView.setText2(date);


        // get the bottom sheet view
        ViewGroup layoutBottomSheet = (ViewGroup) findViewById(R.id.bottom_sheet);
        bottomSheetManager = new BottomSheetManager(layoutBottomSheet);
        bottomSheetManager.setState(BottomSheetBehavior.STATE_HIDDEN);


        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.RECORD_AUDIO);
    }

    /**
     * 单词点击监听
     */
    CharacterClickListener characterClickListener = new CharacterClickListener() {

        @Override
        public void onCharacterClick(View view, String character) {
            // TODO Auto-generated method stub
            //网络请求

//            Toast.makeText(MainActivity.this, character, Toast.LENGTH_SHORT).show();
            bottomSheetManager.setState(BottomSheetBehavior.STATE_COLLAPSED);

            getWordDetail(character);
        }

        @Override
        public void onClickBlank() {
            bottomSheetManager.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    };


    /**
     * 网络请求
     * @param character
     */
    private void getWordDetail(String character) {
        bottomSheetManager.showLoading();
        HttpManager.getInstance().create(Api.class)
                .searchWord(character)
                .subscribeOn(Schedulers.io()) //  指定被观察者的操作在io线程中完成
                .observeOn(AndroidSchedulers.mainThread())// 指定观察者接收到数据，然后在Main线程中完成
                .subscribe(new Observer<BaseRes<WordDetail>>() {
                               @Override
                               public void onSubscribe(@NonNull Disposable d) {
                               }

                               @Override
                               public void onNext(@NonNull BaseRes<WordDetail> searchWordRes) {
                                   WordDetail data = searchWordRes.getData();
                                   bottomSheetManager.setData(data);
                               }

                               @Override
                               public void onError(@NonNull Throwable e) {
                               }

                               @Override
                               public void onComplete() {
                               }
                           }
                );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_jump){

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 回收播放器
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bottomSheetManager != null) {
            bottomSheetManager.stop();
            bottomSheetManager = null;
        }
    }
}
