package ru.com.alexsolo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
public class IllegalFormatException extends RuntimeException{
    public IllegalFormatException(String message) {
        super(message);
    }
}
