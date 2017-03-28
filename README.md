### 技术点
~~* 通过 jobqueue 实现缓存retrofit失败的请求(4.4 gms一直有问题，5.0 测试通过 待修改)~~
* 通过老版本的jobqueue实现队列功能(已完成)
* 通过拦截器设置cookie (已实现)
* 搭建网络访问的基础工具类(已实现)
* 通过stetho网络调试(已完成)
* 通过RxJava 实现事件总线 RxBus(已完成)

----
#### 说明
> **存在的的问题**
* Activity 的职责过于臃肿，基本上所有业务逻辑都处于Activity里面。`MVP`+`DI`(依赖注入)是一个很好的解决办法，可惜自己力有未逮

<br>
>**技术实现**
 采用了[Retrofit][1]+[RxJava][2]的方式实现数据获取,[android-priority-job][3]缓存请求，时间处理采用了[threetenabp][4](在Android上实现了JSR-310标准，即类似于Java8中的时间处理，十分便利)
<br>

* 基本的访问网络的逻辑

```
 HttpMethods.getInstane().getService(PDAApi.class)
                    .userLoginOut(driverSession)
                    .compose(RxHelper.io_main())
                    .subscribe((r) -> {
                        //逻辑代码
                        }
                    },(e) -> {
                        //错误处理
                    });
```
* 新建Job(Job可以看作对请求的一层包装，因为要求断线重连的机制，类似于每次请求都会新建一个job添加到队列中去，余下工作(序列化，保存，断开重试，检测网络等等)都交给了[android-priority-job][3],官方的文档详细描述了使用方法，下面是一个简略的使用范例。
```
public class UnBindJob extends Job{

    private UnBindJob() {
        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy("order"));
    }

    @Override
    public void onAdded() {

    }
    @Override
    public void onRun() throws Throwable {
        HttpMethodNoRx.getInstane().getService(PDAApiNoRx.class)
                .orderUnBind(assetsCode,session)
                .execute();
        }
    }
    @Override
    protected void onCancel() {

        }
    }
    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        Log.d(LOG_TAG,"job execute fail");
        //Exceptionoe为ApiException时候不需要去重试
        if(throwable instanceof ApiException) {
            return false;
        }else {
            return true;
        }
    }
}
```
* 添加Job的使用范例
```
jobManager = App.getInstance().getJobManager();
jobManager.addJobInBackground(UnBindJob.create().setAssetsCode(scanResult).setSession(session));
```

> 整个流程如上面所示， 其中项目中可能难以理解的部分，包括搭建的Retrofit+RxJava的访问逻辑。下面就详细的解释一下

* 首先HttpMethods是一个单例模式，保证整个项目只会有一个实例
```
public class HttpMethods {

    //配置参数
    private static final int DEFAULT_TIME = 5;

    private Retrofit retrofit;
    //获得全局的上下文
    private static Context context = App.getInstance();

    private HttpMethods() {
    }
   //单例模式
    public static class SingleHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }
    public static HttpMethods getInstane(){
        return SingleHolder.INSTANCE;
    }
}
```
* 其次通过在OKHttp里面添加拦截器(责任链模式)的方式，实现cookie的添加。另外采用了类似于建造者模式，往Retrofit的Builder里面添加工厂类。
 * 添加cookie拦截器(CookieHeaderProvider)

```
builder.addNetworkInterceptor(new StethoInterceptor()).
                    addInterceptor(new CookieHeaderProvider(context)).
                    connectTimeout(DEFAULT_TIME, TimeUnit.SECONDS);
```
  * 添加自定义的异常处理的工厂类(ResponseConvertFactory)

```
 retrofit = new Retrofit.Builder()
                .client(getUnsafeOkHttpClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(ResponseConvertFactory.create())
                .baseUrl(context.getString(R.string.BASE_URL))
                .build();
```

* https的处理，将会信任所有证书，防止请求https链接的时候报SSL相关的错误

```
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
```
* 关于cookie的处理，将会在每次请求的时候，在请求头添加一个cookie

```
public class CookieHeaderProvider implements Interceptor {

    public static final  String PREF_COOKIES = "PREF_COOKIES";
    private Context context  = App.getInstance();

    public CookieHeaderProvider(Context context) {
        this.context = context;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        String result = "";
        String workerSession = (String) SPHelper.get(context,context.getString(R.string.WORKER_SESSION),"");
        String driverSession = (String) SPHelper.get(context,context.getString(R.string.DRIVER_SESSION),"");
        if(workerSession.isEmpty() || driverSession.isEmpty()) {
            if(!workerSession.isEmpty()) {
                result = "worker="+workerSession;
            }
            if(!driverSession.isEmpty()) {
                result = "driver="+driverSession;
            }
        }else {
            result = "driver="+workerSession+";"+"worker="+driverSession;
        }
        if(!TextUtils.isEmpty(result)) {
            builder.addHeader("Cookie", result);
        }
        return chain.proceed(builder.build());
    }
}
```
<br>


> jackOptions 加 annotationProcessor 是个深坑。。。慎重


[1]:https://github.com/square/retrofit "retrofit"
[2]:https://github.com/ReactiveX/RxJava "RxJava"
[3]:https://github.com/path/android-priority-jobqueue "android-pripority-job"
[4]:https://github.com/JakeWharton/ThreeTenABP
