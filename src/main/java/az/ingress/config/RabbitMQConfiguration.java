package az.ingress.config;

import lombok.experimental.FieldDefaults;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static az.ingress.mapper.ObjectMapperFactory.OBJECT_MAPPER;
import static az.ingress.model.constants.QueueConstants.PUBLISHER_DLQ;
import static az.ingress.model.constants.QueueConstants.PUBLISHER_DLQ_EXCHANGE;
import static az.ingress.model.constants.QueueConstants.PUBLISHER_DLQ_ROUTING_KEY;
import static az.ingress.model.constants.QueueConstants.PUBLISHER_EXCHANGE;
import static az.ingress.model.constants.QueueConstants.PUBLISHER_QUEUE;
import static az.ingress.model.constants.QueueConstants.PUBLISHER_ROUTING_KEY;
import static lombok.AccessLevel.PRIVATE;

@Configuration
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class RabbitMQConfiguration {

    @Bean
    public Queue subscriberQueue() {
        return QueueBuilder.durable(PUBLISHER_QUEUE)
                .withArgument("x-dead-letter-exchange", PUBLISHER_DLQ_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", PUBLISHER_DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(PUBLISHER_DLQ).build();
    }

    @Bean
    public TopicExchange subscriberExchange() {
        return new TopicExchange(PUBLISHER_EXCHANGE);
    }

    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(PUBLISHER_DLQ_EXCHANGE);
    }

    @Bean
    public Binding subscriberBinding(Queue subscriberQueue, TopicExchange subscriberExchange) {
        return BindingBuilder
                .bind(subscriberQueue)
                .to(subscriberExchange)
                .with(PUBLISHER_ROUTING_KEY);
    }

    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, TopicExchange deadLetterExchange) {
        return BindingBuilder
                .bind(deadLetterQueue)
                .to(deadLetterExchange)
                .with(PUBLISHER_DLQ_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        var objectMapper = OBJECT_MAPPER.getInstance();
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        var template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        var factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        factory.setDefaultRequeueRejected(false);
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        factory.setPrefetchCount(10);
        return factory;
    }
}