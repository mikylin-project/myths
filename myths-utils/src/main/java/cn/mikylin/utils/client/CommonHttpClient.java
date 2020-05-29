package cn.mikylin.utils.client;

import cn.mikylin.myths.common.CollectionUtils;
import cn.mikylin.myths.common.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 经典的 apache httpclient 工具类
 * 依赖jar httpclient-4.5.9
 * @author mikylin
 * @date 20190715
 */
public class CommonHttpClient implements ApacheHttpClientBase{

    /**
     * 客户端主体
     */
    private CloseableHttpClient client;


    public CommonHttpClient(){
        this(15000,null);
    }

    public CommonHttpClient(int timeout,List<Header> headers){
        //连接池
        PoolingHttpClientConnectionManager manager = buildPool();
        //客户端设置
        buildClient(timeout,headers,manager);
    }


    /**
     * 线程池设置
     */
    private PoolingHttpClientConnectionManager buildPool(){
        /**
         * 创建的时候设置超时参数
         * timeToLive 超时时间
         * TimeUnit 超时的单位
         */
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager(10l, TimeUnit.SECONDS);

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
    private void buildClient(int timeout, List<Header> headers, PoolingHttpClientConnectionManager manager){

        if(CollectionUtils.isBlank(headers))
            headers = new ArrayList<>();

        this.client = HttpClients.custom()
                                //默认的请求头
                                .setDefaultHeaders(headers)
                                //存入线程池
                                .setConnectionManager(manager)
                                //存入 config
                                .setDefaultRequestConfig(buildConfig(timeout))
                                //创建
                                .build();
    }

    /**
     * 发送 get 请求
     */
    @Override
    public String get(String url, Map<String,Object> kvParam, Header... headers) throws IOException {
        HttpGet get = httpGet(url,kvParam,headers);
        return link(get);
    }


    /**
     * 发送 post 请求
     * json 格式
     */
    @Override
    public String json(String url, String json, Header... headers) throws IOException {
        StringEntity entity = jsonEntity(url, json);
        HttpPost post = httpPost(url,entity,headers);
        return link(post);
    }


    /**
     * 发送 post 请求
     * key-value 格式
     */
    @Override
    public String kv(String url, Map<String,Object> kvParam, Header... headers) throws IOException {
        StringEntity entity = kvEntity(url,kvParam);
        HttpPost post = httpPost(url,entity,headers);
        return link(post);
    }

    /**
     * 发送 post 请求
     * xml 格式（主要用于 webservice 接口）
     */
    public String xml(String url, String xml, Header... headers) throws IOException {
        StringEntity entity = new StringEntity(xml, ContentType.create("text/xml", charset));//解决中文乱码问题
        HttpPost post = httpPost(url,entity,headers);
        return link(post);
    }

    /**
     * webserivce 接口
     */
    public String soap(String url,String xml,String userName,
                       String passWord, String soapAction) throws IOException {

        List<Header> headers = new ArrayList<>();

        if (StringUtils.isNotBlank(soapAction)) //停车的立方的场库使用的是微软的 webservice 标准，所以需要传入soapAction
            headers.add(new BasicHeader("SOAPAction", soapAction));
        if(StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(passWord)){
            String check = userName + ":" + passWord;
            byte[] bytes = check.getBytes(charset);
            String encoding = Base64.getEncoder().encodeToString(bytes);
            headers.add(new BasicHeader("Authorization", "Basic " + encoding));
        }
        Header[] hs = (Header[])headers.toArray();
        return xml(url,xml,hs);
    }


    /**
     * 连接服务端，并返回字符串
     */
    private String link(HttpRequestBase req) throws IOException {
        //执行连接
        CloseableHttpResponse response = client.execute(req);
        return getResponseEntity(response);
    }










    public static void main(String[] args) throws UnsupportedEncodingException {
        CloseableHttpClient client = HttpClients.custom().build();

        String url = "http://all.vod.siqiangame.com/vod/zhenqujuping/20200409/592161-0918TUGOH德国米技炉周年庆纪念装/u.m3u8";
//        if(StringUtils.isContainChinese(url))
//            url = URLEncoder.encode(url, "UTF-8");
        HttpGet get = new HttpGet(url);
        get.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36");
        get.setHeader("Upgrade-Insecure-Requests","1");
        try {
            HttpContext httpContext = new BasicHttpContext();
            CloseableHttpResponse execute = client.execute(get,httpContext);
            HttpHost currentHost = (HttpHost) httpContext.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
            HttpUriRequest req = (HttpUriRequest) httpContext.getAttribute(ExecutionContext.HTTP_REQUEST);
            System.out.println(currentHost.toURI() + req.getURI());
            HttpEntity entity = execute.getEntity();
            String s = EntityUtils.toString(entity, "UTF-8");
            System.out.println(s);

            Pattern p = Pattern.compile("#EXTINF:[0-9]\\d*\\.?\\d*,");
            Matcher m = p.matcher(s);
            System.out.println(m.find());
            while (m.find()) {
                String group = m.group(0);
                String substring = group.substring(8, group.length() - 1);
                System.out.println(substring);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
