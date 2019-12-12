package top.guitoubing.ssenotification.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import top.guitoubing.ssenotification.pojo.vo.PageVO;
import top.guitoubing.ssenotification.service.EmailService;
import top.guitoubing.ssenotification.service.PageParseService;

import java.util.List;

public class SendEmailJob extends QuartzJobBean {

  @Autowired
  PageParseService pageParseService;

  @Autowired
  EmailService emailService;

  @Override
  protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    List<PageVO> pageVOS = pageParseService.parseNotificationPage();
    if (pageVOS != null && !pageVOS.isEmpty()) {
      for (PageVO pageVO : pageVOS) {
        emailService.send(pageVO);
      }
    }
  }
}
