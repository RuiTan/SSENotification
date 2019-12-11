package top.guitoubing.ssenotification.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import top.guitoubing.ssenotification.pojo.vo.ResponseVO;

import java.util.logging.Logger;

@RestController
@CrossOrigin(maxAge = 3600, origins = "*")
public class IndexController {

  private static Logger logger = Logger.getLogger(IndexController.class.getName());

  @RequestMapping(value = "receiveNotification")
  @ResponseBody
  public ResponseVO receiveNotification(){
      return new ResponseVO(ResponseVO.SUCCESS_STATUS, "邮件发送成功");
  }
}
