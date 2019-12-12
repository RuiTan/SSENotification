package top.guitoubing.ssenotification.service.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import top.guitoubing.ssenotification.pojo.vo.PageVO;
import top.guitoubing.ssenotification.service.PageParseService;
import top.guitoubing.ssenotification.utils.DateUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
public class PageParseServiceImpl implements PageParseService {

  private static final Logger logger = Logger.getLogger(PageParseServiceImpl.class.getName());
  private static final String HOST = "http://sse.tongji.edu.cn";
  private static final String BASE_ROUTE = "/data/list/xwdt";
  private static Long LAST_VIEW;

  static {
    String lastView = System.getProperty("LAST_VIEW");
    if (lastView == null || lastView.isEmpty()) {
      try {
        LAST_VIEW = getYesterdayViewId();
        System.setProperty("LAST_VIEW", String.valueOf(LAST_VIEW));
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      LAST_VIEW = Long.valueOf(lastView);
    }
    logger.info("环境变量LAST_VIEW为：" + LAST_VIEW);
  }

  @Override
  public List<PageVO> parseNotificationPage() {
    Document response;
    try {
      response = Jsoup.connect(HOST + BASE_ROUTE).get();
      List<Element> notificationList = response.getElementsByClass("data-list").get(0).children();
      List<PageVO> result = new ArrayList<>();
      for (Element element : notificationList) {
        if (isNotificationValid(element)) {
          Element notification = element.getElementsByTag("a").get(0);

          result.add(generatePageVO(notification.attr("href")));
        } else {
          break;
        }
      }
      updateLastView();
      return result;
    } catch (IOException e) {
      logger.warning("connect failed: " + HOST + BASE_ROUTE);
    }
    return null;
  }

  private void updateLastView() throws IOException {
    LAST_VIEW = getViewIdByPage();
    logger.info("环境变量LAST_VIEW更新为：" + LAST_VIEW);
  }

  private static Long getViewIdByElement(Element element){
    String href = element.getElementsByTag("a").get(0).attr("href");
    return Long.valueOf(href.substring(href.length() - 4));
  }

  private Long getViewIdByPage() throws IOException {
    String href = Jsoup.connect(HOST + BASE_ROUTE).get().getElementsByClass("data-list").get(0).child(0).getElementsByTag("a").get(0).attr("href");
    return Long.valueOf(href.substring(href.length() - 4));
  }

  private static Long getYesterdayViewId() throws IOException {
    Elements elements = Jsoup.connect(HOST + BASE_ROUTE).get().getElementsByClass("data-list").get(0).children();
    String today = DateUtils.formatDateToString(new Date(), DateUtils.YYYY_MM_DD);
    for (Element element : elements) {
      String viewDate = element.getElementsByTag("span").get(0).text();
      if (!viewDate.equals(today)) {
        return getViewIdByElement(element);
      }
    }
    return null;
  }

  private PageVO generatePageVO(String url_suffix) {
    Document response;
    try {
      response = Jsoup.connect(HOST + url_suffix).get();
      return new PageVO(
              response.getElementsByClass("view-title").get(0).text(),
              response.getElementsByClass("view-info").get(0).text(),
              correctContent(response.getElementsByClass("view-cnt").get(0))
              );
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private String correctContent(Element content){
    modifyAttachmentURL(content);
    modifyImageURL(content);
    return content.html();
  }

  private void modifyAttachmentURL(Element content){
    for (Element a : content.getElementsByTag("a")) {
      String href = a.attr("href");
      if (href.startsWith("/")) {
        a.attr("href", HOST + href);
      }
    }
  }

  private void modifyImageURL(Element content) {
    for (Element img : content.getElementsByTag("img")){
      String href = img.attr("href");
      if (href.startsWith("/")) {
        img.attr("href", HOST + href);
      }
    }
  }

  private boolean isNotificationValid(Element element){
    Long viewId = getViewIdByElement(element);
    return viewId > LAST_VIEW;
  }
}
