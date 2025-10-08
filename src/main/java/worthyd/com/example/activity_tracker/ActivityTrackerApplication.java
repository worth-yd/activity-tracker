package worthyd.com.example.activity_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import worthyd.com.example.activity_tracker.kafka.service.*;

@SpringBootApplication
@EnableJpaRepositories("worthyd.com.example.activity_tracker.auth.repository")
@EntityScan("worthyd.com.example.activity_tracker.auth.model")
public class ActivityTrackerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ActivityTrackerApplication.class, args);
  }

  @RestController
  public class hello {

    private final HelloService helloService;

    public hello(HelloService hello) {
      this.helloService = hello;
    }

    @GetMapping("/send")
    public String send(@RequestParam String msg) {
      helloService.sendHello(msg);
      return "Message sent: " + msg;
    }
  }
}
