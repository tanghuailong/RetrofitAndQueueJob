package com.vstar.sacredsun_android_pda.util.rest;

/**
 * Created by tanghuailong on 2017/1/12.
 */

import android.content.Context;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.vstar.sacredsun_android_pda.App;
import com.vstar.sacredsun_android_pda.R;

import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * 访问网络的工具类
 */
public class HttpMethods {

    //配置参数
    private static final int DEFAULT_TIME = 5;

    private Retrofit retrofit;
    //获得全局的上下文
    private static Context context = App.getInstance();

    private HttpMethods() {

        retrofit = new Retrofit.Builder()
                .client(getUnsafeOkHttpClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(ResponseConvertFactory.create())
                .baseUrl(context.getString(R.string.BASE_URL))
                .build();
    }

   //单例模式
    public static class SingleHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    public static HttpMethods getInstane(){
        return SingleHolder.INSTANCE;
    }

    public <T> T getService(Class<T> tClass) {
        return retrofit.create(tClass);
    }

    /**
     * 对SSL连接不进行验证
     * @return
     */
    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // 创建一个不验证证书的管理者
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // 按照所有信任的管理者
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // 创建一个sslSocketFactory
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            X509TrustManager trustManager = Platform.get().trustManager(sslSocketFactory);

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory,trustManager);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            builder.addNetworkInterceptor(new StethoInterceptor()).
                    addInterceptor(new CookieHeaderProvider(context)).
                    connectTimeout(DEFAULT_TIME, TimeUnit.SECONDS);

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
