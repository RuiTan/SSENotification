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

  @RequestMapping(value = "addReceiver")
  public void addReceiver(String receiver){
    String regex = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    if (receiver.matches(regex)) {
      System.setProperty("RECEIVERS", System.getProperty("RECEIVERS") + "," + receiver);
      logger.info("收件人" + receiver + "添加成功");
    }
  }

  @RequestMapping(value = "setLastView")
  public void setLastView(Long lastViewId) {
    System.setProperty("LAST_VIEW", String.valueOf(lastViewId));
    logger.info("环境变量LAST_VIEW设置为：" + System.getProperty("LAST_VIEW"));
  }
}
