package top.guitoubing.ssenotification.pojo.vo;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author tanrui
 * @date Oct 8
 */
public class MailVO {

  private static ResourceBundle emailConfig = ResourceBundle.getBundle("emailConfig");
  private static String host;
  private static String sender;
  private static String name;
  private static String username;
  private static String password;
  private static Map<String, String> to;
  private static String encoding;
  private PageVO content;

  private MailVO(){}

  private void init(){
    if (host == null) {
      host = emailConfig.getString("mail.host");
    }
    if (sender == null) {
      sender = emailConfig.getString("mail.sender");
    }
    if (name == null) {
      name = emailConfig.getString("mail.name");
    }
    if (username == null) {
      username = emailConfig.getString("mail.sender");
    }
    if (password == null) {
      password = emailConfig.getString("mail.key");
    }
    if (to == null) {
      to = new HashMap<>();
      String[] receivers = emailConfig.getString("mail.receivers").split(",");
      for (String receiver : receivers) {
        to.put(receiver, receiver);
      }
    }
    if (encoding == null) {
      encoding = emailConfig.getString("mail.encoding");
    }
  }

  public MailVO(PageVO content){
    init();
    this.content = content;
  }

  public PageVO getContent() {
    return content;
  }

  public void setContent(PageVO content) {
    this.content = content;
  }

  public static ResourceBundle getEmailConfig() {
    return emailConfig;
  }

  public static String getHost() {
    return host;
  }

  public static String getSender() {
    return sender;
  }

  public static String getName() {
    return name;
  }

  public static String getUsername() {
    return username;
  }

  public static String getPassword() {
    return password;
  }

  public static Map<String, String> getTo() {
    return to;
  }

  public static String getEncoding() {
    return encoding;
  }

  //
//  public String getReceiver() {
//    return receiver;
//  }
//
//  public void setReceiver(String receiver) {
//    if (!receiver.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) {
//      return;
//    }
//    this.receiver = receiver;
//  }
}
