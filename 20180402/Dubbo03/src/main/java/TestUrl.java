import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by Administrator on 2018/4/4.
 */
public class TestUrl {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String url="override%3A%2F%2F192.168.8.5%2Fcom.xxx.service.IUserService%3Fcategory%3Dconfigurators%26dynamic%3Dfalse%26enabled%3Dtrue%26mock%3Dforce%253Areturn%2Bnull";
        System.out.println(URLDecoder.decode(url,"utf-8"));
    }
}
