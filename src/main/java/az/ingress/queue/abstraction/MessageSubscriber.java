package az.ingress.queue.abstraction;

public interface MessageSubscriber {
    void subscribe(String message);
}