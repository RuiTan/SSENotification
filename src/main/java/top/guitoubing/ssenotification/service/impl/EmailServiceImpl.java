package top.guitoubing.ssenotification.service.impl;

import org.springframework.stereotype.Service;
import top.guitoubing.ssenotification.pojo.vo.MailVO;
import top.guitoubing.ssenotification.pojo.vo.PageVO;
import top.guitoubing.ssenotification.service.EmailService;
import top.guitoubing.ssenotification.utils.DateUtils;
import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
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

  private static String[] getReceivers() {
    String receiverProperty = System.getProperty("RECEIVERS");
    if (receiverProperty == null || receiverProperty.isEmpty()) {
      System.setProperty("RECEIVERS", emailConfig.getString("mail.receivers"));
      return emailConfig.getString("mail.receivers").split(",");
    } else {
      return receiverProperty.split(",");
    }
  }

  private static String username = emailConfig.getString("mail.username");

  @Override
  public void send(PageVO content) {
    MailVO receiverVO = new MailVO(content);
    if (!sendEmail(receiverVO)) {
      logger.warning("邮件发送失败!");
    }
  }

  private boolean sendEmail(MailVO mailVO) {

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
      String[] receivers = getReceivers();
      sendMessage(session.getTransport(), setMessage(session, mailVO.getContent(), receivers));
      logger.info("邮件成功发送给：" + Arrays.toString(receivers));
    } catch (NoSuchProviderException e) {
      logger.warning("transport获取失败");
      return false;
    }
    return true;
  }

  private void sendMessage(Transport transport, Message message){
    try {
      transport.connect(host, sender, key);
      transport.sendMessage(message, message.getAllRecipients());
      transport.close();
    } catch (NoSuchProviderException e) {
      logger.warning("获取transport失败");
    } catch (MessagingException e) {
      logger.warning("transport连接失败");
    }
  }

  private Message setMessage(Session session, PageVO content, String[] receivers){
    // 邮件内容
    MimeMessage message = new MimeMessage(session);
    String date = DateUtils.formatDateToString(new Date(), DateUtils.YYYY_MM_DD);
    try {
      message.setFrom(new InternetAddress(sender, username));
      message.setSubject("【软件学院" + date + "通知】-" + content.getTitle(), "UTF-8");
      for (String receiver : receivers) {
        message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiver));
      }

      MimeMultipart mimeMultipart = new MimeMultipart();
      MimeBodyPart text = new MimeBodyPart();
      text.setContent(content.getContent(), "text/html;charset=UTF-8");
      mimeMultipart.addBodyPart(text);
      mimeMultipart.setSubType("related");
      message.setContent(mimeMultipart);
    } catch (MessagingException e) {
      logger.warning("邮件构建失败");
    } catch (UnsupportedEncodingException e) {
      logger.warning("发件人信息错误");
    }
    return message;
  }
}
