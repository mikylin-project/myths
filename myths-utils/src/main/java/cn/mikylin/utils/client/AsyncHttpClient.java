package cn.mikylin.utils.client;

import cn.mikylin.myths.common.CollectionUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * apache httpasyncclient 工具类
 * 依赖jar httpasyncclient-4.1.4
 * @author mikylin
 * @date 20190715
 */
public class AsyncHttpClient implements ApacheHttpClientBase{

    //客户端主体
    private CloseableHttpAsyncClient client;

    public AsyncHttpClient(){
        this(15000,null);
    }

    public AsyncHttpClient(int timeout,List<Header> headers){
        //连接池
        PoolingNHttpClientConnectionManager manager = buildPool();
        //客户端设置
        buildClient(timeout,headers,manager);
    }

    /**
     * 线程池设置
     */
    private PoolingNHttpClientConnectionManager buildPool(){

        //配置io线程
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                                                        //线程数
                                                        .setIoThreadCount(10)
                                                        //超时时间
                                                        .setConnectTimeout(10)
                                                        .setSoKeepAlive(true)
                                                        .build();

        ConnectingIOReactor ioReactor;
        try {
            ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
        } catch (IOReactorException e) {
            throw new RuntimeException();
            //此处需要打开 Selector，会需要捕捉异常
            //一般情况下不会出错，如果报错了，肯定是出了系统级错误
        }

        /**
         * 创建的时候设置超时参数
         * timeToLive 超时时间
         * TimeUnit 超时的单位
         */
        PoolingNHttpClientConnectionManager manager = new PoolingNHttpClientConnectionManager(ioReactor);

        /**
         * 设置每个路由分配的最大连接数
         * 如果路由非常集中，可以设置大一些
         * 默认 2
         */
        manager.setDefaultMaxPerRoute(5);

        /**
         * 连接池的最大连接数
         * 默认 20
         */
        manager.setMaxTotal(20);

        return manager;
    }

    /**
     * 客户端主体设置
     */
    private void buildClient(int timeout, List<Header> headers, PoolingNHttpClientConnectionManager manager){

        if(CollectionUtils.isBlank(headers))
            headers = new ArrayList<>();

        this.client = HttpAsyncClients.custom()
                                    //默认的请求头
                                    .setDefaultHeaders(headers)
                                    //存入线程池
                                    .setConnectionManager(manager)
                                    //存入 config
                                    .setDefaultRequestConfig(buildConfig(timeout))
                                    //创建
                                    .build();
        this.client.start();
    }

    /**
     * get 请求
     */
    @Override
    public String get(String url, Map<String, Object> kvParam, Header... headers) throws IOException, ExecutionException, InterruptedException {

        Future<HttpResponse> future = asyncGet(url, kvParam, headers);
        HttpResponse httpResponse = future.get();
        return getResponseEntity(httpResponse);
    }

    /**
     * 异步 get 请求
     */
    public Future<HttpResponse> asyncGet(String url, Map<String, Object> kvParam, Header... headers) throws IOException {
        HttpGet get = httpGet(url,kvParam,headers);
        return link(get);
    }

    /**
     * post json 请求
     */
    @Override
    public String json(String url, String json, Header... headers) throws IOException, ExecutionException, InterruptedException {
        Future<HttpResponse> future = asyncJson(url, json, headers);
        HttpResponse httpResponse = future.get();
        return getResponseEntity(httpResponse);
    }

    /**
     * 异步 post json 请求
     */
    public Future<HttpResponse> asyncJson(String url, String json, Header... headers) throws IOException {
        StringEntity entity = jsonEntity(url, json);
        HttpPost post = httpPost(url,entity,headers);
        return link(post);
    }

    /**
     * post kv 请求
     */
    @Override
    public String kv(String url, Map<String, Object> kvParam, Header... headers) throws IOException, ExecutionException, InterruptedException {
        Future<HttpResponse> future = asyncKv(url, kvParam, headers);
        HttpResponse httpResponse = future.get();
        return getResponseEntity(httpResponse);
    }

    /**
     * 异步 post kv 请求
     */
    public Future<HttpResponse> asyncKv(String url, Map<String, Object> kvParam, Header... headers) throws IOException {
        StringEntity entity = kvEntity(url,kvParam);
        HttpPost post = httpPost(url,entity,headers);
        return link(post);
    }



    /**
     * 连接服务端，并返回字符串
     */
    private Future<HttpResponse> link(HttpRequestBase req) {
        return client.execute(req, new Back());
    }


    /**
     * 异步 http 中类似 aio 的回调机制
     * 只要请求获得结果就会返回
     */
    private static class Back implements FutureCallback<HttpResponse> {

        private long start = System.currentTimeMillis();
        Back(){}

        /**
         * 使用 EntityUtils 获取 response 中的数据，只有一次
         * 即，如果已经被拿取过，第二次就拿不到了
         */
        public void completed(HttpResponse httpResponse) {
            try {
                System.out.println("cost is:" + (System.currentTimeMillis() - start) + ":" + EntityUtils.toString(httpResponse.getEntity()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void failed(Exception e) {
            System.err.println("cost is:" + (System.currentTimeMillis() - start) + ":" + e);
        }

        public void cancelled() {

        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        AsyncHttpClient c = new AsyncHttpClient();
        String a = c.get("http://baidu.com",null,null);
        System.out.println(a);
    }
}
