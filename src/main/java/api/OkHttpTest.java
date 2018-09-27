package api;

import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import util.CommonMethod;
import util.JSONPathUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OkHttpTest {

    static Logger log = Logger.getLogger(OkHttpTest.class.getName());
    // 创建okhttp对象
    static OkHttpClient client = new OkHttpClient();
    // 设置请求体的数据类型
    static final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
    static String Host = "https://alpha-api.wanmen.org/4.0";

    /**
     * 注册接口
     *
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String userSignin() throws IOException {

        System.out.println("#######  注册接口运行  #######");

        String random = CommonMethod.getRandomString(17);
        String name = "user_" + random;
        String account = random + "@wanmen.org";
        // 构造请求体
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", name);
        userInfo.put("account", account);
        userInfo.put("password", "1111aaaa");
        String json = JSON.toJSONString(userInfo);

        // 通过 RequestBody 的 create 方法来创建媒体类型为 application/json 的内容并提交
        RequestBody requestBody = RequestBody.create(mediaType, json);
        // 发送请求
        Request request = new Request.Builder()
                .url(Host + "/main/signup")
                .post(requestBody)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            System.out.println(responseBody);

            return account;
        }
    }

    /**
     * 登陆接口
     *
     * @param resAccount
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String userLogin(String resAccount) throws IOException {

        System.out.println("#######  登陆接口运行  #######");

        // 构造请求体
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("account", resAccount);
        userInfo.put("password", "1111aaaa");
        String json = JSON.toJSONString(userInfo);

        // 通过 RequestBody 的 create 方法来创建媒体类型为 application/json 的内容并提交
        RequestBody requestBody = RequestBody.create(mediaType, json);
        // 发送请求
        Request request = new Request.Builder()
                .url(Host + "/main/signin")
                .post(requestBody)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            System.out.println(responseBody);
            String authorization = response.header("authorization");

            return authorization;
        }


    }

    /**
     * 抽奖接口
     *
     * @param resAuthorization
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static List<String> userLottery(String resAuthorization) throws IOException {

        System.out.println("#######  抽奖接口运行  #######");

        // 通过 RequestBody 的 create 方法来创建媒体类型为 application/json 的内容并提交

        // 发送请求
        Request request = new Request.Builder()
                .url(Host + "/lottery/draw")
                .get()
                .header("Authorization", resAuthorization)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            System.out.println(responseBody);
            // 通过jsonpath解析响应体中的json数据
            String awardId = JSONPathUtil.extract(responseBody, "$.awardId").toString();
            String awardName = JSONPathUtil.extract(responseBody, "$.awardName");
            List<String> awrdInfo = new ArrayList<>();
            awrdInfo.add(awardId);
            awrdInfo.add(awardName);

            return awrdInfo;
        }


    }


    public static void main(String[] args) throws IOException {

        String account = null;
        String auth = null;
        List<String> awardInfo;

        /*try {
            account = userSignin();
            System.out.println("用户名： " + account);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("@@@@@@@@@@@@@@@@@ 注册失败 @@@@@@@@@@@@@@@@@@@@");
        }

        try {
            auth = userLogin(account);
            System.out.println("认证： " + auth);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("@@@@@@@@@@@@@@@@@ 登陆失败 @@@@@@@@@@@@@@@@@@@@");
        }

        try {
            awardInfo = userLottery(auth);
            System.out.println("获奖ID： " + awardInfo.get(0));
            System.out.println("获奖名称： " + awardInfo.get(1));
        } catch (IOException e) {
            e.printStackTrace();
            log.info("@@@@@@@@@@@@@@@@@ 抽奖失败 @@@@@@@@@@@@@@@@@@@@");
        }*/

        account = userSignin();
        System.out.println("用户名： " + account);
        auth = userLogin(account);
        System.out.println("认证： " + auth);
        awardInfo = userLottery(auth);
        System.out.println("获奖ID： " + awardInfo.get(0));
        System.out.println("获奖名称： " + awardInfo.get(1));
    }


}
