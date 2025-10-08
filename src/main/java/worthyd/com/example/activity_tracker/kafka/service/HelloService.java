package worthyd.com.example.activity_tracker.kafka.service;

import worthyd.com.example.activity_tracker.kafka.producer.*;

import org.springframework.stereotype.Service;

@Service
public class HelloService {
  private final HelloWorldProducer producer;

  public HelloService(HelloWorldProducer producer) {
    this.producer = producer;
  }

  public void sendHello(String message) {
    producer.sendMessage(message);
  }
}
