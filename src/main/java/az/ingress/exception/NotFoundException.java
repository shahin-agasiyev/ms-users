package az.ingress.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message, Object... args) {
        super(message.formatted(args));
    }
}
