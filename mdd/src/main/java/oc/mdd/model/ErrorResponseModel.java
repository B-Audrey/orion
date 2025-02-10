package oc.mdd.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ErrorResponseModel {

    private final LocalDateTime timestamp;
    private final int status;
    private final String message;

    public ErrorResponseModel(HttpStatus status, String message) {
        this.timestamp = LocalDateTime.now();
        this.status = status.value();
        this.message = message;
    }


}
