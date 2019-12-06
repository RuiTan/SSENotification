package top.guitoubing.ssenotification.service.impl;

import org.springframework.stereotype.Service;
import top.guitoubing.ssenotification.pojo.vo.MailVO;
import top.guitoubing.ssenotification.pojo.vo.PageVO;
import top.guitoubing.ssenotification.service.EmailService;
import top.guitoubing.ssenotification.utils.DateUtils;
import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Logger;

@Service
public class EmailServiceImpl implements EmailService {

  private static Logger logger = Logger.getLogger(EmailServiceImpl.class.getName());

  private static ResourceBundle emailConfig = ResourceBundle.getBundle("emailConfig");

  private static String host = emailConfig.getString("mail.host");
  private static String sender = emailConfig.getString("mail.sender");
  private static String key = emailConfig.getString("mail.key");
  private static String receivers = emailConfig.getString("mail.receivers");

  @Override
  public void send(PageVO content) {
    String[] receiverArray = receivers.split(",");
    for (String receiver : receiverArray) {
      MailVO receiverVO = new MailVO(receiver, content);
      if (!sendEmail(receiverVO)) {
        logger.warning("邮件发送失败，收件人为：" + receiver);
      }
    }
  }

  public boolean sendEmail(MailVO mailVO) {

    Properties properties = new Properties();

    MailSSLSocketFactory sslSocketFactory = null;
    try {
      sslSocketFactory = new MailSSLSocketFactory();
      sslSocketFactory.setTrustAllHosts(true);
      properties.put("mail.smtp.ssl.socketFactory", sslSocketFactory);
    } catch (GeneralSecurityException e) {
      logger.warning("SSL验证失败");
      return false;
    }

    Enumeration<String> keys = emailConfig.getKeys();
    while (keys.hasMoreElements()){
      String key = keys.nextElement();
      properties.setProperty(key, emailConfig.getString(key));
    }
    Session session = Session.getInstance(properties);
    try {
      sendMessage(session.getTransport(), setMessage(session, mailVO.getContent()), mailVO.getReceiver());
    } catch (NoSuchProviderException e) {
      logger.warning("transport获取失败");
      return false;
    }
    return true;
  }

  private void sendMessage(Transport transport, Message message, String receiver){
    try {
      transport.connect(host, sender, key);
      transport.sendMessage(message, new Address[]{new InternetAddress(receiver)});
      transport.close();
    } catch (NoSuchProviderException e) {
      logger.warning("获取transport失败");
    } catch (MessagingException e) {
      logger.warning("transport连接失败");
    }
  }

  private Message setMessage(Session session, PageVO content){
    // 邮件内容
    MimeMessage message = new MimeMessage(session);
    String date = DateUtils.formatDateToString(new Date(), DateUtils.YYYY_MM_DD);
    try {
      message.setSubject("【软件学院" + date + "通知】-" + content.getTitle());
      message.setText(content.toString());
      message.setFrom(new InternetAddress(sender));
    } catch (MessagingException e) {
      logger.warning("邮件构建失败");
    }
    return message;
  }
}
