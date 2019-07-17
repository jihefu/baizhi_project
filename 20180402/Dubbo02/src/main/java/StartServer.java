import com.sun.org.apache.xpath.internal.SourceTree;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created by Administrator on 2018/4/4.
 */
public class StartServer {
    public static void main(String[] args) throws IOException {
        ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
        System.out.println("启动服务工厂 B...");
        System.in.read();
    }
}
