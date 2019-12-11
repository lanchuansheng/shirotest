package com.lcs.util;

import app.yzb.vo.UserInfoVO;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.apache.commons.lang.StringUtils;
import org.phprpc.util.AssocArray;
import org.phprpc.util.Cast;
import org.phprpc.util.PHPSerializer;

/**
 * Created by lowdad on 18-1-23.
 */
public class UserUtil {

    public static UserInfoVO getUserinfo(Integer userid) {

        String result = null;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://userengine.umiwi.com/interface.php?do=getInfoByUidAct&service=ReaderAct&node=webserver_01&remote_addr=113.6.237.70&param%5Buid%5D=" + userid).build();
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(result);
        UserInfoVO vo = new UserInfoVO();
        PHPSerializer p = new PHPSerializer();
        if (StringUtils.isEmpty(result))
            return vo;
        try {
            AssocArray array = (AssocArray) p.unserialize(result.getBytes());
            if (array != null) {
                AssocArray data = (AssocArray) array.get("DATA");
                System.out.println(data);
                if (data != null) {
                    vo.setUid(userid);
                    String phone = (String) Cast.cast(data.get("mobile"), String.class);
                    vo.setPhone(phone);
                    String nickname = (String) Cast.cast(data.get("username"), String.class);
                    vo.setNickname(nickname);
                    String uid = String.valueOf(userid);
                    String jiequ = uid.substring(uid.length() - 2);
                    String touxiang = "http://i2.umivi.net/avatar/" + jiequ + "/" + userid + "m.jpg";
                    vo.setHeadimgurl(touxiang);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return vo;
    }

    public static void main(String[] args) {
        getUserinfo(7106276);

    }


}
