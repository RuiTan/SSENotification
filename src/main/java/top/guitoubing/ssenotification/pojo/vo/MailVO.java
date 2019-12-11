package top.guitoubing.ssenotification.pojo.vo;

/**
 * @author tanrui
 * @date Oct 8
 */
public class MailVO {

  private PageVO content;

  public MailVO(PageVO content) {
    this.content = content;
  }

  public PageVO getContent() {
    return content;
  }

  public void setContent(PageVO content) {
    this.content = content;
  }
}
