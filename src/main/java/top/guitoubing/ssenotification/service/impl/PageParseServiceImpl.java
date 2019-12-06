package top.guitoubing.ssenotification.service.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import top.guitoubing.ssenotification.pojo.vo.PageVO;
import top.guitoubing.ssenotification.service.PageParseService;
import top.guitoubing.ssenotification.utils.DateUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
public class PageParseServiceImpl implements PageParseService {

  private static final Logger logger = Logger.getLogger(PageParseServiceImpl.class.getName());
  private static final String HOST = "http://sse.tongji.edu.cn";
  private static final String BASE_ROUTE = "/data/list/xwdt";

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
      return result;
    } catch (IOException e) {
      logger.warning("connect failed: " + HOST + BASE_ROUTE);
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
              response.getElementsByClass("view-cnt").get(0).text()
              );
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private boolean isNotificationValid(Element element){
    String notificationTime = element.getElementsByTag("span").get(0).text();
    String now = DateUtils.formatDateToString(new Date(), DateUtils.YYYY_MM_DD);
//    Calendar calendar = Calendar.getInstance();
//    calendar.setTime(new Date());
//    calendar.add(Calendar.DAY_OF_YEAR, -1);
//    String now = DateUtils.formatDateToString(calendar.getTime(), DateUtils.YYYY_MM_DD);
    return notificationTime.equals(now);
  }
}
