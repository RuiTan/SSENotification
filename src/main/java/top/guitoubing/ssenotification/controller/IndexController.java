package top.guitoubing.ssenotification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import top.guitoubing.ssenotification.pojo.vo.PageVO;
import top.guitoubing.ssenotification.pojo.vo.ResponseVO;
import top.guitoubing.ssenotification.service.EmailService;
import top.guitoubing.ssenotification.service.PageParseService;

import java.util.List;
import java.util.logging.Logger;

@RestController
@CrossOrigin(maxAge = 3600, origins = "*")
public class IndexController {

  private static Logger logger = Logger.getLogger(IndexController.class.getName());

  @Autowired
  EmailService emailService;

  @Autowired
  PageParseService pageParseService;

  @RequestMapping(value = "receiveNotification")
  @ResponseBody
  public ResponseVO receiveNotification(){
    logger.info("start to receive notification");
    List<PageVO> contents = pageParseService.parseNotificationPage();
    for (PageVO content : contents) {
      emailService.send(content);
    }
    return new ResponseVO(ResponseVO.SUCCESS_STATUS, "send email successfully");
  }

}
