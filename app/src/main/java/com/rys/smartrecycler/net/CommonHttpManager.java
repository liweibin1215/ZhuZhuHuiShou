package com.rys.smartrecycler.net;

import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.lwb.framelibrary.net.converter.StringConverterFactory;
import com.lwb.framelibrary.net.download.FileDownLoadObserver;
import com.lwb.framelibrary.net.execption.ApiException;
import com.lwb.framelibrary.net.response.base.HttpResponse;
import com.rys.smartrecycler.BuildConfig;
import com.rys.smartrecycler.constant.CommonConstant;
import com.rys.smartrecycler.net.api.ApiService;

import java.io.ByteArrayInputStream;
import java.io.File;
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
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 创建时间：2019/4/11
 * 作者：李伟斌
 * 功能描述: 通用Http请求实例
 * 调用方式:
 *   CommonHttpManager.commonObserve(CommonHttpManager.getInstance().getmApiService().userLogin(null)).subscribe(new BaseHttpSubscriber<BaseEntity>() {
        @Override
        public void onSuccess(BaseEntity baseEntity) {
        }
        @Override
        public void onError(int code, String errorMsg) {
        }
        })
 *
 *
 */

public class CommonHttpManager {
    private static final int DEFAULT_CONNECT_TIMEOUT = 30;
    private Retrofit retrofit;
    private static ApiService mApiService;
    private boolean isDebug = true;
    private boolean isHttpsRequest = false;
    private static String PUB_KEY = "";//https公钥
    private static  CommonHttpManager INSTANCE;
//    private static class SingletonHolder {
//        private static final CommonHttpManager INSTANCE = new CommonHttpManager();
//    }

    /**
     * 获取请求实例
     * @return
     */
    public static CommonHttpManager getInstance() {
        if (mApiService == null) {
            synchronized (CommonHttpManager.class) {
                if (mApiService == null) {
                    INSTANCE = new CommonHttpManager();
                }
                }
            }
        return INSTANCE;
    }

    public CommonHttpManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.retryOnConnectionFailure(false);//关闭网络不太好时重试机制
        builder.connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_CONNECT_TIMEOUT*2, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_CONNECT_TIMEOUT*2, TimeUnit.SECONDS);
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
        if("".equals(CommonConstant.BASE_URL)){
            CommonConstant.BASE_URL = "Formal".equals(BuildConfig.serverType)?"http://zhuzhuhuishou.com/":"http://test.zhuzhuhuishou.com/";//http://39.96.57.72/
        }
        OkHttpClient client = builder.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(CommonConstant.BASE_URL)
                .client(client)
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//关联RxJava2
                .build();
        mApiService = retrofit.create(ApiService.class);
    }

    /**
     * 获取接口api
     * @return
     */
    public ApiService getmApiService(){
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
     * 统一的Rx线程调度切换
     *
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> ObservableTransformer<T, T> applyCommonThreadSchedulers() {
        return (ObservableTransformer<T, T>) schedulersTransformer;
    }
    /**
     *  通用请求入口一
     *  含线程调度、数据转换
     *  需要对请求结果进行处理，返回Data
     * @param observable
     * @param <T>
     * @return
     */
    public static <T> Observable<T> commonObserve(Observable<HttpResponse<T>> observable){
        return observable.compose(CommonHttpManager.<T>applyThreadSchedulers()).compose(CommonHttpManager.<T>responseDataTrans());
    }

    /**
     *  通用请求入口二
     *  含线程调度
     *  不需要对请求结果进行处理
     * @param observable
     * @param <T>
     * @return
     */
    public static <T> Observable<T> observeWithNoDataTrans(Observable<T> observable){
        return observable.compose(CommonHttpManager.<T>applyCommonThreadSchedulers());
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

    public static Disposable downloadFile(Observable<ResponseBody> observable, final String destDir, final String fileName, final FileDownLoadObserver<File> fileDownLoadObserver) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(responseBody -> fileDownLoadObserver.saveFile(responseBody, destDir, fileName))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(file -> fileDownLoadObserver.onDownLoadSuccess(file), new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        fileDownLoadObserver.onDownLoadFail(throwable);
                    }
                }, () -> fileDownLoadObserver.onComplete());
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
