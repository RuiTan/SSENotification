package top.guitoubing.ssenotification.service.impl;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.stereotype.Service;
import top.guitoubing.ssenotification.pojo.vo.MailVO;
import top.guitoubing.ssenotification.pojo.vo.PageVO;
import top.guitoubing.ssenotification.service.EmailService;
import top.guitoubing.ssenotification.utils.DateUtils;

import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class EmailServiceImpl implements EmailService {

  private static Logger logger = Logger.getLogger(EmailServiceImpl.class.getName());

  @Override
  public void send(PageVO content) {
    MailVO mailVO = new MailVO(content);
    logger.info("开始发送邮件 -- " + content.getTitle());
    if (!sendEmail(mailVO)) {
      logger.warning("发送失败！");
    }
  }

  private boolean sendEmail(MailVO mailVO) {
    HtmlEmail email = new HtmlEmail();

    try {
      email.setHostName(MailVO.getHost());
      email.setCharset(MailVO.getEncoding());
      email.setFrom(MailVO.getSender(), MailVO.getName());
      email.setAuthentication(MailVO.getUsername(), MailVO.getPassword());
      // 设置收件人
      if (!MailVO.getTo().isEmpty()) {
        for (Map.Entry<String, String> entry: MailVO.getTo().entrySet()) {
          email.addTo(entry.getKey(), entry.getValue());
        }
      }
      String date = DateUtils.formatDateToString(new Date(), DateUtils.YYYY_MM_DD);
      email.setSubject("【软件学院" + date + "通知】-" + mailVO.getContent().getTitle());
      email.setHtmlMsg(mailVO.getContent().getContent());
      email.send();
      logger.info("邮件已发送");
    } catch (EmailException e) {
      logger.warning("email配置失败！");
      return false;
    }
    return true;
  }
}
