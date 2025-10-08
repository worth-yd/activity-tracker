
package worthyd.com.example.activity_tracker.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class HelloWorldProducer {
  private final KafkaTemplate<String, String> kafkaTemplate;

  public HelloWorldProducer(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendMessage(String message) {
    kafkaTemplate.send("hello-topic", message);
    System.out.println("✅ Sent: " + message);
  }
}
