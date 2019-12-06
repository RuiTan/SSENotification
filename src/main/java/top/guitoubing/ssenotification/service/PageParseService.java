package top.guitoubing.ssenotification.service;

import top.guitoubing.ssenotification.pojo.vo.PageVO;

import java.util.List;

public interface PageParseService {
  List<PageVO> parseNotificationPage();
}
