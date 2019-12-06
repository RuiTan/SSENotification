package top.guitoubing.ssenotification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SseNotificationApplication extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder springApplicationBuilder){
    return springApplicationBuilder.sources(this.getClass());
  }

  public static void main(String[] args) {
    SpringApplication.run(SseNotificationApplication.class, args);
  }

}
