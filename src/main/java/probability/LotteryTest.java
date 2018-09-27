package probability;

import business.LotterBussines;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author 10400
 * @create 2018-04-19 20:38
 */
public class LotteryTest {

    static Logger log = Logger.getLogger(LotteryTest.class.getName());

    //1、配置线程池
    private static ExecutorService es = Executors.newFixedThreadPool(5);
    private int kindleNum;
    private int watchNum;
    private int bugNum;
    private int artificialNum;
    private int seniorNum;
    private int juniorNum;
    private int persongrowNum;
    private int languageNum;
    private static int errorNum = 0;
    List<Future> resultList;

    //2、封装响应Feature
    class BizResult {
        public String orderId;

        public String getOrderId() {

            return orderId;
        }

        public void setOrderId(String orderId) {

            this.orderId = orderId;
        }

    }


    //3、实现Callable接口
    class BizTask implements Callable {

        private String orderId;


        public BizTask(String orderId) {
            this.orderId = orderId;

        }

        @Override
        public Object call() {

            List<String> awardInfo = null;


            try {
                //todo business
                System.out.println("当前线程Id = " + this.orderId);

                BizResult br = new BizResult();
                br.setOrderId(this.orderId);

                // 调用抽奖结果，得到中奖信息，并统计失败次数
                try {
                    awardInfo = LotterBussines.getAwardByOk();
                } catch (Exception e) {
                    e.printStackTrace();
                    errorNum++;
                }
                System.out.println("业务失败次数： " + errorNum);
                // 获得中奖ID，匹配后统计次数
                String awardName = awardInfo.get(1);
                // 根据中奖结果统计中奖个数和概率
                switch (awardInfo.get(0)) {
                    // kindle
                    case "5ba0cb9058918673be3fb4fe":
                        System.out.println("中奖名称： " + awardName);
                        kindleNum++;
                        break;
                    // 手表
                    case "5ba0cba058918673be3fb503":
                        System.out.println("中奖名称： " + awardName);
                        watchNum++;
                        break;
                    // 万门帆布袋
                    case "5ba0cbae58918673be3fb506":
                        System.out.println("中奖名称： " + awardName);
                        bugNum++;
                        break;
                    //  人工智能课程
                    case "5ba0cbb858918673be3fb508":
                        System.out.println("中奖名称： " + awardName);
                        artificialNum++;
                        break;
                    // 高中全科学习卡
                    case "5ba0cbc358918673be3fb50a":
                        System.out.println("中奖名称： " + awardName);
                        seniorNum++;
                        break;
                    // 初中全科学习卡
                    case "5ba0cbcc58918673be3fb510":
                        System.out.println("中奖名称： " + awardName);
                        juniorNum++;
                        break;
                    // 《怎么样管理好你的时间》课程
                    case "5ba0cbe258918673be3fb515":
                        System.out.println("中奖名称： " + awardName);
                        persongrowNum++;
                        break;
                    // 《100句走遍日本之超实用旅游日语》课程
                    case "5ba0cbea58918673be3fb517":
                        System.out.println("中奖名称： " + awardName);
                        languageNum++;
                        break;
                }

                // 计算已将抽奖的次数
                int haveLottery = kindleNum + watchNum + bugNum + artificialNum + seniorNum + juniorNum + persongrowNum + languageNum;

                // 每500个人打印一下当前的中奖概率到日志
                if ((haveLottery % 5) == 0) {
                    log.info("中了【kindle】的数量：" + kindleNum + "  ,中奖概率是： " + ((float) kindleNum / haveLottery) * 100 + "%");
                    log.info("中了【手表】的数量：" + watchNum + "  ,中奖概率是： " + ((float) watchNum / haveLottery) * 100 + "%");
                    log.info("中了【万门帆布袋】的数量：" + bugNum + "  ,中奖概率是： " + ((float) bugNum / haveLottery) * 100 + "%");
                    log.info("中了【人工智能课程】的数量：" + artificialNum + "  ,中奖概率是： " + ((float) artificialNum / haveLottery) * 100 + "%");
                    log.info("中了【高中学习课程】的数量：" + seniorNum + "  ,中奖概率是： " + ((float) seniorNum / haveLottery) * 100 + "%");
                    log.info("中了【初中学习课程】的数量：" + juniorNum + "  ,中奖概率是： " + ((float) juniorNum / haveLottery) * 100 + "%");
                    log.info("中了【《怎么样管理好你的时间》课程】的数量：" + persongrowNum + "  ,中奖概率是： " + ((float) persongrowNum / haveLottery) * 100 + "%");
                    log.info("中了【《100句走遍日本之超实用旅游日语》课程】的数量：" + languageNum + "  ,中奖概率是： " + ((float) languageNum / haveLottery) * 100 + "%");
                    log.info("当前抽奖次数： " + haveLottery);
                    log.info("业务失败次数： " + errorNum);
                }

                // 设置每组线程的间隔时间
                Thread.sleep(2000);
                return br;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 业务逻辑入口
     */
    public List<Future> beginBusiness() throws InterruptedException, ExecutionException {
        // 模拟批量业务数据
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            list.add(String.valueOf(i));
        }

        // 接收多线程响应结果
        resultList = new ArrayList<>();

        //begin thread
        for (int i = 0, size = list.size(); i < size; i++) {
            //todo something befor thread
            Future future = es.submit(new BizTask(list.get(i)));
            resultList.add(future);
        }

        for (Future f : resultList) {
            f.get();
        }

        log.info("中了【kindle】的数量：" + kindleNum + "  ,中奖概率是： " + ((float) kindleNum / (resultList.size() - errorNum)) * 100 + "%");
        log.info("中了【手表】的数量：" + watchNum + "  ,中奖概率是： " + ((float) watchNum / (resultList.size() - errorNum)) * 100 + "%");
        log.info("中了【万门帆布袋】的数量：" + bugNum + "  ,中奖概率是： " + ((float) bugNum / (resultList.size() - errorNum)) * 100 + "%");
        log.info("中了【人工智能课程】的数量：" + artificialNum + "  ,中奖概率是： " + ((float) artificialNum / (resultList.size() - errorNum)) * 100 + "%");
        log.info("中了【高中学习课程】的数量：" + seniorNum + "  ,中奖概率是： " + ((float) seniorNum / (resultList.size() - errorNum)) * 100 + "%");
        log.info("中了【初中学习课程】的数量：" + juniorNum + "  ,中奖概率是： " + ((float) juniorNum / (resultList.size() - errorNum)) * 100 + "%");
        log.info("中了【《怎么样管理好你的时间》课程】的数量：" + persongrowNum + "  ,中奖概率是： " + ((float) persongrowNum / (resultList.size() - errorNum)) * 100 + "%");
        log.info("中了【《100句走遍日本之超实用旅游日语》课程】的数量：" + languageNum + "  ,中奖概率是： " + ((float) languageNum / (resultList.size() - errorNum)) * 100 + "%");
        log.info("业务失败次数： " + errorNum);

        log.info(" =====多线程执行结束====== ");

        //wait finish
        return resultList;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        LotteryTest ft = new LotteryTest();
        List<Future> futures = ft.beginBusiness();
        log.info("futures.size() = " + futures.size());
        log.info("失败线程数： " + errorNum);
        //todo some operate
        log.info(" ==========================end========================= ");
    }

}
