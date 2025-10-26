package az.ingress.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
    UNEXPECTED_ERROR("Unexpected error occurred"),
    USER_NOT_FOUND("User with id: %s not found");

    private final String message;
}
