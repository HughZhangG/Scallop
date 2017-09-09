package com.gu.cheng.scallop.api;

import com.gu.cheng.scallop.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gc on 2017/9/8.
 */
public class HttpManager {
    private static final String BASE_URL = "https://api.shanbay.com/";
    private final Retrofit mRetrofit;

    private HttpManager(){
        mRetrofit = createRetrofit();
    }


    private static class HttpManagerHolder{
        private static final HttpManager INSTANCE = new HttpManager();
    }
    public static final HttpManager getInstance(){
        return HttpManagerHolder.INSTANCE;
    }

    /**
     *
     * @return
     */
    private Retrofit createRetrofit() {
        //初始化okHttp
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(9, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS);

        if (BuildConfig.DEBUG){
            // 如果为 debug 模式，则添加日志拦截器
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(builder.build())// 传入请求客户端
                .addConverterFactory(GsonConverterFactory.create()) // 添加Gson转换工厂
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())// 添加RxJava2调用适配工厂
                .build();
        return retrofit;
    }

    private  <T> T checkNotNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }


    /**
     * 创建相应的服务接口
     */
    public <T> T create(Class<T> service){
        checkNotNull(service, "service is null");
        return mRetrofit.create(service);
    }


}
