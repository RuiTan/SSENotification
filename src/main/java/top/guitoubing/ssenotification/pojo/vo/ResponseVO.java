package top.guitoubing.ssenotification.pojo.vo;

public class ResponseVO {

  public static final String SUCCESS_STATUS = "success";
  public static final String FAILURE_STATUS = "failed";

  private String status;
  private String message;

  public ResponseVO(String status, String message) {
    this.status = status;
    this.message = message;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
