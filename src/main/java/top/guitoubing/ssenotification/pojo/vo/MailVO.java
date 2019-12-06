package top.guitoubing.ssenotification.pojo.vo;

/**
 * @author tanrui
 * @date Oct 8
 */
public class MailVO {

  private String receiver;

  private PageVO content;

  public MailVO(String receiver, PageVO content) {
    this.receiver = receiver;
    this.content = content;
  }

  public MailVO(String receiver) {
    this(receiver, null);
  }

  public PageVO getContent() {
    return content;
  }

  public void setContent(PageVO content) {
    this.content = content;
  }

  public String getReceiver() {
    return receiver;
  }

  public void setReceiver(String receiver) {
    if (!receiver.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) {
      return;
    }
    this.receiver = receiver;
  }
}
