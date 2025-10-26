package az.ingress.config;

import lombok.experimental.FieldDefaults;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static lombok.AccessLevel.PRIVATE;

@Configuration
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class RabbitMQConfiguration {
    String publisherQ;
    String publisherDLQ;
    String publisherQExchange;
    String publisherDLQExchange;
    String publisherQKey;
    String publisherDLQKey;

    public RabbitMQConfiguration(@Value("${rabbitmq.publisher-service.queue}") String publisherQ,
                                 @Value("${rabbitmq.publisher-service.dlq}") String publisherDLQ) {

        this.publisherQ = publisherQ;
        this.publisherDLQ = publisherDLQ;
        this.publisherQExchange = publisherQ + "_EXCHANGE";
        this.publisherDLQExchange = publisherDLQ + "_EXCHANGE";
        this.publisherQKey = publisherQ + "_KEY";
        this.publisherDLQKey = publisherDLQ + "_KEY";
    }

    @Bean
    DirectExchange publisherDLQExchange() {
        return new DirectExchange(publisherDLQExchange);
    }

    @Bean
    DirectExchange publisherQExchange() {
        return new DirectExchange(publisherQExchange);
    }

    @Bean
    Queue publisherDLQ() {
        return QueueBuilder.durable(publisherDLQ).build();
    }

    @Bean
    Queue publisherQ() {
        return QueueBuilder.durable(publisherQ)
                .withArgument("x-dead-letter-exchange", publisherDLQExchange)
                .withArgument("x-dead-letter-routing-key", publisherDLQKey)
                .build();
    }

    @Bean
    Binding publisherDLQBinding() {
        return BindingBuilder.bind(publisherDLQ())
                .to(publisherDLQExchange()).with(publisherDLQKey);
    }

    @Bean
    Binding publisherQBinding() {
        return BindingBuilder.bind(publisherQ())
                .to(publisherQExchange()).with(publisherQKey);
    }
}