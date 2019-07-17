import com.alibaba.dubbo.rpc.RpcContext;
import com.xxx.entities.User;
import com.xxx.service.IUserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2018/4/4.
 */
public class StartConsumer {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
        IUserService userService = (IUserService) ctx.getBean("userService");

       /* for(int i=0;i<10;i++){
            System.out.println(userService.queryUserById(0));
        }*/
       /* List<User> users = userService.queryAll();
        for (User user : users) {
            System.out.println(user);
        }
        System.out.println(userService.queryUserById(0));*/
       User user = userService.queryUserById(1);

        System.out.println(user);

       // System.in.read();
    }
}
