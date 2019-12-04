package cn.mikylin.myths;

import cn.mikylin.utils.client.CommonHttpClient;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class HttpTest {

    @Test
    public void http() throws IOException {
        CommonHttpClient client = new CommonHttpClient();

        Header h = new BasicHeader("Content-Type","application/json");

        String json = client.json("http://localhost:8080/test",
                "{\"aaa\" : 2,\"bbb\" : 1}",h);

        System.out.println(json);

    }
}
