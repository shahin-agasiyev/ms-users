package az.ingress.queue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeadLetterQueueConsumer {

    @RabbitListener(queues = "PUBLISHER_DLQ")
    public void processFailedMessages(String message) {
    }
}