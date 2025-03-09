package com.local_dating.matching_service.util.exception;

import com.local_dating.matching_service.util.MessageCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends RuntimeException {

    private final MessageCode messageCode;

    public BusinessException(MessageCode messageCode) {
        super(messageCode.getMessage());
        this.messageCode = messageCode;
    }
}
