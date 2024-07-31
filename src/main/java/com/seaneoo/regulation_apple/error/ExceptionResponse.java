package com.seaneoo.regulation_apple.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@JsonInclude(NON_NULL)
public class ExceptionResponse {

    @JsonProperty("status_code")
    public Integer statusCode;

    @JsonProperty("status_reason")
    public String statusReason;

    public String path;

    public Instant timestamp;

    public String message;

    public List<String> errors;
}
