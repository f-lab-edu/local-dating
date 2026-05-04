package com.local_dating.matching_service.util.exception;

import com.local_dating.matching_service.util.MessageCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BusinessException extends RuntimeException {

    private final MessageCode messageCode;
    private final String errorValue;
    private final List<Long> errorValues;

    public BusinessException(MessageCode messageCode) {
        super(messageCode.getMessage());
        this.messageCode = messageCode;
        this.errorValue = null;
        this.errorValues = null;
    }

    public BusinessException(MessageCode messageCode, String errorValue) {
        super(messageCode.getMessage());
        this.messageCode = messageCode;
        this.errorValue = errorValue;
        this.errorValues = null;
    }

    public BusinessException(MessageCode messageCode, List<Long> errorValues) {
        super(messageCode.getMessage());
        this.messageCode = messageCode;
        this.errorValue = null;
        this.errorValues = errorValues;
    }
}
