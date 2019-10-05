package com.lwb.framelibrary.net.BaseExample;

import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.lwb.framelibrary.net.converter.StringConverterFactory;
import com.lwb.framelibrary.net.execption.ApiException;
import com.lwb.framelibrary.net.interceptor.CustomInterceptor;
import com.lwb.framelibrary.net.response.base.HttpResponse;

import java.io.ByteArrayInputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 创建时间：2019/4/11
 * 作者：李伟斌
 * 功能描述:
 */

public class BaseHttpManager {
    public static String BASE_URL = "";
    private static final int DEFAULT_CONNECT_TIMEOUT = 30;
    private static final int DEFAULT_WRITE_TIMEOUT = 60;
    private static final int DEFAULT_READ_TIMEOUT = 60;
    private Retrofit retrofit;
    private static ExampleApiService mApiService;
    private boolean isDebug = false;
    private boolean isHttpsRequest = false;
    private static String PUB_KEY = "";//https公钥
    private static class SingletonHolder {
        private static final BaseHttpManager INSTANCE = new BaseHttpManager();
    }

    /**
     * 获取请求实例
     * @return
     */
    public static BaseHttpManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public BaseHttpManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.retryOnConnectionFailure(false);//关闭网络不太好时重试机制
        builder.connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS);
        builder.addInterceptor(new CustomInterceptor());//添加请求头部
        if (isDebug) {
            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        if (isHttpsRequest) {
            SSLSocketFactory sslSocketFactory = null;
            try {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
                sslSocketFactory = sslContext.getSocketFactory();
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                e.printStackTrace();
            }
            final HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(final String hostname,
                                      final SSLSession session) {
                    //服务端主机域名地址校验
                    return true;
                }
            };
            builder.hostnameVerifier(hostnameVerifier).sslSocketFactory(sslSocketFactory, trustManager);
        }
        OkHttpClient client = builder.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//关联RxJava2
                .build();
        mApiService = retrofit.create(ExampleApiService.class);
    }

    /**
     * 获取接口api
     * @return
     */
    public ExampleApiService getmApiService(){
        return mApiService;
    }

    /**
     * 获取对应的Service
     *
     * @param service Service 的 class
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service) {
        return retrofit.create(service);
    }

    /**
     * 统一的Rx线程调度切换
     *
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> ObservableTransformer<HttpResponse<T>, HttpResponse<T>> applyThreadSchedulers() {
        return (ObservableTransformer<HttpResponse<T>, HttpResponse<T>>) schedulersTransformer;
    }

    /**
     *  通用请求入口
     * @param observable
     * @param <T>
     * @return
     */
    public static <T> Observable<T> commonObserve(Observable<HttpResponse<T>> observable){
        return observable.compose(BaseHttpManager.<T>applyThreadSchedulers()).compose(BaseHttpManager.<T>responseDataTrans());
    }

    /**
     * 优雅的处理接口返回标准解析
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<HttpResponse<T>, T> responseDataTrans() {
        return new ObservableTransformer<HttpResponse<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<HttpResponse<T>> upstream) {
                return upstream.map(new Function<HttpResponse<T>, T>() {
                    @Override
                    public T apply(HttpResponse<T> response) throws Exception {
                        if (response.isSuccessful()) {
                            if(response.getData() != null){
                                return response.getData();
                            }else{
                                throw new ApiException(ApiException.Code_Data_Null, response.getMessage());
                            }
                        } else {
                            throw new ApiException(response.getCode(), response.getMessage());
                        }
                    }
                });
            }
        };
    }

    /**
     * 重用 使用一个单例 ObservableTransformer 线程切换
     */
    static final ObservableTransformer schedulersTransformer = new ObservableTransformer() {
        @Override
        public ObservableSource apply(Observable upstream) {
            return upstream.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    /**
     * Rx优雅处理服务器返回,成功返回T实体，失败抛出异常（服务器返回的code和message）
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<HttpResponse<T>, HttpResponse<T>> handleResultBase() {
        return new ObservableTransformer<HttpResponse<T>, HttpResponse<T>>() {
            @Override
            public ObservableSource<HttpResponse<T>> apply(Observable<HttpResponse<T>> upstream) {
                return upstream.flatMap(new Function<HttpResponse<T>, ObservableSource<HttpResponse<T>>>() {
                    @Override
                    public ObservableSource<HttpResponse<T>> apply(@io.reactivex.annotations.NonNull HttpResponse<T> response) throws Exception {
                        if (response.isSuccessful()) {
                            return createData(response);
                        } else {
                            throw new ApiException(response.getCode(), response.getMessage());
                        }
                    }
                });
            }
        };

    }

    /**
     * 创建成功的数据
     *
     * @param data
     * @param <T>
     * @return
     */
    private static <T> Observable<HttpResponse<T>> createData(final HttpResponse<T> data) {
        return Observable.create(new ObservableOnSubscribe<HttpResponse<T>>() {
            @Override
            public void subscribe(ObservableEmitter<HttpResponse<T>> observableEmitter) throws Exception {
                try {
                    //数据处理....

                    observableEmitter.onNext(data);
                    observableEmitter.onComplete();
                } catch (Exception e) {
                    observableEmitter.onError(e);
                }
            }
        });

    }


    private static X509TrustManager trustManager = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            //校验客户端证书
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            //校验服务端证书
            X509Certificate ca = (X509Certificate) CertificateFactory.getInstance("X.509")
                    .generateCertificate(new ByteArrayInputStream(PUB_KEY.getBytes()));
            for (X509Certificate cert : chain) {
                // 检查服务端证书是否过期
                cert.checkValidity();
                try {
                    //和APP预埋证书对比
                    cert.verify(ca.getPublicKey());
                } catch (Exception e) {
                    //证书校验异常
                    throw new SecurityException("证书错误！");
                }
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    };
}
