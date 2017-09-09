package com.gu.cheng.scallop.api;

import com.gu.cheng.scallop.api.model.BaseRes;
import com.gu.cheng.scallop.api.model.WordDetail;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by gc on 2017/9/8.
 */
public interface Api {
    //URL： https://api.shanbay.com/bdc/search/?word={word}
    @GET("bdc/search/")
    Observable<BaseRes<WordDetail>> searchWord(@Query("word") String word);
}
