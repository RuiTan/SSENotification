package top.guitoubing.ssenotification.job;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

  @Bean
  public JobDetail sendEmailJob(){
    return JobBuilder.newJob(SendEmailJob.class).withIdentity("sendEmailJob").storeDurably().build();
  }

  @Bean
  public Trigger sendEmailTrigger(){
    return TriggerBuilder.newTrigger().forJob(sendEmailJob())
            .withIdentity("sendEmailTrigger")
            .withSchedule(CronScheduleBuilder.cronSchedule("0 */1 * * * ?"))
            .build();
  }

}
