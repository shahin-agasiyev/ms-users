package az.ingress.queue.abstraction;

import az.ingress.model.dto.EventDto;

public interface MessageSubscriber {
    void consumeEventMessage(EventDto eventDto);
}