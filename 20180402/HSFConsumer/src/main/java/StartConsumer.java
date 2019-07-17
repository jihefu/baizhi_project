import com.xxx.commons.RpcContext;
import com.xxx.entities.User;
import com.xxx.service.IUserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by Administrator on 2018/4/4.
 */
public class StartConsumer {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        ApplicationContext ctx=new ClassPathXmlApplicationContext("consumer.xml");
        Object host1 = RpcContext.getContext().getAttchment(RpcContext.REMOTE_HOST);
        IUserService userService = (IUserService) ctx.getBean("userService");
        for(int i=0;i<10;i++){
            System.out.println(userService.queryUserById(i));
        }
    }
}
