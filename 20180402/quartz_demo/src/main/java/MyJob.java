import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * Created by Administrator on 2018/4/28.
 */
public class MyJob implements Job {
    /**
     * 任务内容
     * @param jobExecutionContext   任务执行上下文 获取调度信息
     * @throws JobExecutionException
     */
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println(new Date());
    }
}
