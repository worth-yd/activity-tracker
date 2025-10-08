
package worthyd.com.example.activity_tracker.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class HelloWorldConsumer {

  @KafkaListener(topics = "hello-topic", groupId = "hello-group")
  public void listen(String message) throws InterruptedException {
    System.out.println("📩 Received: " + message);
  }
}
