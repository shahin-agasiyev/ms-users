package az.ingress.queue.concrete;

import az.ingress.aop.annotation.Log;
import az.ingress.model.request.UserRequest;
import az.ingress.queue.abstraction.MessageSubscriber;
import az.ingress.service.abstraction.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static az.ingress.mapper.ObjectMapperFactory.OBJECT_MAPPER;
import static lombok.AccessLevel.PRIVATE;

@Log
@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class MessageSubscriberHandler implements MessageSubscriber {
    UserService userService;

    @SneakyThrows
    @RabbitListener(queues = "${rabbitmq.publisher-service.queue}")
    public void subscribe(String message) {
        try {
            var user = OBJECT_MAPPER.getInstance().readValue(message, UserRequest.class);
            userService.create(user);
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException: ", e);
        }
    }
}