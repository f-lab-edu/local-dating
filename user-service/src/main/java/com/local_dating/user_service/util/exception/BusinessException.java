package com.local_dating.user_service.util.exception;

import com.local_dating.user_service.util.MessageCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends RuntimeException {

    private final MessageCode messageCode;
    private final String errorValue;

    public BusinessException(MessageCode messageCode) {
        super(messageCode.getMessage());
        this.messageCode = messageCode;
        this.errorValue = null;
    }

    public BusinessException(MessageCode messageCode, String errorValue) {
        super(messageCode.getMessage());
        this.messageCode = messageCode;
        this.errorValue = errorValue;
    }
}
