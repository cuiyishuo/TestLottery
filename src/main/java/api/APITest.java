package api;

import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import util.CommonMethod;
import util.JSONPathUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class APITest {

    static Logger log = Logger.getLogger(APITest.class.getName());
    private static CloseableHttpClient httpClient = null;
    private static String host = "https://alpha-api.wanmen.org/4.0";

    /**
     * 注册接口
     *
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String userSignin() throws ClientProtocolException, IOException {

        System.out.println("#######  注册接口运行  ######");

        // 创建一个httpclient，用于执行请求
        httpClient = HttpClients.createDefault();
        // 发送一个post请求并接受
        HttpPost httpPost = new HttpPost(host + "/main/signup");

        // 设置请求头信息
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.89 Safari/537.36");

        // 构造请求体数据
        String random = CommonMethod.getRandomString(16);
        String username = "user_" + random;
        String account = "Lottery_" + random + "@wanmen.org";
        String pwd = "1111aaaa";

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("name", username));
        params.add(new BasicNameValuePair("account", account));
        params.add(new BasicNameValuePair("password", pwd));
        UrlEncodedFormEntity reqEntity = new UrlEncodedFormEntity(params, Charset.forName("utf-8"));

        httpPost.setEntity(reqEntity);
        //System.out.println("content:" + reqEntity.toString());

        // 获取响应信息
        CloseableHttpResponse res = httpClient.execute(httpPost);
        //String result = EntityUtils.toString(res.getEntity(), "utf-8");

        // 返回登陆账号
        System.out.println("用户名：" + account);
        return account;

    }

    /**
     * 登陆接口
     *
     * @param resAccount
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String userLogin(String resAccount) throws ClientProtocolException, IOException {

        System.out.println("#######  登陆接口运行  ######");

        // 创建一个httpclient，用于执行请求
        httpClient = HttpClients.createDefault();
        // 发送一个post请求并接受
        HttpPost httpPost = new HttpPost(host + "/main/signin");

        // 设置请求头信息
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.89 Safari/537.36");

        // 构造请求体数据
        String account = resAccount;
        String pwd = "1111aaaa";

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("account", account));
        params.add(new BasicNameValuePair("password", pwd));
        UrlEncodedFormEntity reqEntity = new UrlEncodedFormEntity(params, Charset.forName("utf-8"));

        httpPost.setEntity(reqEntity);
        //System.out.println("content:" + reqEntity.toString());

        // 获取响应信息
        CloseableHttpResponse res = httpClient.execute(httpPost);
        String result = EntityUtils.toString(res.getEntity(), "utf-8");

        // 获取header中的信息
        String header = res.getFirstHeader("Authorization").toString();
        String authentication = header.split(": ")[1];

        System.out.println("Authorization:" + authentication);
        System.out.println("entityResult:" + result);

        // 返回登陆账号
        return authentication;

    }

    /**
     * 查询用户抽奖数据接口
     *
     * @param resAuthorization
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String queryLotteryData(String resAuthorization) throws ClientProtocolException, IOException {

        System.out.println("#######  查询用户抽奖数据接口运行  ######");

        // 创建一个httpclient，用于执行请求
        httpClient = HttpClients.createDefault();
        // 发送一个post请求并接受
        HttpGet httpGet = new HttpGet(host + "/lottery");

        // 设置请求头信息
        httpGet.addHeader("Authorization", resAuthorization);
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.89 Safari/537.36");

        // 获取响应信息
        CloseableHttpResponse res = httpClient.execute(httpGet);
        //String result = EntityUtils.toString(res.getEntity(), "utf-8");

        //System.out.println("entityResult:" + result);

        // 返回登陆账号
        return null;

    }

    /**
     * 用户进行抽奖接口
     *
     * @param resAuthorization
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static List userLottery(String resAuthorization) throws ClientProtocolException, IOException {

        System.out.println("#######  用户进行抽奖接口运行  ######");

        // 创建一个httpclient，用于执行请求
        httpClient = HttpClients.createDefault();
        // 发送一个post请求并接受
        HttpGet httpGet = new HttpGet(host + "/lottery/draw");

        // 设置请求头信息
        httpGet.addHeader("Authorization", resAuthorization);
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.89 Safari/537.36");

        // 获取响应信息
        CloseableHttpResponse res = httpClient.execute(httpGet);
        String result = EntityUtils.toString(res.getEntity(), "utf-8");

        System.out.println("entityResult:" + result);

        String awardId = JSONPathUtil.extract(result, "$.awardId").toString();
        String awardName = JSONPathUtil.extract(result, "$.awardName");

        List<String> awrdInfo = new ArrayList<>();
        awrdInfo.add(awardId);
        awrdInfo.add(awardName);

        System.out.println("获奖ID: " + awrdInfo.get(0) + "    获奖名称： " + awrdInfo.get(1));

        // 返回登陆账号
        return awrdInfo;

    }

    public static void main(String[] args) {
        try {
            String account = APITest.userSignin();
            String authorization = APITest.userLogin(account);
            //APITest.queryLotteryData(authorization);
            APITest.userLottery(authorization);

        } catch (ClientProtocolException c) {

        } catch (IOException i) {

        }

    }
}
