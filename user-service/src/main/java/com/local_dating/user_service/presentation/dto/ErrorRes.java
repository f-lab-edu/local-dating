package com.local_dating.user_service.presentation.dto;

import org.springframework.http.HttpStatus;

public record ErrorRes(HttpStatus httpStatus, String message) {
}
