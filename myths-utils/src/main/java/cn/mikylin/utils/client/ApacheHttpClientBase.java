package cn.mikylin.utils.client;

import cn.mikylin.myths.common.ArrayUtils;
import cn.mikylin.myths.common.MapUtils;
import cn.mikylin.myths.common.StringUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * the base interface for apache http client
 * @author mikylin
 * @date 20190715
 */
public interface ApacheHttpClientBase {

    //编码格式
    String charset = "UTF-8";


    //get 请求
    String get(String url, Map<String, Object> kvParam, Header... headers) throws IOException, ExecutionException, InterruptedException;
    //post json 请求
    String json(String url, String json, Header... headers) throws IOException, ExecutionException, InterruptedException;
    //post kv 请求
    String kv(String url, Map<String, Object> kvParam, Header... headers) throws IOException, ExecutionException, InterruptedException;


    /**
     * 创建 config
     */
    default RequestConfig buildConfig(int timeout){
        RequestConfig config = RequestConfig.custom()
                //连接超时时间 单位毫秒
                .setConnectTimeout(timeout)
                //socket 超时时间 单位毫秒
                .setSocketTimeout(timeout)
                //request 超时时间 单位毫秒
                .setConnectionRequestTimeout(timeout)
                //创建
                .build();
        return config;
    }



    /**
     * 确保 url 不为空
     */
    default void assertUrlNotBlank(String url){
        if(StringUtils.isBlank(url))
            throw new NullPointerException("url can not be blank");
    }

    /**
     * 存入请求头
     */
    default void setHeaders(HttpRequestBase req, Header[] headers){
        if(ArrayUtils.isNotBlank(headers))
            req.setHeaders(headers);
    }

    /**
     * 组装 HttpGet 对象
     */
    default HttpGet httpGet(String url, Map<String, Object> kvParam, Header... headers){
        assertUrlNotBlank(url);

        //组装 url
        if(MapUtils.isNotBlank(kvParam)){

            StringBuilder builder = new StringBuilder();
            kvParam.forEach((k,v) ->{
                builder.append(k);
                builder.append("=");
                builder.append(v);
                builder.append("&");
            });
            builder.deleteCharAt(builder.length() - 1);

            String strParams = builder.toString();
            url = url + "?" + strParams;
        }

        HttpGet get = new HttpGet(url);
        setHeaders(get,headers);
        return get;
    }

    /**
     * 组装 HttpPost 对象
     */
    default HttpPost httpPost(String url, HttpEntity entity, Header... headers){
        //组装 HttpPost
        HttpPost post = new HttpPost(url);
        setHeaders(post,headers);
        post.setEntity(entity);
        return post;
    }



    /**
     * 处理 response 中的内容
     */
    default String getResponseEntity(HttpResponse response) throws IOException{
        if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
            throw new IOException();

        //返回的 entity，转成字符串返回
        HttpEntity responseEntity = response.getEntity();

        return EntityUtils.toString(responseEntity, charset);
    }


    /**
     * 用于组装 post kv 格式的 entity
     */
    default StringEntity kvEntity(String url, Map<String, Object> kvParam) throws UnsupportedEncodingException {
        assertUrlNotBlank(url);

        List<NameValuePair> list = new ArrayList<>();

        if(MapUtils.isNotBlank(kvParam)){
            kvParam.forEach((k,v) ->
                    list.add(new BasicNameValuePair(k, String.valueOf(v)))
            );
        }

        StringEntity entity = new UrlEncodedFormEntity(list, charset); //解决中文乱码问题
        return entity;
    }

    /**
     * 用于组装 post json 格式的 entity
     */
    default StringEntity jsonEntity(String url, String json){
        assertUrlNotBlank(url);
        StringEntity entity = new StringEntity(json, ContentType.create("application/json", charset));//解决中文乱码问题
        return entity;
    }
}
