package cn.mikylin.myths.common;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * summarized by jdk11 HttpClient
 * @author mikylin
 * @date 20190817
 */
public class JDK11HttpClient {

    private static String CONTENT_TYPE = "Content-Type";
    private static String APPLICATION_JSON = "application/json";

    private static Integer TIMEOUT = 5000;

    private HttpClient client;

    public JDK11HttpClient() {
        this(TIMEOUT, HttpClient.Version.HTTP_1_1, Executors.newFixedThreadPool(5));
    }

    public JDK11HttpClient(long timeout, HttpClient.Version defaultHttpVersion,
                           ExecutorService pool){
        //创建 builder
        HttpClient.Builder builder = HttpClient.newBuilder();

        //链式调用
        client = builder

                // 默认的 http 协议版本  1.1 或者 2
                .version(defaultHttpVersion) //.version(HttpClient.Version.HTTP_1_1)

                //连接超时时间，单位为毫秒
                .connectTimeout(Duration.ofMillis(timeout)) //.connectTimeout(Duration.ofMinutes(1))

                //连接完成之后的转发策略
                .followRedirects(HttpClient.Redirect.NEVER) //.followRedirects(HttpClient.Redirect.ALWAYS)

                //指定线程池
                .executor(pool)

                //认证，默认情况下 Authenticator.getDefault() 是 null 值，会报错
                //.authenticator(Authenticator.getDefault())

                //代理地址
                //.proxy(ProxySelector.of(new InetSocketAddress("http://www.baidu.com", 8080)))

                //缓存，默认情况下 CookieHandler.getDefault() 是 null 值，会报错
                //.cookieHandler(CookieHandler.getDefault())

                //创建完成
                .build();
    }


    public String json(String url,String json,HttpClient.Version version) {
        //创建 builder
        HttpRequest.Builder reBuilder = HttpRequest.newBuilder();

        //链式调用
        reBuilder
                //存入消息头
                //消息头是保存在一张 TreeMap 里的
                .header(CONTENT_TYPE, APPLICATION_JSON)

                //url 地址
                .uri(URI.create(url))

                //超时时间
                .timeout(Duration.ofMillis(TIMEOUT))

                //method(...) 方法是 POST(...) 和 GET(...) 方法的底层，效果一样
                //.method("POST",HttpRequest.BodyPublishers.ofString("hello"))

                //发起一个 post 消息，需要存入一个消息体
                .POST(HttpRequest.BodyPublishers.ofString(json));

        if(version != null){
            // http 协议版本
            reBuilder.version(version);
        }

        HttpRequest request = reBuilder
                                //创建完成
                                .build();

        return link(request);
    }

    public String get(String url,HttpClient.Version version) {
        //创建 builder
        HttpRequest.Builder reBuilder = HttpRequest.newBuilder();

        reBuilder
                //存入消息头
                //消息头是保存在一张 TreeMap 里的
                .header(CONTENT_TYPE, APPLICATION_JSON)

                //url 地址
                .uri(URI.create(url))

                //超时时间
                .timeout(Duration.ofMillis(TIMEOUT))

                //发起一个 get 消息，get 不需要消息体
                .GET();

        if(version != null){
            // http 协议版本
            reBuilder.version(version);
        }

        HttpRequest request = reBuilder
                                //创建完成
                                .build();

        return link(request);

    }



    private String link(HttpRequest request) {
        HttpResponse<String> response = null;
        try {
            response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(response.statusCode() == 200)
            return response.body();
        return null;
    }



}
