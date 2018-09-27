package business;

import api.APITest;
import api.OkHttpTest;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

import static api.APITest.userLottery;
import static api.OkHttpTest.userLogin;
import static api.OkHttpTest.userSignin;

public class LotterBussines {

    static Logger log = Logger.getLogger(LotterBussines.class.getName());

    public static List<String> getAward() throws ClientProtocolException, IOException {

        // 注册用户并获得账号
        String account = APITest.userSignin();
        // 登陆用户并获得auth
        String authorization = APITest.userLogin(account);
        // 查询用户抽奖数据
        //APITest.queryLotteryData(authorization);
        // 用户抽奖并返回获奖信息
        List<String> awardId = userLottery(authorization);

        System.out.println("#####################################################################################");

        return awardId;
    }

    public static List<String> getAwardByOk() throws IOException {

        String account = null;
        String auth = null;
        List<String> awardInfo = null;
        int signupError = 0;
        int loginError = 0;
        int lotterError = 0;


        try {
            account = OkHttpTest.userSignin();
            System.out.println("用户名： " + account);
        } catch (IOException e) {
            signupError++;
            e.printStackTrace();
            log.error("@@@@@@@@@@@@@@@@@ 注册失败 @@@@@@@@@@@@@@@@@@@@");
            log.error("注册失败次数： "+signupError);
            log.error(e);
            log.error(e.getMessage());
        }

        try {
            auth = OkHttpTest.userLogin(account);
            System.out.println("认证： " + auth);
        } catch (IOException e) {
            loginError++;
            e.printStackTrace();
            log.error("@@@@@@@@@@@@@@@@@ 登陆失败 @@@@@@@@@@@@@@@@@@@@");
            log.error("登陆失败次数： "+loginError);
            log.error(e);
            log.error(e.getMessage());
        }

        try {
            awardInfo = OkHttpTest.userLottery(auth);
            System.out.println("获奖ID： " + awardInfo.get(0));
            System.out.println("获奖名称： " + awardInfo.get(1));
        } catch (IOException e) {
            lotterError++;
            e.printStackTrace();
            log.error("@@@@@@@@@@@@@@@@@ 抽奖失败 @@@@@@@@@@@@@@@@@@@@");
            log.error("抽奖失败次数： "+lotterError);
            log.error(e);
            log.error(e.getMessage());
        }

        return awardInfo;
    }
}
