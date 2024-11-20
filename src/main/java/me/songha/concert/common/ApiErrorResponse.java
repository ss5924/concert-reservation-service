package me.songha.concert.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApiErrorResponse {
    private String message;
    private int httpStatus;
}
