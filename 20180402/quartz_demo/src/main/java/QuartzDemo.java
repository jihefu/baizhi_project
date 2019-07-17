import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

// 静态导入
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
/**
 * spring task:
 *      简单调度  简单触发条件 间隔....
 *      cron调度  基于cron表达式
 *                      秒 分 时 日 月 周 年
 *                *    任意值
 *                ?    展位
 *                -    区间
 *                /    间隔
 *                特点： 日和周两个位置不能同时出现 *
 *                数值  具体值
 *                      0 0 10 * * ? 2018  // 2018年的每一天的10点0分0秒
 *                      * * * * * ？*      // 每一年的每一天的每小时的每一分钟的每秒
 *                      10-20 * * * * ？ *
 *                      10/5
 *
 *
 * Created by Administrator on 2018/4/28.
 */
public class QuartzDemo {

    public static void main(String[] args) throws SchedulerException {
        // 语法：静态导入+链式调用
        // 触发器
        Trigger trigger = TriggerBuilder.newTrigger()
                // 触发器 = name+group 管理
                .withIdentity("trigger1", "group1")
                // 立即启动
                .startNow()
                // 简单调度 每隔40秒执行一次
//                .withSchedule(simpleSchedule()
//                        .withIntervalInSeconds(5)
//                        .repeatForever())

                // 使用cron表达式进行任务调度
                .withSchedule(cronSchedule("10-30/2 * * * * ? *"))
                .build();
        // 封装任务内容
        JobDetail jobDetail = newJob(MyJob.class)
                .withIdentity("job1", "group1")
                .build();
        // 调度 装配
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        scheduler.scheduleJob(jobDetail,trigger);

        // 启动
        scheduler.start();
        System.out.println("renwukaishihou");
    }
}
