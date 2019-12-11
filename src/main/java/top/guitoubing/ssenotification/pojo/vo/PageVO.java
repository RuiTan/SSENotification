package top.guitoubing.ssenotification.pojo.vo;

import top.guitoubing.ssenotification.utils.DateUtils;

import java.util.Date;

public class PageVO {
  private String title;
  private String info;
  private String content;

  public PageVO(String title, String info, String content) {
    this.title = title;
    this.info = info;
    this.content = content;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public String toString() {
    return title + "<br>" + info + "<br>" + content;
  }

  public static PageVO defaultPageVO(){
    return new PageVO(DateUtils.formatDateToString(new Date(), DateUtils.YYYY_MM_DD) + " 学院今天无通知~", "", "");
  }
}
