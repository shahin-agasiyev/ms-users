package az.ingress.queue.concrete;

import az.ingress.aop.annotation.Log;
import az.ingress.model.dto.EventDto;
import az.ingress.queue.abstraction.MessageSubscriber;
import az.ingress.service.abstraction.UserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PRIVATE;

@Log
@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class MessageSubscriberHandler implements MessageSubscriber {
    UserService userService;

    @RabbitListener(queues = "PUBLISHER_QUEUE")
    public void consumeEventMessage(EventDto eventDto) {
        log.info("Received event message from RabbitMQ: {}", eventDto);

        try {
            userService.notifyUsersAboutNewEvent(eventDto);
            log.info("Successfully processed event: {}", eventDto.getName());
        } catch (Exception ex) {
            log.error("Error processing event message: {}", eventDto, ex);
            throw ex;
        }
    }
}