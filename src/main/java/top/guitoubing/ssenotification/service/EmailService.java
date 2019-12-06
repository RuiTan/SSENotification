package top.guitoubing.ssenotification.service;

import top.guitoubing.ssenotification.pojo.vo.PageVO;

public interface EmailService {

  void send(PageVO content);
}
