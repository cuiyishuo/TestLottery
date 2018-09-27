package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CommonMethod {

    int index;

    public String request() {

        List<String> list = new ArrayList<>();
        list.add("nano");
        list.add("kindle");
        list.add("card");

        index = (int) (Math.random() * list.size());

        return list.get(index);

    }

    /**
     * 生成随机用户名
     *
     * @return
     */
    public String buildAccount() {

        String userName = "";
        userName = "Lottery_" + getRandomString(16) + "@wanmen.org";

        return userName;
    }

    /**
     * 生成指定长度的随机字符串
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static void main(String[] args) {

        String name = getRandomString(10);
        System.out.println(name);

    }
}
